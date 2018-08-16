package com.cn.hnust.dao;

import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.Message;

public interface IMessageDao {
	//查询消息
	public List<Message> queryUserMessage(String i);
	//插入消息
	public void insertUserMessage(Map<String, Object> params);
}