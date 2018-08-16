package com.cn.hnust.service;  
  
import java.util.Map;

import com.cn.hnust.pojo.Version;  
  
public interface IVersionService {
	//查询最新的版本
    public Version queryLastVersion(); 
    //校验版本
    public Map<String, String> getLastVersionInfo(String version);
    //判断版本获取数据的权限
    public boolean isAccess(String version);
    
}  