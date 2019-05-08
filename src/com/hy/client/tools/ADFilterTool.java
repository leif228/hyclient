package com.hy.client.tools;

import java.util.regex.Pattern;

import com.eyunda.third.GlobalApplication;
import com.hy.client.R;

import android.text.TextUtils;

public class ADFilterTool {
	/**
	 * 正则表达式
	 */
	private static String PATTERN = "";

	static {
		initPattern();
	}

	/**
	 * 初始化pattern
	 */
	private static void initPattern() {
		PATTERN = getPatternStr();
	}

	/**
	 * 判断url的域名是否合法
	 * <p>
	 * 域名是否合法：自己项目中使用的域名，为合法域名；其它域名皆为不合法域名，进行屏蔽
	 *
	 * @param url
	 * @return
	 */
	public static boolean hasNotAd(String url) {
		if (TextUtils.isEmpty(url)) {
			return false;
		}
		if (TextUtils.isEmpty(PATTERN)) {
			initPattern();
		}
		if (Pattern.matches(PATTERN, url)) {
			return true;
		}
		return false;
	}

	/**
	 * 拼接正则表达式
	 *
	 * @return
	 */
	private static String getPatternStr() {
		String[] adUrls = GlobalApplication.getInstance().getResources()
				.getStringArray(R.array.legal_domain);
		if (null != adUrls && adUrls.length > 0) {
			StringBuffer sb = new StringBuffer("^(https|http)://.*(");
			for (String a : adUrls) {
				if (null != a && a.length() > 0) {
					sb.append(a).append("|");
				}
			}
			return sb.substring(0, sb.length() - 1) + ").*";
		}
		return null;
	}
}
