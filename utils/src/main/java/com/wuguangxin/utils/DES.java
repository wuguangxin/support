package com.wuguangxin.utils;

import android.text.TextUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DEX加密解密工具类。
 * （在程序初始化时设置秘钥 setKey()，否则会使用默认的秘钥）
 * Created by wuguangxin on 16/10/11
 */
public class DES {
	private static final byte decodeMap[] = initDecodeMap();
	private static final char encodeMap[] = initEncodeMap();
	/** 秘钥 */
	private static String KEY;

	/**
	 * 设置秘钥
	 * @param encryption
	 */
	public static void setKey(String encryption) {
		DES.KEY = encryption;
	}

	private static String getEncryption() {
		if(TextUtils.isEmpty(KEY)){
			throw new NullPointerException("主要参数ENCRYPTION为null，" +
					"请在使用DES类前调用DES.setKey()设置你的encryption，比如在Application.onCreate()里调用。");
		}
		return KEY;
	}

	protected static char encode(int i) {
		return encodeMap[i & 0x3f];
	}

	private static char[] initEncodeMap() {
		char map[] = new char[64];
		for (int i = 0; i < 26; i++)
			map[i] = (char) (65 + i);

		for (int i = 26; i < 52; i++)
			map[i] = (char) (97 + (i - 26));

		for (int i = 52; i < 62; i++)
			map[i] = (char) (48 + (i - 52));

		map[62] = '+';
		map[63] = '/';
		return map;
	}

	private static int calcLength(char buf[]) {
		int len = buf.length;
		int base64count = 0;
		int paddingCount = 0;
		int i;
		for (i = 0; i < len; i++) {
			if (buf[i] == '=')
				break;
			if (buf[i] >= '\u0100')
				return -1;
			if (decodeMap[buf[i]] != -1)
				base64count++;
		}

		for (; i < len; i++) {
			if (buf[i] == '=') {
				paddingCount++;
			} else {
				if (buf[i] >= '\u0100')
					return -1;
				if (decodeMap[buf[i]] != -1)
					return -1;
			}
		}

		if (paddingCount > 2)
			return -1;
		if ((base64count + paddingCount) % 4 != 0)
			return -1;
		else {
			return ((base64count + paddingCount) / 4) * 3 - paddingCount;
		}
	}

	private static byte[] initDecodeMap() {
		byte map[] = new byte[256];
		for (int i = 0; i < 256; i++)
			map[i] = -1;

		for (int i = 65; i <= 90; i++)
			map[i] = (byte) (i - 65);

		for (int i = 97; i <= 122; i++)
			map[i] = (byte) ((i - 97) + 26);

		for (int i = 48; i <= 57; i++)
			map[i] = (byte) ((i - 48) + 52);

		map[43] = 62;
		map[47] = 63;
		map[61] = 127;
		return map;
	}

	public static String encodeBase64Binrary(byte input[]) {
		StringBuffer r = new StringBuffer((input.length * 4) / 3);

		for (int i = 0; i < input.length; i += 3) {
			switch ((input.length - i)) {
			case 1: // '\001'
				r.append(encode(input[i] >> 2));
				r.append(encode((input[i] & 0x3) << 4));
				r.append("==");
				break;

			case 2: // '\002'
				r.append(encode(input[i] >> 2));
				r.append(encode((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xf));
				r.append(encode((input[i + 1] & 0xf) << 2));
				r.append("=");
				break;

			default:
				r.append(encode(input[i] >> 2));
				r.append(encode((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xf));
				r.append(encode((input[i + 1] & 0xf) << 2 | input[i + 2] >> 6 & 0x3));
				r.append(encode(input[i + 2] & 0x3f));
			}
		}

		return r.toString();
	}

	public static byte[] decodeBase64Binrary(String lexicalValue) {
		char buf[] = lexicalValue.toCharArray();
		int outlen = calcLength(buf);
		if (outlen == -1)
			return null;
		byte out[] = new byte[outlen];
		int o = 0;
		int len = buf.length;
		byte quadruplet[] = new byte[4];
		int q = 0;
		for (int i = 0; i < len; i++) {
			byte v = decodeMap[buf[i]];
			if (v != -1)
				quadruplet[q++] = v;
			if (q == 4) {
				out[o++] = (byte) (quadruplet[0] << 2 | quadruplet[1] >> 4);

				if (quadruplet[2] != 127) {
					out[o++] = (byte) (quadruplet[1] << 4 | quadruplet[2] >> 2);
				}

				if (quadruplet[3] != 127) {
					out[o++] = (byte) (quadruplet[2] << 6 | quadruplet[3]);
				}

				q = 0;
			}
		}

		if (q != 0)
			throw new IllegalStateException();
		else
			return out;
	}

	public static byte[] desDecrypt(SecretKey key, byte[] crypt) throws Exception {
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede");
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(crypt);
	}

	public static byte[] desEncrypt(SecretKey key, byte[] src) throws Exception {
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede");
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(src);
	}

	public static SecretKey genDESKey(byte[] key_byte) {
		SecretKey k = null;
		k = new SecretKeySpec(key_byte, "DESEDE");
		return k;
	}

	/**
	 * 加密 （如果text为 null 则转换为 ""）<br>
	 * 示例：
	 * DES.encrypt("") = /VZHDHrzpFU=
	 * DES.encrypt(null) = /VZHDHrzpFU=
	 * @param text 要加密的文本
	 * @param key 加密的秘钥
	 * @return 返回密文
	 */
	public static String encrypt(String text, String key) {
		if(key == null || key == ""){
			return null;
		}
		if (text == null) text = "";
		try {
			SecretKey deskey = genDESKey(key.getBytes());
			byte[] encrypt = desEncrypt(deskey, text.getBytes());
			return encodeBase64Binrary(encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解密 （如果text为 null 则转换为 ""）
	 * 示例：
	 * DES.decrypt("") = null
	 * DES.decrypt(null) = null
	 * @param text 要加密的文本
	 * @param key 加密的秘钥
	 * @return 返回密文
	 */
	public static String decrypt(String text, String key) {
		if(key == null || key == ""){
			return null;
		}
		if (text == null) text = "";
		try {
			byte[] decrypt = decodeBase64Binrary(text);
			SecretKey deskey = genDESKey(key.getBytes());
			byte[] decrByte = desDecrypt(deskey, decrypt);
			return new String(decrByte, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换戄1�7进制
	 * @param buf byte
	 * @return 字符串
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 射1�7进制转换为二进制
	 * @param hexStr String
	 * @return byte[]
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}


	// ============== 扩展方法 ==================================================

	/**
	 * 加密数据。
	 * （在程序初始化时设置秘钥 setKey()，否则会使用默认的秘钥）
	 * @param data 数据
	 * @return 密文
	 */
	public static String encode(String data) {
		return encrypt(data, getEncryption());
	}

	/**
	 * 解密数据。
	 * （在程序初始化时设置秘钥 setKey()，否则会使用默认的秘钥）
	 * @param data 数据
	 * @return 明文
	 */
	public static String decode(String data) {
		return decrypt(data, getEncryption());
	}

}
