package com.cn.hnust.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class HttpUtils {
//	public static Map<String,Object> RequestToMap(HttpServletRequest request){
//		System.out.println("request的编码："+request.getContentType());
//		Map<String, Object> reqParams = new HashMap<String, Object>();
//		if("GET".equals(request.getMethod())){
//			//获取get请求参数数据
//			String paramString = request.getQueryString().trim();
//			if(paramString==null) {
//				return null;
//			}
//			//解析数据
//			String params[] = paramString.split("&");
//			for (String string : params) {
//				String key = string.split("=")[0];
//				String value = string.split("=")[1];
//				reqParams.put(key, value);
//			}
//		}else if("POST".equals(request.getMethod())){
//			//获取post请求参数
//			Enumeration<String> keys = request.getParameterNames();
//			//解析post请求参数
//			while(keys.hasMoreElements()) {
//				String key = keys.nextElement();
//				//System.out.println(key);
//				String value = request.getParameter(key);
////				System.out.println(value);
//				reqParams.put(key, value);
//			}
//		}
//		return reqParams;
//	}
	
	public static JSONObject dataToRequest(String mingwen) {
//		String params = "{\r\n" + 
//				"    account: '195140040',\r\n" + 
//				"    password: '1937915896',\r\n" + 
//				"    version: '3.1.0'\r\n" + 
//				"}";
//		System.out.println(params.indexOf("{"));
//		System.err.println(params.lastIndexOf("}"));
//		String params2 = params.substring(params.indexOf("{")+1, params.lastIndexOf("}")).trim();
//		String[] split = params2.split(",");
//		System.out.println(split.length);
//		for(int i = 0;i<split.length;i++) {
//			String name = split[i].split(":")[0];
//			String value = split[i].split(":")[1];
//			System.out.println(name+value);
//		}
		JSONObject object = null;
		if(mingwen.startsWith("{")&&mingwen.endsWith("}")) {
			//ͨ����json��ʽ���ݽ����ж�,Ȼ��תΪjson��ʽ������
			object = JSONObject.fromObject(mingwen);
			//System.out.println(object);
		}
		return object;
		
	}
	public static Map<String,Object> RequestToMap(HttpServletRequest request){
		Map<String, Object> reqParams = (Map<String, Object>) request.getAttribute("reqParam");
		return reqParams;
	}
	public static void main(String[] args) {
		dataToRequest(null);
	}
}
