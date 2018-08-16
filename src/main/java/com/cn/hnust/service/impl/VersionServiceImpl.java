package com.cn.hnust.service.impl;  
  
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;  
  
import org.springframework.stereotype.Service;  
  
import com.cn.hnust.dao.IUserDao;
import com.cn.hnust.dao.IVersionDao;
import com.cn.hnust.pojo.User;
import com.cn.hnust.pojo.Version;
import com.cn.hnust.service.IUserService;
import com.cn.hnust.service.IVersionService;
 
@Service("versionService")  
public class VersionServiceImpl implements IVersionService {  
    @Resource  
    private IVersionDao versionDao;

	public Version queryLastVersion() {
		return versionDao.queryLastVersion();
	}

	public Map<String, String> getLastVersionInfo(String version) {
		Map<String,String> resultData = new HashMap<String, String>();
		Version lastVersion = versionDao.queryLastVersion();
		String[] serverVersion = lastVersion.getVersionNumber().split("\\.");
		String[] cilentVersion = version.split("\\.");
		if(!serverVersion[0].equals(cilentVersion[0])) {
			//��汾����
			resultData.put("type","2");
			
		}else if ((!serverVersion[1].equals(cilentVersion[1]))||(!serverVersion[2].equals(cilentVersion[2]))) {
			//С�汾���»���bug�޸�
			resultData.put("type", "1");
		}else {
			//��ǰ�汾Ϊ���°汾
			resultData.put("type", "0");
		}
		resultData.put("versionNumber", lastVersion.getVersionNumber());
		resultData.put("href", lastVersion.getHref());
		return resultData;
	}

	public boolean isAccess(String version) {
		boolean flog = false;
		String dataAccess = versionDao.dataAccess(version);
		System.out.println("dataAccess"+dataAccess);
		//0Ϊ�ð汾���Է�������
		if("0".equals(dataAccess)) {
			flog = true;
		}else if("1".equals(dataAccess)) {
			flog = false;
		}
		return flog;
	}  
    
     
  
} 