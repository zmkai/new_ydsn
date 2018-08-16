package com.cn.hnust.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

public class DataJiaoYanUtils {
	//校验请求参数，如果里面的值为空，则返回前端提示信息
	public static void exitEmpty(Map<String, Object> reqParam,HttpServletResponse response,String result) throws IOException {
		Set<String> keySet = reqParam.keySet();
		for (String key : keySet) {
			if(reqParam.get(key)==null) {
				result = JsonUtils.JsonResponse(1, "缺少"+key+"参数", null);
				response.setContentType("text/plain;charset=utf8");
				response.getWriter().write(result);
			}
		}
	}
	//判断爬虫是否完成
	public static boolean  panDingPaChong(String cmd) throws IOException {
		boolean flog = false;
		Process process = Runtime.getRuntime().exec(cmd);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		
		String line;
		while ((line = input.readLine()) != null) {
			System.out.println(line);
			if(line.equals("1")) {
				System.out.println("结尾");
				flog = true;
				break;
			}else if(line.equals("0")) {
				//发生异常
				System.out.println("异常");
				flog = false;
				break;
			}
		}
		return flog;
	}
	
}
