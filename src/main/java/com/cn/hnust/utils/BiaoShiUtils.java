package com.cn.hnust.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.enterprise.inject.New;

public class BiaoShiUtils {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(MD5());
	}
	public static String MD5() {
		Date date = new Date();
		UUID uuid = UUID.randomUUID();
		String loginflog = date.getTime()+uuid.toString();
		String miwen = "";
		//进行md5加密
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(loginflog.getBytes());
			//摘要信息的长度为16位
			byte[] digest = md.digest(loginflog.getBytes());
			StringBuffer buffer = new StringBuffer();
			for(int i =0;i<digest.length;i++) {
				int j = digest[i];
				if(j<0) {
					j+=256;
				}
				if(j<16) {
					buffer.append("0");
				}
				buffer.append(Integer.toHexString(j));
			}
			miwen = buffer.substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("加密出现失败");
			e.printStackTrace();
		}
		return miwen;
	}
}
