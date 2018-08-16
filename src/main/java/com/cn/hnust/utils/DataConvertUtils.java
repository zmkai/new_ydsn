package com.cn.hnust.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cn.hnust.pojo.Score;

public class DataConvertUtils {
	public static List<Map<String, Object>> convertList(List<String> types,List<Score> scores){
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i = 0;i<types.size();i++) {
			Map<String, Object> zhongJianMap = new HashMap<String, Object>();
			String type = types.get(i);
			zhongJianMap.put("name", type);
			List<Score> oneTypeScore = new ArrayList<Score>();
			//遍历结果集合，并将其分类
			for (Score score : scores) {
				if(score.getName().contains(type)) {
					oneTypeScore.add(score);
					//scores.remove(score);
				}
			}
			zhongJianMap.put("data", oneTypeScore);
			resultList.add(zhongJianMap);
		}
		return resultList;
	}
}
