package com.cn.hnust.dao;

import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.Message;

public interface IMessageDao {
	//��ѯ��Ϣ
	public List<Message> queryUserMessage(String i);
	//������Ϣ
	public void insertUserMessage(Map<String, Object> params);
}