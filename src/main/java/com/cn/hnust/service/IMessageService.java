package com.cn.hnust.service;  
  
import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.Message;
  
public interface IMessageService {  
	//��ѯ��Ϣ
	public List<Message> queryUserMessage(String i);
	//������Ϣ
	public boolean insertUserMessage(Map<String, Object> params);
}  