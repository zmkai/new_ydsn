package com.cn.hnust.controller;  
  

import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.hnust.service.IDataService;
import com.cn.hnust.service.IVersionService;
import com.cn.hnust.utils.AESUtils;
import com.cn.hnust.utils.HttpUtils;
import com.cn.hnust.utils.JsonUtils;
  
@Controller  
public class VersionController {  
    @Resource  
    private IVersionService versionService;
    @Resource
    private IDataService dataService;
      
    @RequestMapping(value = "/upgrade.do",method = RequestMethod.GET)  
    public void upgrade(HttpServletRequest request,HttpServletResponse response) throws Exception{  
       Map<String, Object> reqparams = HttpUtils.RequestToMap(request);
       String result = null;
       String miyao = dataService.getMiYao("00000000");
       if(miyao == null) {
    	   miyao = "1234567812345678";
       }
       if(reqparams==null) {
    	   result = JsonUtils.JsonResponse(1, "缺少参数", null);
       }else {
    	   String version = (String) reqparams.get("version");
    	   //System.out.println(version);
    	   //versionService.isAccess(version);
    	   result = JsonUtils.JsonResponse(0, "返回数据成功", versionService.getLastVersionInfo(version));
       }
       result = URLEncoder.encode(AESUtils.getInstance().encrypt(result, miyao),"ASCII");
       response.setContentType("text/plain;charset=utf-8");
       response.getWriter().write(result);
        //model.addAttribute("user", version);  
        //return "showUser";  
    }  
}  