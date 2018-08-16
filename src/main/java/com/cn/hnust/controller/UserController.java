package com.cn.hnust.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.deser.ValueInstantiators.Base;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.hnust.pojo.User;
import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.IUserService;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.BiaoShiUtils;
import com.cn.hnust.utils.DataJiaoYanUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;
import com.cn.hnust.utils.JsoupUtils;

@Controller
public class UserController {
	@Resource
	private IUserService userService;
	@Resource
	private IDataService dataService;

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("用户登录接口");
		String result = "";
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		System.out.println("请求参数的集合为:"+reqparams);
		String init_miyao = "";
		init_miyao = dataService.getMiYao("00000000");
		if(init_miyao==null) {
			init_miyao = "1234567812345678";
		}
		if (reqparams != null) {
			String account = (String) reqparams.get("account");
			String password = (String) reqparams.get("password");
			if (account == null) {
				result = JsonUtils.JsonResponse(1, "缺少账号参数", null);
			}
			if (password == null) {
				result = JsonUtils.JsonResponse(1, "缺少密码参数", null);
			}
			if (JsoupUtils.connectJWC(account, password)) {
				System.out.println("用户可以登录教务处");
				// 查找数据库中是否已经存在记录，若存在则不再重新生成
				if (!dataService.queryUserLoginMark(account)) {
					// 不存在，将用户登录标记插入数据库
					String loginflog = BiaoShiUtils.MD5();
					String miyao = BiaoShiUtils.MD5();
					Map<String, String> params = new HashMap<String, String>();
					params.put("account", account);
					params.put("loginflog", loginflog);
					params.put("miyao", miyao);
					dataService.insertMiWen(params);
					System.out.println("用户登录");
					//调用python的爬虫脚本
					String path = request.getServletContext().getRealPath("/");
					//String cmd = "python3 "+path+"pachong.py "+ account+" "+password;
					//String cmd = "python C:\\Users\\Administrator\\Desktop\\pachong.py "+ account+" "+password;
					String cmd = "python3 /root/pachong.py "+ account+" "+password;
					if(!DataJiaoYanUtils.panDingPaChong(cmd)) {
						System.out.println("未能成功爬取");
						result = JsonUtils.JsonResponse(1, "数据爬取异常", null);
						System.out.println(result);
						result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
						response.setContentType("text/plain;charset=utf8");
						response.getWriter().write(result);
						return;
					}
					System.out.println("开始调用爬虫接口");
				}
				//将用户名和账号更新到数据库，为了防止用户改密码
				String mypassword = userService.queryUserByPassword(account).replaceAll("\r|\n","");
				String zhuanma = Base64.encodeBase64String(password.getBytes("UTF-8"));
				if(mypassword!=null) {
					//执行用户的账号密码的更新
					System.out.println("密码比对结果："+mypassword.equals(zhuanma));
					if(!mypassword.equals(zhuanma)) {
						System.out.println("更新用户密码到数据库");
						//更新用户密码
						Map<String, String> params = new HashMap<String, String>();
						params.put("account", account);
						params.put("password",zhuanma);
						if(!userService.updataUserPassword(params)) {
							System.out.println("数据库写入密码失败");
							result = JsonUtils.JsonResponse(1, "写入失败", null);
							System.out.println(result);
							result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, init_miyao),"ASCII");
							response.setContentType("text/plain;charset=utf8");
							response.getWriter().write(result);
							return;
						}
					}
				}
				System.out.println("查找用户信息");
				User user = userService.queryUserinfo(account);
				result = JsonUtils.JsonResponse(0, "登录成功", user);
			} else {
				System.out.println("用户出现密码错误");
				result = JsonUtils.JsonResponse(1, "账号或者密码错误", null);
			}
			System.out.println("数据返回");
			System.out.println(result);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, init_miyao),"ASCII");
			// 解决后台传入前端中文乱码问题
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}

	}
	//提交反馈，这里还是有中文乱码问题
	@RequestMapping(value = "/commitView.do", method = RequestMethod.POST)
	public void commitView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		String miyao = dataService.getMiYao("00000000");
		if(miyao == null) {
			miyao = "1234567812345678";
		}
		Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
		if (reqparams == null) {
			result = JsonUtils.JsonResponse(1, "缺少参数", null);
			System.out.println(result);
			result =URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
			response.setContentType("text/plain;charset=utf8");
			response.getWriter().write(result);
		}
		//DataJiaoYanUtils.exitEmpty(reqparams, response, result);
		if (userService.insertUserReflect(reqparams)) {
			result = JsonUtils.JsonResponse(0, "提交成功", null);
			//成功用自己的密钥加密
			miyao = dataService.getMiYao((String)reqparams.get("i"));
		} else {
			result = JsonUtils.JsonResponse(1, "提交失败", null);
		}
		System.out.println(result);
		result = AESUtils.getInstance().encrypt(result, miyao);
		result = URLEncoder.encode(result,"ASCII");
		// 解决后台传入前端中文乱码问题
		response.setContentType("text/plain;charset=utf8");
		response.getWriter().write(result);

	}
}