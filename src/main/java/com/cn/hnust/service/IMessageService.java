package com.cn.hnust.service;  
  
import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.Message;
  
public interface IMessageService {  
	//查询消息
	public List<Message> queryUserMessage(String i);
	//插入消息
	public boolean insertUserMessage(Map<String, Object> params);
}  