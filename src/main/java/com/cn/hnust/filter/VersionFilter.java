package com.cn.hnust.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.RepaintManager;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.IVersionService;
import com.cn.hnust.service.impl.DataServiceImpl;
import com.cn.hnust.service.impl.VersionServiceImpl;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;

public class VersionFilter implements Filter {
	private IVersionService versionService;
	private IDataService dataService;

	public VersionFilter() {
		// TODO Auto-generated constructor stub
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String result = "";
		HttpServletRequest request2 = (HttpServletRequest) request;
		HttpServletResponse response2 = (HttpServletResponse) response;
		//System.out.println("VersionFilter"+request2.getRequestURL().toString());
		if ("options".equals(request2.getMethod().toLowerCase())) {
			System.out.println("����Ϊoptions");
			chain.doFilter(request2, response2);
			return;
		}
		// ����������⣬�ڶ�������Ϊ����һ����
		response2.setHeader("Access-Control-Allow-Origin", "*");
		response2.setHeader("Access-Control-Allow-Headers", "accept,content-type");
		response2.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
		// ���������Ŀ������⣬�������ܲ�̫��ȫ
		// String curOrigin = request2.getHeader("Origin");
		// if(request2.getHeader("Origin")!=null) {
		// response2.setHeader("Access-Control-Allow-Origin", curOrigin);
		// }

		String version = "";
		//System.err.println("����汾������");
		// if("GET".equals(request2.getMethod().trim().toUpperCase())) {
		// System.out.println(request2.getQueryString());
		// version = request2.getQueryString();
		// }else if("POST".equals(request2.getMethod())) {
		// version = request2.getParameter("version");
		// }
		Map<String, Object> reqParams = HttpUtils.RequestToMap(request2);
		String miyao = "";
		miyao = dataService.getMiYao("00000000");
		version = (String) reqParams.get("version");
		System.out.println("�ͻ��˰汾Ϊ��"+version);
		// version = request2.getParameter("version");
		if (version == null) {
			result = JsonUtils.JsonResponse(1, "ȱ�ٲ���", null);
			try {
				// ������Ϣ
				result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("text/plain;charset=utf-8");
			response2.getWriter().write(result);
		} else {
			if (versionService.isAccess(version)) {
				// ���Բ鿴���ݣ�����
				chain.doFilter(request2, response2);
			} else {
				// ���ܷ�������
				result = JsonUtils.JsonResponse(2, "���ܷ�������", null);
				// ������Ϣ
				try {
					result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao), "ASCII");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.setContentType("text/plain;charset=utf-8");
				response2.getWriter().write(result);
			}
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		ServletContext context = fConfig.getServletContext();
		XmlWebApplicationContext webApplicationContext = (XmlWebApplicationContext) WebApplicationContextUtils
				.getWebApplicationContext(context);
		if (webApplicationContext != null && webApplicationContext.getBean("versionService") != null
				&& versionService == null)
			versionService = (VersionServiceImpl) webApplicationContext.getBean("versionService");
		if (webApplicationContext != null && webApplicationContext.getBean("dataService") != null
				&& dataService == null)
			dataService = (DataServiceImpl) webApplicationContext.getBean("dataService");
	}

}
