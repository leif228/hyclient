package com.eyunda.tools;

import java.text.NumberFormat;

public class NumberFormatUtil {
	private static String defaultValue = "0.00";
	private static NumberFormat nf = null;

	static {
		nf = NumberFormat.getInstance();
		// 设置数的小数部分所允许的最大位数、最小位数，系统默认为三位小数
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
	}

	public static Integer toInt(double number) {
		if (Double.isNaN(number) || Double.isInfinite(number))
			return 0;
		else
			return new Integer((int) number);
	}

	public static double format(double number) {
		if (Double.isNaN(number) || Double.isInfinite(number))
			return Double.parseDouble(defaultValue);
		else
			return Double.parseDouble(nf.format(number).replace(",", ""));
	}

	public static void main(String[] args) {
		double d = 3423423233.1415926D;
		float f = 2.71828F;
		int i = 123456;

		System.out.println(NumberFormatUtil.format(d));
		System.out.println(NumberFormatUtil.format(f));
		System.out.println(NumberFormatUtil.format(i));
	}
}
