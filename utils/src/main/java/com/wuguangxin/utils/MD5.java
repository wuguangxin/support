package com.wuguangxin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 * 例：加密 123456  ===  e10adc3949ba59abbe56e057f20f883e
 */
public class MD5 {

	private static String key = "";

	public static void setKey(String key) {
		MD5.key = key;
	}

	/**
	 * 加密（如果text为 null 则转换为 ""）
	 * 示例：
	 * MD5.encode("") = d41d8cd98f00b204e9800998ecf8427e
	 * MD5.encode(null) = d41d8cd98f00b204e9800998ecf8427e
	 * @param text 文本
	 * @return 密文
	 */
	public static String encode(String text) {
		if (text == null) text = "";
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			text += key;
			byte[] result = digester.digest(text.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				int number = b & 0xff & 0xff;
				String hexStr = Integer.toHexString(number);
				if (hexStr.length() == 1) {
					sb.append("0");
				}
				sb.append(hexStr);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
