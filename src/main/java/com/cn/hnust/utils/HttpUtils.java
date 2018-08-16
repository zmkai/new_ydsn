package com.cn.hnust.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class HttpUtils {
//	public static Map<String,Object> RequestToMap(HttpServletRequest request){
//		System.out.println("request鐨勭紪鐮侊細"+request.getContentType());
//		Map<String, Object> reqParams = new HashMap<String, Object>();
//		if("GET".equals(request.getMethod())){
//			//鑾峰彇get璇锋眰鍙傛暟鏁版嵁
//			String paramString = request.getQueryString().trim();
//			if(paramString==null) {
//				return null;
//			}
//			//瑙ｆ瀽鏁版嵁
//			String params[] = paramString.split("&");
//			for (String string : params) {
//				String key = string.split("=")[0];
//				String value = string.split("=")[1];
//				reqParams.put(key, value);
//			}
//		}else if("POST".equals(request.getMethod())){
//			//鑾峰彇post璇锋眰鍙傛暟
//			Enumeration<String> keys = request.getParameterNames();
//			//瑙ｆ瀽post璇锋眰鍙傛暟
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
			//通过对json格式数据进行判断,然后转为json格式的数据
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
