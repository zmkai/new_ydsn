package com.cn.hnust.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.deser.ValueInstantiators.Base;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.hnust.pojo.User;
import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.IUserService;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.BiaoShiUtils;
import com.cn.hnust.utils.DataJiaoYanUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;
import com.cn.hnust.utils.JsoupUtils;

@Controller
public class UserController {
	@Resource
	private IUserService userService;
	@Resource
	private IDataService dataService;

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("�û���¼�ӿ�");
		String result = "";
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		System.out.println("��������ļ���Ϊ:"+reqparams);
		String init_miyao = "";
		init_miyao = dataService.getMiYao("00000000");
		if(init_miyao==null) {
			init_miyao = "1234567812345678";
		}
		if (reqparams != null) {
			String account = (String) reqparams.get("account");
			String password = (String) reqparams.get("password");
			if (account == null) {
				result = JsonUtils.JsonResponse(1, "ȱ���˺Ų���", null);
			}
			if (password == null) {
				result = JsonUtils.JsonResponse(1, "ȱ���������", null);
			}
			if (JsoupUtils.connectJWC(account, password)) {
				System.out.println("�û����Ե�¼����");
				// �������ݿ����Ƿ��Ѿ����ڼ�¼��������������������
				if (!dataService.queryUserLoginMark(account)) {
					// �����ڣ����û���¼��ǲ������ݿ�
					String loginflog = BiaoShiUtils.MD5();
					String miyao = BiaoShiUtils.MD5();
					Map<String, String> params = new HashMap<String, String>();
					params.put("account", account);
					params.put("loginflog", loginflog);
					params.put("miyao", miyao);
					dataService.insertMiWen(params);
					System.out.println("�û���¼");
					//����python������ű�
					String path = request.getServletContext().getRealPath("/");
					//String cmd = "python3 "+path+"pachong.py "+ account+" "+password;
					//String cmd = "python C:\\Users\\Administrator\\Desktop\\pachong.py "+ account+" "+password;
					String cmd = "python3 /root/pachong.py "+ account+" "+password;
					if(!DataJiaoYanUtils.panDingPaChong(cmd)) {
						System.out.println("δ�ܳɹ���ȡ");
						result = JsonUtils.JsonResponse(1, "������ȡ�쳣", null);
						System.out.println(result);
						result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
						response.setContentType("text/plain;charset=utf8");
						response.getWriter().write(result);
						return;
					}
					System.out.println("��ʼ��������ӿ�");
				}
				//���û������˺Ÿ��µ����ݿ⣬Ϊ�˷�ֹ�û�������
				String mypassword = userService.queryUserByPassword(account).replaceAll("\r|\n","");
				String zhuanma = Base64.encodeBase64String(password.getBytes("UTF-8"));
				if(mypassword!=null) {
					//ִ���û����˺�����ĸ���
					System.out.println("����ȶԽ����"+mypassword.equals(zhuanma));
					if(!mypassword.equals(zhuanma)) {
						System.out.println("�����û����뵽���ݿ�");
						//�����û�����
						Map<String, String> params = new HashMap<String, String>();
						params.put("account", account);
						params.put("password",zhuanma);
						if(!userService.updataUserPassword(params)) {
							System.out.println("���ݿ�д������ʧ��");
							result = JsonUtils.JsonResponse(1, "д��ʧ��", null);
							System.out.println(result);
							result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, init_miyao),"ASCII");
							response.setContentType("text/plain;charset=utf8");
							response.getWriter().write(result);
							return;
						}
					}
				}
				System.out.println("�����û���Ϣ");
				User user = userService.queryUserinfo(account);
				result = JsonUtils.JsonResponse(0, "��¼�ɹ�", user);
			} else {
				System.out.println("�û������������");
				result = JsonUtils.JsonResponse(1, "�˺Ż����������", null);
			}
			System.out.println("���ݷ���");
			System.out.println(result);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, init_miyao),"ASCII");
			// �����̨����ǰ��������������
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}

	}
	//�ύ���������ﻹ����������������
	@RequestMapping(value = "/commitView.do", method = RequestMethod.POST)
	public void commitView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		String miyao = dataService.getMiYao("00000000");
		if(miyao == null) {
			miyao = "1234567812345678";
		}
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if (reqparams == null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			System.out.println(result);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		//DataJiaoYanUtils.exitEmpty(reqparams, response, result);
		if (userService.insertUserReflect(reqparams)) {
			result = JsonUtils.JsonResponse(0, "�ύ�ɹ�", null);
			//�ɹ����Լ�����Կ����
			miyao = dataService.getMiYao((String)reqparams.get("i"));
		} else {
			result = JsonUtils.JsonResponse(1, "�ύʧ��", null);
		}
		System.out.println(result);
		result = AESUtils.getInstance().encrypt(result, miyao);
		result = URLEncoder.encode(result,"ASCII");
		// �����̨����ǰ��������������
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);

	}
}