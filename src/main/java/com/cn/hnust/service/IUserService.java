package com.cn.hnust.service;  
  
import java.util.Map;

import com.cn.hnust.pojo.User;  
  
public interface IUserService {  
    public User queryUserinfo(String account); 
    
    //�ύ�û�����
    public boolean insertUserReflect(Map<String, Object> refParamms);
    //�����û�������
    public String queryUserByPassword(String account);
    //�����û�������
    public boolean updataUserPassword(Map<String,String> params);
}  