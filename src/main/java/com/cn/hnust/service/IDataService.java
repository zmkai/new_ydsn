package com.cn.hnust.service;  
  
import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.ChaJian;
import com.cn.hnust.pojo.CourseInfo;
import com.cn.hnust.pojo.KaoBiao;
import com.cn.hnust.pojo.Score;

  
public interface IDataService {  
	//查询账号相关密文
	public boolean queryUserLoginMark(String account);
	//插入密文记录
	public boolean insertMiWen(Map<String, String> params);
	//查询学生课程信息
	public List<CourseInfo> queryStudentCourses(String account);
	//查询用户登录的登录标识
	public boolean isUserLogin(String i);
	//查询用于爬虫的信息(用户的账号和密码)
 	public Map<String, Object> queryPanChongXinXin(String i);
 	//查询学生的考表信息,参数i为用户的登录标记
 	public List<KaoBiao> queryStudentTest(String i);
 	//查询成绩类型,参数i为用户的登录标记
 	public List<String> queryUserGradeType(String i);
 	//查询学生的成绩，参数i为用户的登录标记
 	public List<Score> queryStudentGrade(String i);
 	//获取插件列表
 	public List<ChaJian> getChaJianList();
 	//获取开学日期
 	public String getOpenDate();
 	//通过登录状态获得数据加密密钥
 	public String getMiYao(String loginFlog);
}  