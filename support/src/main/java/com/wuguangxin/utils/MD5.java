package com.wuguangxin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 * 例：加密 123456  ===  e10adc3949ba59abbe56e057f20f883e
 */
public class MD5 {

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

	/**
	 * 加密(注册用户测试用)
	 * @param text 文本
	 * @return 密文
	 */
	public static String encryptUTF(String text) {
		if (text == null) text = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("utf-8"));
            StringBuffer sb = new StringBuffer();
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i] & 0xFF;
                if (b < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
