package com.cn.hnust.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

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
import com.cn.hnust.service.impl.VersionServiceImpl;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;

/**
 * Servlet Filter implementation class LoginFilter
 */
public class LoginFilter implements Filter {
	private IDataService dataService;
    
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	
	public void destroy() {
		// TODO Auto-generated method stub
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		//System.out.println("LoginFilter"+httpServletRequest.getRequestURL().toString());
		
		String url = httpServletRequest.getRequestURL().toString();
		String jiekou = url.substring(url.lastIndexOf("/")+1);
		//System.out.println(jiekou);
		if("login.do".equals(jiekou)||"upgrade.do".equals(jiekou)||"openingdate.do".equals(jiekou)||"getChaJianList.do".equals(jiekou)) {
			//System.out.println("放行"+url.toString());
			chain.doFilter(httpServletRequest, httpServletResponse);
			return ;
		}
		String result = "";
		String miyao = dataService.getMiYao("00000000");
		if(miyao==null) {
			miyao = "1234567812345678";
		}
		
		Map<String, Object> reqparams = HttpUtils.RequestToMap(httpServletRequest);
		String i = (String) reqparams.get("i");
		//System.out.println("在登录过滤器中的解密后的登录数据为:"+i);
		if(dataService.isUserLogin(i)) {
			System.out.println("用户成功登录");
			chain.doFilter(request, response);
		}else {
			System.out.println("登录失败");
			result = JsonUtils.JsonResponse(1, "用户登录异常", null);
			try {
				result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			httpServletResponse.setContentType("text/plain;charset=utf8");
			httpServletResponse.getWriter().write(result);
			return ;
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		ServletContext context = fConfig.getServletContext();
		XmlWebApplicationContext webApplicationContext = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(context);
		 if(webApplicationContext != null && webApplicationContext.getBean("dataService") != null && dataService == null) {
			 dataService =(DataServiceImpl)webApplicationContext.getBean("dataService");
		 }
	}
}
