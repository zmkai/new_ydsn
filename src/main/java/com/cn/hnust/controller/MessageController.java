package com.cn.hnust.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.hnust.pojo.Message;
import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.IMessageService;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;

@Controller
public class MessageController {
	@Resource
	private IMessageService messageService;
	@Resource
	private IDataService dataService;
	@RequestMapping(value = "/lookMessage.do", method = RequestMethod.POST)
	public void lookMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		String miyao = dataService.getMiYao("00000000");
		if(miyao ==null) {
			miyao = "1234567812345678";
		}
		
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if(reqparams==null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		String i = (String) reqparams.get("i");
		if(dataService.isUserLogin(i)) {
			//���ڸõ�¼״̬
			List<Message> resultList = messageService.queryUserMessage(i);
			result = JsonUtils.JsonResponse(0, "�������ݳɹ�", resultList);
			//��ȡ�û�˽����Կ
			miyao = dataService.getMiYao((String)reqparams.get("i"));
		}else {
			//�����ڸõ�¼״̬
			result = JsonUtils.JsonResponse(1, "��û�е�¼", null);
		}
		result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}
	
	@RequestMapping(value = "/tuiSong.do", method = RequestMethod.POST)
	//������������������,��δ���
	public void tuiSong(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String miyao = dataService.getMiYao("00000000");
		if(miyao ==null) {
			miyao = "1234567812345678";
		}
		String result = "";
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if(reqparams==null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		String i = (String) reqparams.get("i");
		if(dataService.isUserLogin(i)) {
			System.out.println("������Ϣ�е��������Ϊ:"+reqparams);
			//���ڸõ�¼״̬
			if(messageService.insertUserMessage(reqparams)) {
				//����ɹ�
				result = JsonUtils.JsonResponse(0, "��Ϣ���ͳɹ�", null);
				miyao = dataService.getMiYao((String)reqparams.get("i"));
			}else {
				result = JsonUtils.JsonResponse(1, "������Ϣʧ��", null);
			}
		}else {
			//�����ڸõ�¼״̬
			result = JsonUtils.JsonResponse(1, "��û�е�¼", null);
		}
		result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}
}