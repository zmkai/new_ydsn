package com.cn.hnust.service;  
  
import java.util.Map;

import com.cn.hnust.pojo.Version;  
  
public interface IVersionService {
	//��ѯ���µİ汾
    public Version queryLastVersion(); 
    //У��汾
    public Map<String, String> getLastVersionInfo(String version);
    //�жϰ汾��ȡ���ݵ�Ȩ��
    public boolean isAccess(String version);
    
}  