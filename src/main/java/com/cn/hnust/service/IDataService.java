package com.cn.hnust.service;  
  
import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.ChaJian;
import com.cn.hnust.pojo.CourseInfo;
import com.cn.hnust.pojo.KaoBiao;
import com.cn.hnust.pojo.Score;

  
public interface IDataService {  
	//��ѯ�˺��������
	public boolean queryUserLoginMark(String account);
	//�������ļ�¼
	public boolean insertMiWen(Map<String, String> params);
	//��ѯѧ���γ���Ϣ
	public List<CourseInfo> queryStudentCourses(String account);
	//��ѯ�û���¼�ĵ�¼��ʶ
	public boolean isUserLogin(String i);
	//��ѯ�����������Ϣ(�û����˺ź�����)
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