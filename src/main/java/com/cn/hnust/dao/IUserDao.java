package com.cn.hnust.dao;

import java.util.Map;

import com.cn.hnust.pojo.User;

public interface IUserDao {
   public User queryUserinfo(String account);
   //�ύ�û�����
   public void insertUserReflect(Map<String, Object> refParamms);
   //�����û�������
   public String queryUserByPassword(String account);
   //�����û�������
   public void updataUserPassword(Map<String,String> params);
}