package com.cn.hnust.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

public class DataJiaoYanUtils {
	//У�������������������ֵΪ�գ��򷵻�ǰ����ʾ��Ϣ
	public static void exitEmpty(Map<String, Object> reqParam,HttpServletResponse response,String result) throws IOException {
		Set<String> keySet = reqParam.keySet();
		for (String key : keySet) {
			if(reqParam.get(key)==null) {
				result = JsonUtils.JsonResponse(1, "ȱ��"+key+"����", null);
				response.setContentType("text/plain;charset=utf8");
				response.getWriter().write(result);
			}
		}
	}
	//�ж������Ƿ����
	public static boolean  panDingPaChong(String cmd) throws IOException {
		boolean flog = false;
		Process process = Runtime.getRuntime().exec(cmd);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		
		String line;
		while ((line = input.readLine()) != null) {
			System.out.println(line);
			if(line.equals("1")) {
				System.out.println("��β");
				flog = true;
				break;
			}else if(line.equals("0")) {
				//�����쳣
				System.out.println("�쳣");
				flog = false;
				break;
			}
		}
		return flog;
	}
	
}
