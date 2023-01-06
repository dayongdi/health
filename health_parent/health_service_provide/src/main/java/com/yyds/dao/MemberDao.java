package com.yyds.dao;

import com.javamenc.pojo.Member;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MemberDao {

    //根据手机号码查找会员
    public Member findByTelephone(String telephone);
    //根据ID查询会员
    public Member findById(int id);
    //查询所有会员信息
    public List<Member> findAllMember();
    //根据条件查询
    public List<Member> findMemberByCondition();



    //增加会员
    public void add(Member member);
    //根据id删除会员
    public void deleteById(int id);
    //编辑会员信息
    public void edit(Member member);


    //根据日期统计会员数，统计指定日期之前的会员数
    public int selectMemberCountByDateBefore(Date date);
    //根据日期统计会员数，统计指定日期之后的会员数
    public int selectMemberCountByDateAfter(Date date);
    //统计日期当天注册的会员数
    public int selectMemberCountByDate(Date date);
    //统计全部会员数
    public int selectMemberCount();


    //查询指定时间内的会员注册数
    public Integer selectMemberCountBetweenDate(Map<String, Date> map);

}
