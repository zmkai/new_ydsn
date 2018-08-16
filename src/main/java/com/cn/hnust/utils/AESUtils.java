package com.cn.hnust.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.sun.mail.util.BASE64EncoderStream;

/**
 * 
 * 
 * @author: �չ���
 * 
 * @date: 2018��6��1�� ����3:28:40
 *
 * @Description: TODO AES�ӽ��ܹ�����
 *
 */
public class AESUtils {

	/**
	 * AES���ܷ���
	 * 
	 * @param sSrc
	 *            Ҫ���ܵ��ַ���
	 * @param sKey
	 *            ������Կ
	 * @return ���ܺ���ַ��� ����ʧ�ܷ���null
	 */
	/*
	 * public static String Encrypt(String sSrc, String sKey) { if (sKey == null ||
	 * sKey.length() != 16) { return null; } try { byte[] raw =
	 * sKey.getBytes("utf-8"); SecretKeySpec skeySpec = new SecretKeySpec(raw,
	 * "AES"); Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//
	 * "�㷨/ģʽ/���뷽ʽ" cipher.init(Cipher.ENCRYPT_MODE, skeySpec); byte[] encrypted =
	 * cipher.doFinal(sSrc.getBytes("utf-8")); return new
	 * Base64().encodeToString(encrypted);// �˴�ʹ��BASE64��ת�빦�ܣ�ͬʱ����2�μ��ܵ����� } catch
	 * (Exception e) { return null; } }
	 */

	/**
	 * AES���ܷ���
	 * 
	 * @param sSrc
	 *            Ҫ���ܵ�����
	 * @param sKey
	 *            ������Կ
	 * @return ���ܺ������ ����ʧ�ܷ���null
	 */
	/*
	 * public static String Decrypt(String sSrc, String sKey) { if (sKey == null ||
	 * sKey.length() != 16) { return null; } try { byte[] raw =
	 * sKey.getBytes("utf-8"); SecretKeySpec skeySpec = new SecretKeySpec(raw,
	 * "AES"); Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	 * cipher.init(Cipher.DECRYPT_MODE, skeySpec); byte[] encrypted1 = new
	 * Base64().decode(sSrc);// ����base64���� byte[] original =
	 * cipher.doFinal(encrypted1); String originalString = new String(original,
	 * "utf-8"); return originalString; } catch (Exception e) { return null; } }
	 */
	// public static void main(String[] args) throws Exception {
	/*
	 * �˴�ʹ��AES-128-ECB����ģʽ��key����ҪΪ16λ
	 */
	// //String cKey = "1234567890123456";
	// // Ҫ���ܵ��ִ�
	// String cSrc =
	// "{account:\"195140040\",password:\"195140040\",version:\"3.1.0\"}";
	// System.out.println(cSrc);
	// // ����
	// String enString = AESUtils.Encrypt(cSrc, cKey);
	// System.out.println("���ܺ���ִ��ǣ�" + enString);
	//
	// // ����
	// String DeString = AESUtils.Decrypt(enString, cKey);
	// System.out.println("���ܺ���ִ��ǣ�" + DeString);
	// }

	/*
	 * �����õ�Key ������26����ĸ��������� �˴�ʹ��AES-128-CBC����ģʽ��key��ҪΪ16λ��
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

	// ����
	public String encrypt(String sSrc, String sKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// ʹ��CBCģʽ����Ҫһ������iv�������Ӽ����㷨��ǿ��
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return new Base64().encodeToString(encrypted);
		// return new BASE64Encoder().encode(encrypted);//�˴�ʹ��BASE64��ת�롣
	}

	// ����
	public String decrypt(String sSrc, String sKey) throws Exception {
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			// byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//����base64����
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
		//��Ҫ���ܵ��ַ���
		//String miwen = "c/3+SLug9+wHHly08cy0gA==";
		String miwen = "YaQJEcLipP4mRVKAVZK719lqpCtZFRqem1kl/J2Vra8=";
		// ��Ҫ���ܵ��ִ�
		String cSrc = "{version:'3.10'}";
		System.out.println(cSrc);
		// ����
		long lStart = System.currentTimeMillis();
		String enString = AESUtils.getInstance().encrypt(cSrc, sKey);
		System.out.println("���ܺ���ִ��ǣ�" + enString);

		long lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("���ܺ�ʱ��" + lUseTime + "����");
		// ����
		lStart = System.currentTimeMillis();
		String DeString = AESUtils.getInstance().decrypt(enString, sKey);
		System.out.println("���ܺ���ִ��ǣ�" + DeString);
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("���ܺ�ʱ��" + lUseTime + "����");
		miwen = "hc3BUYGcmJVUn/NXEro18FP13k0kMjhbob+V6ur4fSwP6iDJ+/Ow2QZns5RAw3COE1HZute7Yd6WKa8i99DZ4g==";
		System.out.println("�����ܵ��ַ����Ľ��ܺ��ַ���:"+AESUtils.getInstance().decrypt(miwen, "71aa70cd8f447368"));
	}
}
