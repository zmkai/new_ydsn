package com.cn.hnust.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.hnust.pojo.ChaJian;
import com.cn.hnust.pojo.CourseInfo;
import com.cn.hnust.pojo.KaoBiao;
import com.cn.hnust.pojo.Score;
import com.cn.hnust.pojo.User;
import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.IUserService;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.BiaoShiUtils;
import com.cn.hnust.utils.DataConvertUtils;
import com.cn.hnust.utils.DataJiaoYanUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;
import com.cn.hnust.utils.JsoupUtils;

@Controller
public class DataController {
	@Resource
	private IDataService dataService;

	@RequestMapping(value = "/getCourseInfo.do", method = RequestMethod.POST)
	public void getCourseInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		String miyao = dataService.getMiYao("00000000");
		if (miyao == null) {
			miyao = "1234567812345678";
		}
		if (reqparams.get("f")==null||reqparams.get("i")==null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
			return ;
		}
		Integer f = (Integer) reqparams.get("f");
		String i = (String) reqparams.get("i");
		// �ж��Ƿ���Ҫ������ȡ����
		if (f == 1) {
			// ��panChongInfo������洢��account��password�����ֶ�
			Map<String, Object> panChongInfo = dataService.queryPanChongXinXin(i);
			String account = (String) panChongInfo.get("account");
			//System.out.println(panChongInfo.get("password"));
			String cmd = "python C:\\Users\\Administrator\\Desktop\\pachong.py "+ account;
			//String path = request.getServletContext().getRealPath("/");
			//String cmd = "python3 "+path+"pachong.py "+ account;
			//String cmd = "python3 /root/pachong.py "+ account;
			//System.out.println("���л���Ϊ��"+request.getContextPath());
			//System.out.println("��ȡweb��Ŀ��ȫ·��"+request.getServletContext().getRealPath("/"));
			//System.out.println("classesĿ¼��ȫ·��"+this.getClass().getClassLoader().getResource("/").getPath());
//			File file=new File("."); 
//			String path=file.getAbsolutePath();
//			System.out.println("��ǰĿ¼��·��"+path);
//			path=file.getPath();
//			System.out.println("��ǰĿ¼��·��1"+path);
			if(!DataJiaoYanUtils.panDingPaChong(cmd)) {
				System.out.println("δ�ܳɹ���ȡ");
				result = JsonUtils.JsonResponse(1, "������ȡ�쳣", null);
				System.out.println(result);
				result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
				response.setContentType("text/plain;charset=utf8");
				response.getWriter().write(result);
				return;
			}
			System.out.println("���������ȡ�ӿ�,��Ҫ�˺ź�����");

		}
		// ���ڸõ�¼״̬
		List<CourseInfo> courseInfos = dataService.queryStudentCourses(i);

		result = JsonUtils.JsonResponse(0, "�������ݳɹ�", courseInfos);
		miyao = dataService.getMiYao((String) reqparams.get("i"));
		System.out.println(result);
		result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}

	@RequestMapping(value = "/getKaoBiao.do", method = RequestMethod.POST)
	public void getKaoBiao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		String miyao = dataService.getMiYao("00000000");
		if (miyao == null) {
			miyao = "1234567812345678";
		}
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if (reqparams == null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			System.out.println(result);
			result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
			response.setContentType("text/plain;charset=utf8");

			response.getWriter().write(result);
		}
		String i = (String) reqparams.get("i");

		if (dataService.isUserLogin(i)) {
			// ���ڸõ�¼״̬
			List<KaoBiao> kaoBiaos = dataService.queryStudentTest(i);
			result = JsonUtils.JsonResponse(0, "�������ݳɹ�", kaoBiaos);
			miyao = dataService.getMiYao((String) reqparams.get("i"));
		} else {
			// �����ڸõ�¼״̬
			result = JsonUtils.JsonResponse(1, "��û�е�¼", null);
		}
		System.out.println(result);
		result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}

	@RequestMapping(value = "/getScore.do", method = RequestMethod.POST)
	public void getScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		String miyao = dataService.getMiYao("00000000");
		if (miyao == null) {
			miyao = "1234567812345678";
		}
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if (reqparams == null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		String i = (String) reqparams.get("i");
		if (dataService.isUserLogin(i)) {
			// ���ڸõ�¼״̬
			List<String> scoreType = dataService.queryUserGradeType(i);
			List<Score> scores = dataService.queryStudentGrade(i);
			List<Map<String, Object>> resultList = DataConvertUtils.convertList(scoreType, scores);
			result = JsonUtils.JsonResponse(0, "�������ݳɹ�", resultList);
			miyao = dataService.getMiYao((String) reqparams.get("i"));
		} else {
			// �����ڸõ�¼״̬
			result = JsonUtils.JsonResponse(1, "��û�е�¼", null);
		}
		System.out.println(result);
		result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}

	@RequestMapping(value = "/getChaJianList.do", method = RequestMethod.GET)
	public void getChaJianList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("��ʼ��ȡ���");
		String miyao = dataService.getMiYao("00000000");
		if (miyao == null) {
			miyao = "1234567812345678";
		}
		String result = "";
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if (reqparams == null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		List<ChaJian> resultList = dataService.getChaJianList();
		result = JsonUtils.JsonResponse(0, "�������ݳɹ�", resultList);
		System.out.println(result);
		result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}

	@RequestMapping(value = "/openingdate.do", method = RequestMethod.GET)
	public void openingdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		String miyao = dataService.getMiYao("00000000");
		if (miyao == null) {
			miyao = "1234567812345678";
		}
		String result = "";
		String openDate = dataService.getOpenDate();
		resultMap.put("date", openDate);
		result = JsonUtils.JsonResponse(0, "�������ݳɹ�", resultMap);
		System.out.println(result);
		result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}
}