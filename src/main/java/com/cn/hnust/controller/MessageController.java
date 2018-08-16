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
			result = JsonUtils.JsonResponse(1, "缺少参数", null);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		String i = (String) reqparams.get("i");
		if(dataService.isUserLogin(i)) {
			//存在该登录状态
			List<Message> resultList = messageService.queryUserMessage(i);
			result = JsonUtils.JsonResponse(0, "返回数据成功", resultList);
			//获取用户私有密钥
			miyao = dataService.getMiYao((String)reqparams.get("i"));
		}else {
			//不存在该登录状态
			result = JsonUtils.JsonResponse(1, "您没有登录", null);
		}
		result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}
	
	@RequestMapping(value = "/tuiSong.do", method = RequestMethod.POST)
	//这里有中文乱码问题,尚未解决
	public void tuiSong(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String miyao = dataService.getMiYao("00000000");
		if(miyao ==null) {
			miyao = "1234567812345678";
		}
		String result = "";
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if(reqparams==null) {
			result = JsonUtils.JsonResponse(1, "缺少参数", null);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		String i = (String) reqparams.get("i");
		if(dataService.isUserLogin(i)) {
			System.out.println("推送消息中的请求参数为:"+reqparams);
			//存在该登录状态
			if(messageService.insertUserMessage(reqparams)) {
				//插入成功
				result = JsonUtils.JsonResponse(0, "消息推送成功", null);
				miyao = dataService.getMiYao((String)reqparams.get("i"));
			}else {
				result = JsonUtils.JsonResponse(1, "推送消息失败", null);
			}
		}else {
			//不存在该登录状态
			result = JsonUtils.JsonResponse(1, "您没有登录", null);
		}
		result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);
	}
}