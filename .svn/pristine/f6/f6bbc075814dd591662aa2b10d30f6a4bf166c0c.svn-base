package com.eyunda.tools;

import java.io.UnsupportedEncodingException;

/**
 * 用于转码操作，以实现简单加密。涉及中文问题，请用 encode 与 decode 进行加密与解密。
 */
public class Base64 {

	private static final String s_keys = "n2xeDEMb5kTvKSfy9XYZa-m81Jijsz_wR6OcCIogtu4ABFGVpPl3UqrdHWQ7hN0L";

	public Base64() {
	}

	/**
	 * 把字符串转换成GBK编码的字节加密，以解决汉中文问题。
	 */
	public static String encode(String unencoded) throws Exception {
		return encrypt(getISOString(unencoded));
	}

	/**
	 * 与 encode（）对应，解码字符串
	 */
	public static String decode(String encoded) throws Exception {
		return getGBKString(decrypt(encoded));
	}

	/**
	 * base64 加密码方法
	 */
	public static String encrypt(String str) throws Exception {
		if (str == null)
			return str;

		int len = str.length();
		if (len == 0)
			return str;

		StringBuffer pTmp = new StringBuffer(len);

		pTmp = pTmp.append(str);

		StringBuffer dest = new StringBuffer();
		for (int i = 0; i < len; i++) {
			char ch = pTmp.charAt(i);
			int idx1 = ch >> 2 & 0x3f;
			int idx2 = ch << 4 & 0x30;
			dest.append(s_keys.charAt(idx1));
			if (++i == len) {
				dest.append(s_keys.charAt(idx2));
				break;
			}
			ch = pTmp.charAt(i);
			idx1 = idx2 | ch >> 4 & 0xf;
			idx2 = ch << 2 & 0x3f;
			dest.append(s_keys.charAt(idx1));
			if (++i == len) {
				dest.append(s_keys.charAt(idx2));
				break;
			}
			ch = pTmp.charAt(i);
			idx1 = idx2 | ch >> 6 & 0x3;
			idx2 = ch & 0x3f;
			dest.append(s_keys.charAt(idx1));
			dest.append(s_keys.charAt(idx2));
		}

		return dest.toString();
	}

	/**
	 * base64解密方法
	 */
	public static String decrypt(String str) throws Exception {
		if (str == null)
			throw new Exception("NULL pointer.");
		int len = str.length();
		if (len == 0)
			return str;

		StringBuffer dest = new StringBuffer();
		for (int j = 0; j < len; j++) {
			char ch = str.charAt(j);
			int i;
			for (i = 0; i < 64; i++)
				if (s_keys.charAt(i) == ch)
					break;

			char tempDest = (char) (i << 2);
			if (++j == len) {
				dest.append(tempDest);
				break;
			}
			ch = str.charAt(j);
			for (i = 0; i < 64; i++)
				if (s_keys.charAt(i) == ch)
					break;

			dest.append(tempDest |= i >> 4);
			int temp = (i & 0xf) << 4;
			if (++j == len)
				break;
			ch = str.charAt(j);
			for (i = 0; i < 64; i++)
				if (s_keys.charAt(i) == ch)
					break;

			dest.append((char) (temp | i >> 2));
			temp = (i & 0x3) << 6;
			if (++j == len)
				break;
			ch = str.charAt(j);
			for (i = 0; i < 64; i++)
				if (s_keys.charAt(i) == ch)
					break;

			dest.append((char) (temp | i));
		}

		return dest.toString();

	}

	/**
	 * 以下两个方法用于中文转码
	 */
	private static String getISOString(String l_S_Source) throws UnsupportedEncodingException {
		String l_S_Desc = "";
		if (l_S_Source != null && !l_S_Source.trim().equals("")) {
			byte l_b_Proc[] = l_S_Source.getBytes("GBK");
			l_S_Desc = new String(l_b_Proc, "ISO8859_1");
		}
		return l_S_Desc;
	}

	private static String getGBKString(String l_S_Source) throws UnsupportedEncodingException {
		String l_S_Desc = "";
		if (l_S_Source != null && !l_S_Source.trim().equals("")) {
			byte l_b_Proc[] = l_S_Source.getBytes("ISO8859_1");
			l_S_Desc = new String(l_b_Proc, "GBK");
		}
		return l_S_Desc;
	}

	/**
	 * 用于javascript的中文转码（utf-8 和 utf-16 互转）
	 */
	private static String utf8to16(String str) {
		int len = str.length();
		int c, char2, char3;
		String out = "";
		for (int i = 0; i < len;) {
			c = str.charAt(i++);
			switch (c >> 4) {

			// 0xxxxxxx
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				out += str.charAt(i - 1);
				break;

			// 110x xxxx 10xx xxxx
			case 12:
			case 13:
				char2 = str.charAt(i++);
				out += (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
				break;

			// 1110 xxxx 10xx xxxx 10xx xxxx
			case 14:
				char2 = str.charAt(i++);
				char3 = str.charAt(i++);
				out += (char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
				break;
			}
		}
		return out;
	}

	private static String utf16to8(String str) {
		int len, c;
		String out = "";
		len = str.length();
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				out += str.charAt(i);
			} else if (c > 0x07FF) {
				out += (char) (0xE0 | ((c >> 12) & 0x0F));
				out += (char) (0x80 | ((c >> 6) & 0x3F));
				out += (char) (0x80 | ((c >> 0) & 0x3F));
			} else {
				out += (char) (0xC0 | ((c >> 6) & 0x1F));
				out += (char) (0x80 | ((c >> 0) & 0x3F));
			}
		}
		return out;
	}

	/**
	 * 对应 base64.js 文件的 decode(str) 函数
	 */
	public static String encodeForJS(String str) throws Exception {
		return encrypt(utf16to8(str));
	}

	/**
	 * 对应 base64.js 文件的 encode(str) 函数
	 */
	public static String decodeForJS(String str) throws Exception {
		return utf8to16(decrypt(str));
	}

	public static void main(String args[]) throws Exception {

		System.out.println("ORIGIN:uewqe5fdsfsdfcdxczxc43534gfd");
		String e = encode("uewqe5fdsfsdfcdxczxc43534gfd");
		System.out.println("ENCRYPT: " + e);
		String d = decode("B8mkA8-bt3YEtUdjt3djCzdPCzcMcE-EdzB3tF");
		System.out.println("DECRYPT: " + d);
		
		String ssss = "abcdef";
		System.out.println(ssss);
		System.out.println(ssss.split(""));
	}

}
