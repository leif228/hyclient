package com.eyunda.third.adapters.chat.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public DateUtil() {
	}

	public final static Calendar myc = Calendar.getInstance();

	// private static SimpleDateFormat sdf = new SimpleDateFormat();

	/**
	 * 获得当天时间
	 * 
	 * @param pattern
	 *            输出的时间格式
	 * @return 返回时间
	 */
	public static String getTime(String pattern) {
		String timestr;
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMddHHmmss";
		}
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date cday = new Date();
		timestr = sdf.format(cday);
		return timestr;
	}

	/**
	 * 获得当天日期
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getDate(String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMdd";
		}
		return DateUtil.getTime(pattern);
	}

	/**
	 * 时间格式转换
	 * 
	 * @param cday
	 * @param pattern
	 * @return
	 */
	public static String getTime(Date cday, String pattern) {
		String timestr;
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMddHHmmss";
		}
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		timestr = sdf.format(cday);
		return timestr;
	}

	/**
	 * 日期格式转换
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getDate(Date date, String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMdd";
		}
		return DateUtil.getTime(date, pattern);
	}

	/**
	 * 将字串转换为指定格式的日期
	 * 
	 * @param time
	 *            时间
	 * @param pattern
	 *            为空时，将使用yyyy-MM-dd格式
	 * @return
	 */
	public static Date StrToDate(String time, String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		ParsePosition pos = new ParsePosition(0);
		Date dt1 = formatter.parse(time, pos);
		return dt1;
	}

	/**
	 * 将时间转换为parrten2格式
	 */
	public static String getTime(String t1, String pattern, String parrten2) {
		if (t1 == null || pattern.length() != t1.length()) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		SimpleDateFormat formatter2 = new SimpleDateFormat(parrten2);
		ParsePosition pos = new ParsePosition(0);
		Date dt1 = formatter.parse(t1, pos);
		return formatter2.format(dt1);
	}

	/**
	 * 比较两个字符串时间的大小
	 */
	public static int compareStringTime(String t1, String t2, String pattern) {
		if (pattern == null) {
			pattern = "yyyyMMddHHmmss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos1 = new ParsePosition(0);
		Date dt1 = formatter.parse(t1, pos);
		Date dt2 = formatter.parse(t2, pos1);

		Long quot = dt1.getTime() - dt2.getTime();
		quot = quot / 1000 / 60 / 60 / 24;

		return quot.intValue();
	}

	public static String addTime(String datetime, String pattern, long seconds) {
		if (pattern == null) {
			pattern = "yyyyMMddHHmmss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		ParsePosition pos = new ParsePosition(0);
		Date dt1 = formatter.parse(datetime, pos);
		long l = dt1.getTime() / 1000 + seconds;
		dt1.setTime(l * 1000);
		String mydate = formatter.format(dt1);

		return mydate;
	}

	/**
	 * 得到指定时间的后一分钟时间
	 */
	public static String getAfterMinuteTime(String time, String pattern) {
		String timestr = "";

		if (pattern == null || "".equals(pattern)) {
			pattern = "yyyyMMddHHmm";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		try {
			Date date = formatter.parse(time);
			Date date2 = new Date(date.getTime() + 60 * 1000);
			timestr = formatter.format(date2);
		} catch (Exception e) {

		}
		return timestr;
	}

	/**
	 * 得到指定时间的前一分钟时间
	 */
	public static String getBeforeMinuteTime(String time, String pattern) {
		String timestr = "";

		if (pattern == null) {
			pattern = "yyyyMMddHHmm";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		try {
			Date date = formatter.parse(time);
			Date date2 = new Date(date.getTime() - 60 * 1000);
			timestr = formatter.format(date2);
		} catch (Exception e) {

		}
		return timestr;
	}

	/**
	 * 得到指定日期的后一天日期
	 */
	public static String getAfterDayTime(String time, String pattern) {
		String timestr = "";
		if (pattern == null) {
			pattern = "yyyyMMdd";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		try {
			Date date = formatter.parse(time);
			Date date2 = new Date(date.getTime() + 24 * 60 * 60 * 1000);
			timestr = formatter.format(date2);
		} catch (Exception e) {

		}
		return timestr;
	}

	/**
	 * 得到指定日期的前一天日期
	 */
	public static String getBeforeDayTime(String time, String pattern) {
		String timestr = "";
		if (pattern == null) {
			pattern = "yyyyMMdd";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		try {
			Date date = formatter.parse(time);
			Date date2 = new Date(date.getTime() - 24 * 60 * 60 * 1000);
			timestr = formatter.format(date2);
		} catch (Exception e) {

		}
		return timestr;
	}

	/**
	 * 得到指定日期的多少天以前的日期
	 */
	public static String getBeforeDay(String strDate, int days, String pattern) {
		String timestr = "";
		if (pattern == null) {
			pattern = "yyyyMMdd";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		try {
			Date dtDate = formatter.parse(strDate);
			long longDate = dtDate.getTime();
			long interval = days * 24 * 60 * 60 * 1000L;
			Date date2 = new Date(longDate - interval);
			timestr = formatter.format(date2);
		} catch (Exception e) {

		}
		return timestr;
	}

	/**
	 * 得到指定月份的后一个月份
	 */
	public static String getAfterMonth(String time, String pattern) {
		String timestr = "";
		if (pattern == null) {
			pattern = "yyyyMM";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		try {
			Calendar ca = Calendar.getInstance();
			ca.setTime(formatter.parse(time));
			ca.add(Calendar.MONTH, 1);
			timestr = formatter.format(ca.getTime());
		} catch (Exception e) {

		}
		return timestr;
	}

	/**
	 * 得到指定月份的前一个月份
	 */
	public static String getBeforeMonth(String time, String pattern) {
		String timestr = "";
		if (pattern == null) {
			pattern = "yyyyMM";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		try {
			Calendar ca = Calendar.getInstance();
			ca.setTime(formatter.parse(time));
			ca.add(Calendar.MONTH, -1);
			timestr = formatter.format(ca.getTime());
		} catch (Exception e) {

		}
		return timestr;
	}

	/**
	 * 格式化日期时间为指定格式的字符串
	 */
	public static String formatDate(Date dt, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(dt);
	}

	/** 两个时间相差距离多少秒 */
	public static Long getDistanceSeconds(String str1, String str2, String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMddHHmmss";
		}

		DateFormat df = new SimpleDateFormat(pattern);

		long sec = 0;
		try {
			long time1 = df.parse(str1).getTime();
			long time2 = df.parse(str2).getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			sec = ((diff + 999) / 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sec;
	}

	/** 两个时间之间相差距离多少天 */
	public static long getDistanceDays(String str1, String str2, String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMddHHmmss";
		}

		DateFormat df = new SimpleDateFormat(pattern);

		Date one;
		Date two;
		long days = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	/** 两个时间相差距离多少天多少小时多少分多少秒 */
	public static long[] getDistanceTimes(String str1, String str2, String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMddHHmmss";
		}

		DateFormat df = new SimpleDateFormat(pattern);

		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long[] times = { day, hour, min, sec };
		return times;
	}

	/** 两个时间相差距离多少天多少小时多少分多少秒 */
	public static String getDistanceTime(String str1, String str2, String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMddHHmmss";
		}

		DateFormat df = new SimpleDateFormat(pattern);

		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}

	/**
	 * 日期字符串加天
	 * 
	 * @param src
	 *            原日期字符串
	 * @param f
	 *            格式(1:yyyyMMddHHmmss 2:yyyy.MM.dd HH:mm:ss 3:yyyy-MM-dd
	 *            HH:mm:ss" 4:yyyy年MM月dd日 HH时mm分ss秒)
	 * @param n
	 *            天数(可为负数)
	 * @return
	 */
	public static String timeStrAddDate(String src, int f, int n) {
		SimpleDateFormat sdf = null;
		if (f == 1) {
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		} else if (f == 2) {
			sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		} else if (f == 3) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (f == 4) {
			sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		} else {
			throw new RuntimeException("参数错误");
		}
		Calendar ca = Calendar.getInstance();
		try {
			ca.setTime(sdf.parse(src));
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		ca.add(Calendar.DAY_OF_MONTH, n);
		return sdf.format(ca.getTime());
	}

	/**
	 * 得到几天后的时间
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		ca.set(Calendar.DATE, ca.get(Calendar.DATE) + day);
		return ca.getTime();
	}

	/**
	 * 将一个符合特定格式（比如yyyy-MM-dd）的字符串转化成为Date类型对象
	 * 
	 * @param ymd
	 * @return
	 */
	public static Date getDateFromYmd(String ymd) {
		Date date = null;
		String[] dateDivide = ymd.split("-");
		if (dateDivide.length == 3) {
			int year = Integer.parseInt(dateDivide[0].trim());
			int month = Integer.parseInt(dateDivide[1].trim());
			int day = Integer.parseInt(dateDivide[2].trim());
			Calendar c = Calendar.getInstance();
			c.set(year, month - 1, day);
			date = c.getTime();
			return date;
		} else
			return null;
	}

	public static void main(String args[]) {
		String m = "201312";
		String n = DateUtil.getAfterMonth(m, null);
		System.out.println("the next month is " + n);
	}
}
