package com.cn.hnust.service.impl;  
  
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;  
  
import org.springframework.stereotype.Service;

import com.cn.hnust.dao.IDataDao;
import com.cn.hnust.dao.IUserDao;
import com.cn.hnust.pojo.ChaJian;
import com.cn.hnust.pojo.CourseInfo;
import com.cn.hnust.pojo.KaoBiao;
import com.cn.hnust.pojo.Score;
import com.cn.hnust.pojo.User;
import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.IUserService;
 
@Service("dataService")  
public class DataServiceImpl implements IDataService {
	@Resource
	private IDataDao dataDao;
	public boolean queryUserLoginMark(String account) {
		boolean flog = false;
		String loginflog = dataDao.queryUserLoginMark(account);
		if (loginflog!=null) {
			flog = true;
		}else {
			flog = false;
		}
		return flog;
	}
	//这里可能会有一点问题
	public boolean insertMiWen(Map<String, String> params) {
		boolean flog = false;
		try {
			dataDao.insertMiWen(params);
			flog = true;
		}catch (Exception e) {
			flog = false;
			e.printStackTrace();
		}
		return flog;
		
	}
	public List<CourseInfo> queryStudentCourses(String account) {
		return dataDao.queryStudentCourses(account);
	}
	public boolean isUserLogin(String i) {
		boolean flog = false;
		int count = dataDao.queryUserLoginFlog(i);
		if(count==1) {
			flog = true;
		}
		return flog;
	}
	public Map<String, Object> queryPanChongXinXin(String i) {
		
		return dataDao.queryPanChongXinXin(i);
	}
	public List<KaoBiao> queryStudentTest(String i) {
		return dataDao.queryStudentTest(i);
	}
	public List<String> queryUserGradeType(String i) {
		return dataDao.queryUserGradeType(i);
	}
	public List<Score> queryStudentGrade(String i) {
		return dataDao.queryStudentGrade(i);
	}
	public List<ChaJian> getChaJianList() {
		return dataDao.getChaJianList();
	}
	public String getOpenDate() {
		return dataDao.getOpenDate();
	}
	public String getMiYao(String loginFlog) {
		return dataDao.getMiYao(loginFlog);
	}  
} 