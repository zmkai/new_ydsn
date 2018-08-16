package com.cn.hnust.utils;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @discription json宸ュ叿绫�
 * @author 鍛ㄦⅵ鍑�
 * @date 2018/4/2
 */
public class JsonUtils {
	public static void main(String[] args) {
		
	}
	
	//灏嗙粨鏋滆浆鎹㈡垚json鏍煎紡鐨勫瓧绗︿覆
	public static String JsonResponse(int code,String msg,Object data) {
		JSONObject object = new JSONObject();
		object.put("code", code);
		object.put("msg", msg);
		if(data!=null) {
			//鍒ゆ柇data鏄惁涓簃ap绫诲瀷锛屽鏋滄槸鍒欐寜鐓ap鐨勬柟寮忚繘琛宩son灏佽
			if(Map.class.isInstance(data)) {
				object.put("data", data);
			//鍒ゆ柇data鏄惁涓簂ist绫诲瀷锛屽鏋滄槸鎸夌収list鐨勬柟寮忚繘琛宩son灏佽
			}else if(List.class.isInstance(data)) {
				JSONArray array = JSONArray.fromObject(data);
				object.put("data", array);
			}else {
				//鍏朵粬鏍煎紡锛屾瘮濡傚崟涓璞�
				object.put("data", data);
			}
		}else {
			//濡傛灉data涓簄ull锛屽垯涓嶅瓨鍦╠ata瀛楁
			object.put("data", null);
		}
		//System.out.println(object);
		return object.toString();		
	}
}
