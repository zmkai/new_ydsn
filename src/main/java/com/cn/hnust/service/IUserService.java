package com.cn.hnust.service;  
  
import java.util.Map;

import com.cn.hnust.pojo.User;  
  
public interface IUserService {  
    public User queryUserinfo(String account); 
    
    //提交用户反馈
    public boolean insertUserReflect(Map<String, Object> refParamms);
    //查找用户的密码
    public String queryUserByPassword(String account);
    //更新用户的密码
    public boolean updataUserPassword(Map<String,String> params);
}  