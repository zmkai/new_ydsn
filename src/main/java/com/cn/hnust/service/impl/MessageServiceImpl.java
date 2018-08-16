package com.cn.hnust.service.impl;  
  
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;  
  
import org.springframework.stereotype.Service;

import com.cn.hnust.dao.IMessageDao;
import com.cn.hnust.dao.IUserDao;
import com.cn.hnust.pojo.Message;
import com.cn.hnust.pojo.User;
import com.cn.hnust.service.IMessageService;
import com.cn.hnust.service.IUserService;
 
@Service("messageService")  
public class MessageServiceImpl implements IMessageService {  
    @Resource  
    private IMessageDao messageDao;

	public List<Message> queryUserMessage(String i) {
		return messageDao.queryUserMessage(i);
	}

	public boolean insertUserMessage(Map<String, Object> params) {
		boolean flog = false;
		try {
			messageDao.insertUserMessage(params);
			flog = true;
		} catch (Exception e) {
			flog = false;
			e.printStackTrace();
		}
		return flog;
	}


	
    
     
  
} 