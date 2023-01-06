package com.yyds.controller;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.javamenc.constant.MessageConstant;
import com.javamenc.constant.RedisConstant;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Member;
import com.yyds.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result check(@RequestBody Map<String, String> map, HttpServletResponse response, HttpServletRequest request){
        try {
            //2. 从map中获取手机号和用户输入的验证码
            String telephone = map.get("telephone");
            String validateCode = map.get("validateCode");
            String code = jedisPool.getResource().get(telephone + "-" + RedisConstant.SENDTYPE_LOGIN);

            if (StringUtils.isEmpty(code)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            if (!StringUtils.isEquals(validateCode, code)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //5. 验证码一致，判断是否是会员
            Member member = memberService.findByTel(telephone);

            //5.1 非会员则保存会员信息
            if (null == member) {
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            // 登录成功   将手机号码写入到 Cookie中  跟踪用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");                    // 根路径
            cookie.setMaxAge(60 * 60*24*7);         // 用户信息保存7天
            response.addCookie(cookie);

            request.getSession().setAttribute("member",member);

            // 将会员信息保存到 Redis中
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60 * 30, json); // 30分钟

            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.LOGIN_FAIL);
        }
    }


    @PostMapping("/exit")
    public Result exit(HttpServletRequest request){
        request.getSession().invalidate();
        return new Result(true,"退出登录成功");
    }

}
