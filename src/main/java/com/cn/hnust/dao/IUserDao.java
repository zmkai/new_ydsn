package com.cn.hnust.dao;

import java.util.Map;

import com.cn.hnust.pojo.User;

public interface IUserDao {
   public User queryUserinfo(String account);
   //提交用户反馈
   public void insertUserReflect(Map<String, Object> refParamms);
   //查找用户的密码
   public String queryUserByPassword(String account);
   //更新用户的密码
   public void updataUserPassword(Map<String,String> params);
}