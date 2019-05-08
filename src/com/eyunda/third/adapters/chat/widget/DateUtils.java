package com.eyunda.third.adapters.chat.widget;
import java.text.*;
import java.util.*;
public class DateUtils {


    public DateUtils()
    {
    }

    public static String getTimestampString(Date date)
    {
        String s = null;
        long l = date.getTime();
        if(isSameDay(l))
        {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            int i = calendar.get(11);
            if(i > 17)
                s = "\u665A\u4E0A hh:mm";
            else
            if(i >= 0 && i <= 6)
                s = "\u51CC\u6668 hh:mm";
            else
            if(i > 11 && i <= 17)
                s = "\u4E0B\u5348 hh:mm";
            else
                s = "\u4E0A\u5348 hh:mm";
        } else
        if(isYesterday(l))
            s = "\u6628\u5929 HH:mm";
        else
            s = "M\u6708d\u65E5 HH:mm";
        return (new SimpleDateFormat(s, Locale.CHINA)).format(date);
    }

    public static boolean isCloseEnough(long l, long l1)
    {
        long l2 = l - l1;
        if(l2 < 0L)
            l2 = -l2;
        return l2 < 30000L;
    }

    private static boolean isSameDay(long l)
    {
        TimeInfo timeinfo = getTodayStartAndEndTime();
        return l > timeinfo.getStartTime() && l < timeinfo.getEndTime();
    }

    private static boolean isYesterday(long l)
    {
        TimeInfo timeinfo = getYesterdayStartAndEndTime();
        return l > timeinfo.getStartTime() && l < timeinfo.getEndTime();
    }

    public static Date StringToDate(String s, String s1)
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
        Date date = null;
        try
        {
            date = simpledateformat.parse(s);
        }
        catch(ParseException parseexception)
        {
            parseexception.printStackTrace();
        }
        return date;
    }

    public static String toTime(int i)
    {
        i /= 1000;
        int j = i / 60;
        boolean flag = false;
        if(j >= 60)
        {
            int k = j / 60;
            j %= 60;
        }
        int l = i % 60;
        return String.format("%02d:%02d", new Object[] {
            Integer.valueOf(j), Integer.valueOf(l)
        });
    }

    public static String toTimeBySecond(int i)
    {
        int j = i / 60;
        boolean flag = false;
        if(j >= 60)
        {
            int k = j / 60;
            j %= 60;
        }
        int l = i % 60;
        return String.format("%02d:%02d", new Object[] {
            Integer.valueOf(j), Integer.valueOf(l)
        });
    }

    public static TimeInfo getYesterdayStartAndEndTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date date = calendar.getTime();
        long l = date.getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(5, -1);
        calendar1.set(11, 23);
        calendar1.set(12, 59);
        calendar1.set(13, 59);
        calendar1.set(14, 999);
        Date date1 = calendar1.getTime();
        long l1 = date1.getTime();
        TimeInfo timeinfo = new TimeInfo();
        timeinfo.setStartTime(l);
        timeinfo.setEndTime(l1);
        return timeinfo;
    }

    public static TimeInfo getTodayStartAndEndTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date date = calendar.getTime();
        long l = date.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(11, 23);
        calendar1.set(12, 59);
        calendar1.set(13, 59);
        calendar1.set(14, 999);
        Date date1 = calendar1.getTime();
        long l1 = date1.getTime();
        TimeInfo timeinfo = new TimeInfo();
        timeinfo.setStartTime(l);
        timeinfo.setEndTime(l1);
        return timeinfo;
    }

    public static TimeInfo getBeforeYesterdayStartAndEndTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -2);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date date = calendar.getTime();
        long l = date.getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(5, -2);
        calendar1.set(11, 23);
        calendar1.set(12, 59);
        calendar1.set(13, 59);
        calendar1.set(14, 999);
        Date date1 = calendar1.getTime();
        long l1 = date1.getTime();
        TimeInfo timeinfo = new TimeInfo();
        timeinfo.setStartTime(l);
        timeinfo.setEndTime(l1);
        return timeinfo;
    }

    public static TimeInfo getCurrentMonthStartAndEndTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date date = calendar.getTime();
        long l = date.getTime();
        Calendar calendar1 = Calendar.getInstance();
        Date date1 = calendar1.getTime();
        long l1 = date1.getTime();
        TimeInfo timeinfo = new TimeInfo();
        timeinfo.setStartTime(l);
        timeinfo.setEndTime(l1);
        return timeinfo;
    }

    public static TimeInfo getLastMonthStartAndEndTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -1);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date date = calendar.getTime();
        long l = date.getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(2, -1);
        calendar1.set(5, 1);
        calendar1.set(11, 23);
        calendar1.set(12, 59);
        calendar1.set(13, 59);
        calendar1.set(14, 999);
        calendar1.roll(5, -1);
        Date date1 = calendar1.getTime();
        long l1 = date1.getTime();
        TimeInfo timeinfo = new TimeInfo();
        timeinfo.setStartTime(l);
        timeinfo.setEndTime(l1);
        return timeinfo;
    }

    public static String getTimestampStr()
    {
        return Long.toString(System.currentTimeMillis());
    }

    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

}
