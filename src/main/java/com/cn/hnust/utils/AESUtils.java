package com.cn.hnust.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.sun.mail.util.BASE64EncoderStream;

/**
 * 
 * 
 * @author: 苏国启
 * 
 * @date: 2018年6月1日 下午3:28:40
 *
 * @Description: TODO AES加解密工具类
 *
 */
public class AESUtils {

	/**
	 * AES加密方法
	 * 
	 * @param sSrc
	 *            要加密的字符串
	 * @param sKey
	 *            加密秘钥
	 * @return 加密后的字符串 加密失败返回null
	 */
	/*
	 * public static String Encrypt(String sSrc, String sKey) { if (sKey == null ||
	 * sKey.length() != 16) { return null; } try { byte[] raw =
	 * sKey.getBytes("utf-8"); SecretKeySpec skeySpec = new SecretKeySpec(raw,
	 * "AES"); Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//
	 * "算法/模式/补码方式" cipher.init(Cipher.ENCRYPT_MODE, skeySpec); byte[] encrypted =
	 * cipher.doFinal(sSrc.getBytes("utf-8")); return new
	 * Base64().encodeToString(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用 } catch
	 * (Exception e) { return null; } }
	 */

	/**
	 * AES解密方法
	 * 
	 * @param sSrc
	 *            要解密的密文
	 * @param sKey
	 *            解密秘钥
	 * @return 解密后的明文 解密失败返回null
	 */
	/*
	 * public static String Decrypt(String sSrc, String sKey) { if (sKey == null ||
	 * sKey.length() != 16) { return null; } try { byte[] raw =
	 * sKey.getBytes("utf-8"); SecretKeySpec skeySpec = new SecretKeySpec(raw,
	 * "AES"); Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	 * cipher.init(Cipher.DECRYPT_MODE, skeySpec); byte[] encrypted1 = new
	 * Base64().decode(sSrc);// 先用base64解密 byte[] original =
	 * cipher.doFinal(encrypted1); String originalString = new String(original,
	 * "utf-8"); return originalString; } catch (Exception e) { return null; } }
	 */
	// public static void main(String[] args) throws Exception {
	/*
	 * 此处使用AES-128-ECB加密模式，key长度要为16位
	 */
	// //String cKey = "1234567890123456";
	// // 要加密的字串
	// String cSrc =
	// "{account:\"195140040\",password:\"195140040\",version:\"3.1.0\"}";
	// System.out.println(cSrc);
	// // 加密
	// String enString = AESUtils.Encrypt(cSrc, cKey);
	// System.out.println("加密后的字串是：" + enString);
	//
	// // 解密
	// String DeString = AESUtils.Decrypt(enString, cKey);
	// System.out.println("解密后的字串是：" + DeString);
	// }

	/*
	 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private String sKey = "1234567812345678";
	private String ivParameter = "1234567812345678";
	private static AESUtils instance = null;

	private AESUtils() {

	}

	public static AESUtils getInstance() {
		if (instance == null)
			instance = new AESUtils();
		return instance;
	}

	// 加密
	public String encrypt(String sSrc, String sKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return new Base64().encodeToString(encrypted);
		// return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
	}

	// 解密
	public String decrypt(String sSrc, String sKey) throws Exception {
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			// byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
			byte[] encrypted1 = new Base64().decode(sSrc);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		String sKey = "1234567812345678";
		//需要解密的字符串
		//String miwen = "c/3+SLug9+wHHly08cy0gA==";
		String miwen = "YaQJEcLipP4mRVKAVZK719lqpCtZFRqem1kl/J2Vra8=";
		// 需要加密的字串
		String cSrc = "{version:'3.10'}";
		System.out.println(cSrc);
		// 加密
		long lStart = System.currentTimeMillis();
		String enString = AESUtils.getInstance().encrypt(cSrc, sKey);
		System.out.println("加密后的字串是：" + enString);

		long lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("加密耗时：" + lUseTime + "毫秒");
		// 解密
		lStart = System.currentTimeMillis();
		String DeString = AESUtils.getInstance().decrypt(enString, sKey);
		System.out.println("解密后的字串是：" + DeString);
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("解密耗时：" + lUseTime + "毫秒");
		miwen = "hc3BUYGcmJVUn/NXEro18FP13k0kMjhbob+V6ur4fSwP6iDJ+/Ow2QZns5RAw3COE1HZute7Yd6WKa8i99DZ4g==";
		System.out.println("待解密的字符串的解密后字符串:"+AESUtils.getInstance().decrypt(miwen, "71aa70cd8f447368"));
	}
}
