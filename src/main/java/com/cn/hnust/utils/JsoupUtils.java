package com.cn.hnust.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Method;

public class JsoupUtils {
	public static boolean connectJWC(String account,String password) throws IOException {
		String url = "http://210.47.163.27:9001/loginAction.do";
		boolean flog = false;
		Map<String, String> params = new HashMap<String, String>();
		params.put("zjh", account);
		params.put("mm", password);
		Document document = Jsoup.connect(url).data(params).method(Method.POST).get();
		Elements elementsByTag = document.getElementsByTag("title");
		String html = elementsByTag.get(0).html();
		if(html.contains("学分制综合教务")) {
			flog = true;
		}else {
			flog = false;
		}
		return flog;
	}
}
