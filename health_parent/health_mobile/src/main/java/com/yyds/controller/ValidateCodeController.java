package com.yyds.controller;

import com.javamenc.constant.MessageConstant;
import com.javamenc.constant.RedisConstant;
import com.javamenc.entity.Result;

import com.javamenc.utils.Mail;
import com.javamenc.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        try {
//            + 生成验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
// //            + 调用短信发送的工具类发送验证码
//             SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());

            //由于手机短信太贵，暂时换成邮箱验证码
            Mail.sendMail(telephone,validateCode.toString());
//            + 发送成功之后，将对应手机的验证码存到redis中==【3分钟】==
            jedisPool.getResource().setex(telephone + "-" + RedisConstant.SENDTYPE_ORDER, 3 * 60, validateCode.toString());

            //            + 返回响应
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        try {
//            2. 调用工具类生成验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);

// //            3. 调用阿里云的工具类发送验证码
//             SMSUtils.sendShortMessage(SMSUtils.QUICK_LOGIN_VALIDATE_CODE, telephone, validateCode.toString());

            //由于手机短信太贵，暂时换成邮箱验证码
            Mail.sendMail(telephone,validateCode.toString());




//            4. 将发送的验证码按照手机号+验证码类型存到redis【3分钟】
            jedisPool.getResource().setex(telephone + "-" + RedisConstant.SENDTYPE_LOGIN, 3 * 60, validateCode.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
