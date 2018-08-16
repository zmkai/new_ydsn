package com.cn.hnust.service.impl;  
  
import java.util.Map;

import javax.annotation.Resource;  
  
import org.springframework.stereotype.Service;  
  
import com.cn.hnust.dao.IUserDao;
import com.cn.hnust.pojo.User;
import com.cn.hnust.service.IUserService;
 
@Service("userService")  
public class UserServiceImpl implements IUserService {  
    @Resource  
    private IUserDao userDao;

	public User queryUserinfo(String account) {
		return this.userDao.queryUserinfo(account);
	}

	public boolean insertUserReflect(Map<String, Object> refParamms) {
		boolean flog = false;
		try {
			//≤Â»Î≥…π¶
			userDao.insertUserReflect(refParamms);
			flog = true;
		}catch (Exception e) {
			flog = false;
			e.printStackTrace();
		}
		return flog;
	}

	public String queryUserByPassword(String account) {
		return userDao.queryUserByPassword(account);
	}

	public boolean updataUserPassword(Map<String, String> params) {
		boolean flog = false;
		try {
			userDao.updataUserPassword(params);
			flog = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flog;
	}  
} 