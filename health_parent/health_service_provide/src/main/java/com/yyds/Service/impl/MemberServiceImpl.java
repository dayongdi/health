package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.javamenc.pojo.Member;
import com.javamenc.utils.DateUtils;
import com.yyds.Service.MemberService;
import com.yyds.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Transactional
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {

        List<Integer> integerList=new ArrayList<>();

        if (months !=null){
            for (String month:months) {
                month+="-31";
                try {
                    Date date = DateUtils.parseString2Date(month);
                    Integer count = memberDao.selectMemberCountByDateBefore(date);
                    integerList.add(count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return integerList;
    }

    @Override
    public Member findByTel(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }


}
