package com.yyds.Service;

import com.javamenc.pojo.Member;

import java.util.List;

public interface MemberService {

    public List<Integer> findMemberCountByMonths(List<String> lists);

    public Member findByTel(String telephone);

    public void add(Member member);
}
