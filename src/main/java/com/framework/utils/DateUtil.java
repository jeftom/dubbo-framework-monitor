package com.framework.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yuanjinglin on 17/6/19.
 */
public class DateUtil {

    /**
     * 私有化构造防止实例化
     */
    private DateUtil(){

    }
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 格式化日期 yyyy-MM-dd
     * @param date
     * @return
     */
    public static String format(Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    /**
     * 格式化日期
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 日期后N天，清空时分秒
     * @param date
     * @param day
     * @return
     */
    public static Date dateAdd(Date date, int day) {
        Calendar c = getCalendar(date);
        c.add(Calendar.DATE, day);
        return setZeroTime(c.getTime());
    }

    /**
     * 清空时分秒
     * @param date
     * @return
     */
    public static Date setZeroTime(Date date) {
        Calendar c = getCalendar(date);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date setEndTime(Date date) {
        Calendar c = getCalendar(date);

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 59);
        return c.getTime();
    }

    /**
     * 返回当前日期
     * @return
     */
    public static Date getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }
    /**
     * 当前日
     * @return
     */
    public static int getCurrentDayOfMonth() {
        Date now = getCurrentDate();
        Calendar c=DateUtil.getCalendar(now);
        return c.get(Calendar.DAY_OF_MONTH);
    }
    /**
     * 当前月
     * @return
     */
    public static int getCurrentMonth() {
        Date now = getCurrentDate();
        Calendar c=DateUtil.getCalendar(now);
        return c.get(Calendar.MONTH)+1;
    }
    /**
     * 当前年
     * @return
     */
    public static int getCurrentYear() {
        Date now = getCurrentDate();
        Calendar c=DateUtil.getCalendar(now);
        return c.get(Calendar.YEAR);
    }

    /**
     * 返回当前 Calendar
     * @return
     */
    public static Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.setTime(getCurrentDate());
        return c;
    }

    /**
     * 按日期生成Calendar
     * @param date
     * @return
     */
    public static Calendar getCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * 获取下月一号日期
     * @return
     */
    public static Date getNextMonth(){
        Calendar cal = getCalendar();
        cal.add(Calendar.MONTH,1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 合并年月 201609 201612
     * @param year
     * @param month
     * @return
     */
    public static int mergeYearMonth(int year,int month){
        int searchMonth=0;
        if(month>9){
            searchMonth=Integer.parseInt(year+""+month);
        }else{
            searchMonth=Integer.parseInt(year+"0"+month);
        }
        return searchMonth;
    }
}
