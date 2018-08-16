package com.cn.hnust.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.impl.DataServiceImpl;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;

import net.sf.json.JSONObject;

public class JiaMiFilter implements Filter {
	private IDataService dataService;

    public JiaMiFilter() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		System.out.println("JIaMi"+httpServletRequest.getRequestURL().toString());
//		if("options".equals(httpServletRequest.getMethod().toLowerCase())) {
//			System.out.println("请求为options");
//			httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
//			httpServletResponse.setHeader("Access-Control-Allow-Headers", "accept,content-type"); 
//			httpServletResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT"); 
//			httpServletResponse.getWriter().write("12");
//			return ;
//			//chain.doFilter(httpServletRequest, httpServletResponse);
//		}
		
		//解决跨域问题，第二个参数为另外一个域
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "accept,content-type"); 
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT"); 
		Map<String, Object> reqParamsMap = new HashMap<String, Object>();
		Map<String, Object> getParams = new HashMap<String, Object>();
		String miWenData = "";
		//选择密钥
//		String url = httpServletRequest.getRequestURL().toString();
//		String jiekou = url.substring(url.lastIndexOf("/")+1);
//		System.out.println(jiekou);
//		if("login.do".equals(jiekou)||"upgrade.do".equals(jiekou)||"openingdate.do".equals(jiekou)) {
//			chain.doFilter(httpServletRequest, httpServletResponse);
//		}
		
		String mingWen = "";
		String loginMiWen = null;
		//获取公共密文，一边解密带有登录标记的密文
		String miyao = dataService.getMiYao("00000000");
		String miyao2 = miyao;
		if(miyao==null) {
			miyao = "1234567812345678";
		}
		//System.out.println("密钥为:"+miyao);
		if("post".equals(httpServletRequest.getMethod().toLowerCase())) {
			System.out.println("post请求");
			//获取到的密文数据,post方法中不需要进行URL解码
			miWenData = httpServletRequest.getParameter("d");
			if(httpServletRequest.getParameter("i")!=null) {
				loginMiWen = httpServletRequest.getParameter("i");
			}
			//System.out.println("post方法中未通过URL解码时的密文数据为"+miWenData);
			miWenData = URLDecoder.decode(miWenData,"ASCII");
			//System.out.println("post方法中通过URL解码时的密文数据为"+miWenData);
		
//			
		}else if("get".equals(httpServletRequest.getMethod().toLowerCase())) {
			System.out.println("get请求"); 
			//解析get请求参数,get请求需要解码
			String params = httpServletRequest.getQueryString();
			if(params.contains("&")) {
				String[] split = params.split("&");
				for(int i = 0 ;i<split.length;i++) {
					getParams.put(split[i].split("=")[0], split[i].split("=")[1]);
				}
				//将登录标记密文获得
				loginMiWen = (String)getParams.get("i");
			}else {
				getParams.put(params.split("=")[0], params.split("=")[1]);
			}
			//获得参数密文
			miWenData = (String) getParams.get("d");
			//System.out.println("get方法中未通过URL解码时的密文数据为"+miWenData);
			miWenData = URLDecoder.decode(miWenData,"ASCII");
			miWenData=URLDecoder.decode(miWenData,"ASCII");
			//System.out.println("get方法中通过URL解码时的密文数据为"+miWenData);
			//System.out.println("URL解码后的请求数据的密文数据为:"+miWenData);
		}
		//解i,为了获得用户的密钥
		try {
			//System.out.println("登录标记的密文数据为："+loginMiWen);
			if(loginMiWen!=null) {
				String i = AESUtils.getInstance().decrypt(URLDecoder.decode(loginMiWen,"ASCII"), miyao);
				//存在登录标记的时候获取请求参数解密的密钥
				miyao = dataService.getMiYao(i);
				//System.out.println("用户自己的密钥为"+miyao);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			//将密文解密
			mingWen = AESUtils.getInstance().decrypt(miWenData,miyao);
			if(mingWen==null) {
				String result = "";
				try {
					result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao2),"ASCII");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				httpServletResponse.setContentType("text/plain;charset=utf8");
				httpServletResponse.getWriter().write(result);
				return;
			}
			System.out.println("解密后的字符串为:"+mingWen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject params = HttpUtils.dataToRequest(mingWen);
		System.out.println(params);
		if(params==null) {
			String result = JsonUtils.JsonResponse(1, "参数格式不对", null);
			try {
				result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao2),"ASCII");
			} catch (Exception e) {
				e.printStackTrace();
			}
			httpServletResponse.setContentType("text/plain;charset=utf8");
			httpServletResponse.getWriter().write(result);
		}
		reqParamsMap.putAll(params);
		
		System.out.println(reqParamsMap);
		httpServletRequest.setAttribute("reqParam", reqParamsMap);
		chain.doFilter(httpServletRequest, httpServletResponse);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		ServletContext context = fConfig.getServletContext();
		XmlWebApplicationContext webApplicationContext = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(context);
		 if(webApplicationContext != null && webApplicationContext.getBean("dataService") != null && dataService == null) {
			 dataService =(DataServiceImpl)webApplicationContext.getBean("dataService");
		 }
	}

}
