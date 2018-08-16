package com.cn.hnust.dao;

import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.ChaJian;
import com.cn.hnust.pojo.CourseInfo;
import com.cn.hnust.pojo.KaoBiao;
import com.cn.hnust.pojo.Score;


public interface IDataDao {
	//��ѯ�˺��������
	public String queryUserLoginMark(String account);
	//�������ļ�¼
	public void insertMiWen(Map<String, String> params);
	//��ѯѧ���γ���Ϣ
	public List<CourseInfo> queryStudentCourses(String account);
	//��ѯѧ���ĵ�¼״̬
	public int queryUserLoginFlog(String i);
	//��ѯ�����������Ϣ
 	public Map<String, Object> queryPanChongXinXin(String i);
 	//��ѯѧ���Ŀ�����Ϣ,����iΪ�û��ĵ�¼���
 	public List<KaoBiao> queryStudentTest(String i);
 	//��ѯ�ɼ�����,����iΪ�û��ĵ�¼���
 	public List<String> queryUserGradeType(String i);
 	//��ѯѧ���ĳɼ�������iΪ�û��ĵ�¼���
 	public List<Score> queryStudentGrade(String i);
 	//��ȡ����б�
 	public List<ChaJian> getChaJianList();
 	//��ȡ��ѧ����
 	public String getOpenDate();
 	//ͨ����¼״̬������ݼ�����Կ
 	public String getMiYao(String loginFlog);
 	
}