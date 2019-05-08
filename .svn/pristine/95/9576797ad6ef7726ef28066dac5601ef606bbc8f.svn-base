package com.eyunda.third.adapters.chat.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;

public class CalendarUtil {
	private static String[] parsePatterns;
	static {
		parsePatterns = new String[] { "yyyy/MM/dd", "yyyy-MM-dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm",
				"yyyy-MM-dd HH:mm:ss", "YYYY-MM-DD hh:mm:ss", "yyyy/MM/dd HH:mm", "yyyy-MM" };
	}

	public static Calendar parse(String string) {
		Calendar result = Calendar.getInstance();
		try {
			result.setTime(DateUtils.parseDate(string, parsePatterns));
			return result;
		} catch (ParseException e) {
			System.out.println("------------");
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将一个 年年年年月月日日 的字符串转换为日历类
	 * 
	 * @param parameterValue
	 */
	public static Calendar parseYYYYMMDD(String parameterValue) {
		String format = "yyyyMMdd";
		return parse(parameterValue, format);
	}

	public static Calendar parseyyyy_MM_dd(String parameterValue) {
		String format = "yyyy-MM-dd";
		return parse(parameterValue, format);
	}

	public static Calendar parseYY_MM_DD(String parameterValue) {
		String format = "yy-MM-dd";
		return parse(parameterValue, format);
	}

	public static Calendar parse(String string, String... format) {
		Calendar result = Calendar.getInstance();
		try {
			result.setTime(DateUtils.parseDate(string, format));
			return result;
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/** 获取前N天 */
	public static Calendar getDay(int n) {
		Calendar date = getTheDayZero(now());
		date.add(Calendar.DATE, n);
		return date;
	}

	public static String toYYYY_MM(Calendar date) {
		return new SimpleDateFormat("yyyy-MM").format(date.getTime());
	}

	public static String toYYYY_MM_DD(Calendar date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
	}

	public static String toYYYY_MM_DD_HH_MM_SS(Calendar date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime());
	}

	public static String toHH_mm(Calendar date) {
		return new SimpleDateFormat("HH:mm").format(date.getTime());
	}

	public static String toHH_mm_ss(Calendar date) {
		return new SimpleDateFormat("HH:mm:ss").format(date.getTime());
	}

	public static String toYYYYMMDD(Calendar date) {
		return new SimpleDateFormat("yyyyMMdd").format(date.getTime());
	}

	public static String toYYYYMM(Calendar date) {
		return new SimpleDateFormat("yyyyMM").format(date.getTime());
	}

	public static String toYYYYMMDDHHmmss(Calendar date) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date.getTime());
	}

	public static String toYYYY(Calendar date) {
		return new SimpleDateFormat("yyyy").format(date.getTime());
	}

	public static Calendar getYesterday() {
		Calendar result = Calendar.getInstance();
		result.add(Calendar.DATE, -1);
		return result;
	}

	/**
	 * 返回此日期的零点整，如2007-3-14 19:00:35 返回 2007-3-14 00:00:00
	 * 
	 * @param date
	 */
	public static Calendar getTheDayZero(Calendar date) {
		Calendar result = (Calendar) date.clone();
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		result.set(Calendar.MILLISECOND, 0);
		return result;
	}

	/**
	 * 返回此日期加上几天后的日期
	 * 
	 * @param date
	 * @param days
	 */
	public static Calendar addDays(Calendar date, int days) {
		Calendar result = (Calendar) date.clone();

		result.add(Calendar.DATE, days);

		return result;
	}

	public static Calendar addMonths(Calendar date, int months) {
		Calendar result = (Calendar) date.clone();

		result.add(Calendar.MONTH, months);

		return result;
	}

	/**
	 * 返回此日期的午夜，如2007-3-14 19:00:35 返回 2007-3-14 23:59:59
	 * 
	 * @param date
	 */
	public static Calendar getTheDayMidnight(Calendar date) {
		Calendar result = (Calendar) date.clone();
		result.set(Calendar.HOUR_OF_DAY, 23);
		result.set(Calendar.MINUTE, 59);
		result.set(Calendar.SECOND, 59);
		result.set(Calendar.MILLISECOND, 999);
		return result;
	}

	/**
	 * 返回此日期的设定时间，如2007-3-14 19:00:35 传入date , 4, 59,59,999 返回 2007-3-14
	 * 04:59:59
	 * 
	 * @param date
	 */
	public static Calendar getTheDayByCondition(Calendar date, int hour, int minute, int second, int milisecond) {
		Calendar result = (Calendar) date.clone();
		result.set(Calendar.HOUR_OF_DAY, hour);
		result.set(Calendar.MINUTE, minute);
		result.set(Calendar.SECOND, second);
		result.set(Calendar.MILLISECOND, milisecond);
		return result;
	}

	public static int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Calendar calendar) {
		return calendar.get(Calendar.HOUR);
	}

	public static int getMinute(Calendar calendar) {
		return calendar.get(Calendar.MINUTE);
	}

	public static int getSecond(Calendar calendar) {
		return calendar.get(Calendar.SECOND);
	}

	public static Calendar getOnYmdhms(int year, int month, int day, int hour, int minute, int second) {
		Calendar result = Calendar.getInstance();

		result.set(Calendar.YEAR, year);
		result.set(Calendar.MONTH, month - 1);
		result.set(Calendar.DAY_OF_MONTH, day);
		result.set(Calendar.HOUR_OF_DAY, hour);
		result.set(Calendar.MINUTE, minute);
		result.set(Calendar.SECOND, second);
		result.set(Calendar.MILLISECOND, 0);

		return result;
	}

	public static int compare(Calendar c1, Calendar c2) {
		if (c1.before(c2))
			return -1;
		else if (c1.after(c2))
			return 1;
		else
			return 0;
	}

	public static Calendar now() {
		return Calendar.getInstance();
	}

	public static String toTimeStampFm(Calendar calendar) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}

	public static String toTimeStampFm2(Calendar calendar) {
		if (calendar == null)
			return "";
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(calendar.getTime());
	}

	public static Calendar toCalnedar(Date date) {
		Calendar result = now();
		result.setTime(date);
		return result;
	}

	// 按日期获取本周日期 format:"yyyy-MM-dd" //
	public static List<String> getWeekByCalendar(String format, Calendar calendar) {
		Date mdate = calendar.getTime();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day--;
		if (day == 0)
			day = 7;
		Date fdate;
		List<String> list = new ArrayList<String>();
		Long fTime = mdate.getTime() - day * 24 * 3600000;
		for (int i = 1; i < 5; i++) {
			fdate = new Date();
			fdate.setTime(fTime + (i * 24 * 3600000));
			list.add(new SimpleDateFormat(format).format(fdate));
		}
		return list;
	}

	// 按日期获取后7天日期 format:"yyyy-MM-dd" //
	public static List<String> get7DayByCalendar(String format, Calendar calendar) {
		Date mdate = calendar.getTime();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day--;
		if (day == 0)
			day = 7;
		Date fdate;
		List<String> list = new ArrayList<String>();
		Long fTime = mdate.getTime();
		for (int i = 0; i < 7; i++) {
			fdate = new Date();
			fdate.setTime(fTime + (i * 24 * 3600000));
			list.add(new SimpleDateFormat(format).format(fdate));
		}
		return list;
	}

	// 判断两日期之间的天数差
	public static int dayDiff(Calendar calendar1, Calendar calendar2) {
		int days = 0;
		Calendar c1, c2;
		if (calendar1.before(calendar2)) {
			c1 = (Calendar) calendar1.clone();
			c2 = (Calendar) calendar2.clone();
		} else {
			c2 = (Calendar) calendar1.clone();
			c1 = (Calendar) calendar2.clone();
		}
		while (c1.before(c2)) {
			days++;
			c1.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;
	}

	// 判断两日期之间的天数差
	public static int monthDiff(Calendar month1, Calendar month2) {
		int months = 0;

		Calendar m1 = (Calendar) month1.clone();
		Calendar m2 = (Calendar) month2.clone();

		if (m1.getTimeInMillis() == m2.getTimeInMillis()) {
			months = 0;
		} else if (month1.before(month2)) {
			while (m1.getTimeInMillis() < m2.getTimeInMillis()) {// before是<=
				months++;
				m1.add(Calendar.MONTH, 1);
			}
		} else if (m1.getTimeInMillis() > m2.getTimeInMillis()) {
			while (m1.after(m2)) {// before是<=
				months--;
				m1.add(Calendar.MONTH, -1);
			}
		}

		return months;
	}

	public static void main(String[] args) {
		int year = 2013, month = 12, day = 1, hour = 0, minute = 0, second = 0;

		Calendar c = CalendarUtil.getOnYmdhms(year, month, day, hour, minute, second);

		System.out.println(CalendarUtil.toYYYYMMDDHHmmss(c));
		System.out.println(CalendarUtil.toYYYYMM(c));
		System.out.println(CalendarUtil.getMonth(c));
	}

}
