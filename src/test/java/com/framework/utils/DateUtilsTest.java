package com.framework.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 李溪林 on 17-2-16.
 */
public class DateUtilsTest {
    @Test
    public void formatTest(){
        Date d0 = new Date();
        System.out.println(DateUtils.format(d0));
        String format = "yyyy-MM-dd HH:mm:ss";
        System.out.println(DateUtils.format(d0,format));
    }

    @Test
    public void clearTimeTest(){
        Date d0 = new Date();
        Date d1 = DateUtils.clearTime(d0);
        String format = "yyyy-MM-dd HH:mm:ss";
        System.out.println(DateUtils.format(d1,format));
    }

    @Test
    public void addYearsTest(){
        Date d0 = new Date();
        String format = "yyyy";
        System.out.println(DateUtils.format(d0, format));

        Date d2 = DateUtils.addYears(d0, 2);
        System.out.println(DateUtils.format(d2,format));

        Date d3 = DateUtils.addYears(d0, 10);
        System.out.println(DateUtils.format(d3,format));
    }

    @Test
    public void addMonthsTest(){
        Date d0 = new Date();
        String format = "yyyy-MM";
        System.out.println(DateUtils.format(d0, format));

        Date d2 = DateUtils.addMonths(d0, 2);
        System.out.println(DateUtils.format(d2,format));

        Date d3 = DateUtils.addMonths(d0, 10);
        System.out.println(DateUtils.format(d3,format));
    }

    @Test
    public void addDaysTest(){
        Date d1 = new Date();
        System.out.println(DateUtils.format(d1));

        Date d2 = DateUtils.addDays(d1, 2);
        System.out.println(DateUtils.format(d2));

        Date d3 = DateUtils.addDays(d1, 10);
        System.out.println(DateUtils.format(d3));
    }

    @Test
    public void AddHoursTest(){
        Date d0 = new Date();
        String format = "yyyy-MM-dd HH";
        System.out.println(DateUtils.format(d0, format));

        Date d2 = DateUtils.addHours(d0, 2);
        System.out.println(DateUtils.format(d2,format));

        Date d3 = DateUtils.addHours(d0, 10);
        System.out.println(DateUtils.format(d3,format));
    }

    @Test
    public void addMinutesTest(){
        Date d0 = new Date();
        String format = "yyyy-MM-dd HH:mm";
        System.out.println(DateUtils.format(d0, format));

        Date d2 = DateUtils.addMinutes(d0, 2);
        System.out.println(DateUtils.format(d2,format));

        Date d3 = DateUtils.addMinutes(d0, 10);
        System.out.println(DateUtils.format(d3,format));
    }

    @Test
    public void addSecondsTest(){
        Date d0 = new Date();
        String format = "yyyy-MM-dd HH:mm:ss";
        System.out.println(DateUtils.format(d0, format));

        Date d2 = DateUtils.addSeconds(d0, 2);
        System.out.println(DateUtils.format(d2,format));

        Date d3 = DateUtils.addSeconds(d0, 10);
        System.out.println(DateUtils.format(d3,format));
    }

    @Test
    public void newDateTest() {
        Date d1 = DateUtils.newDate(2016,8,1,13,52,35);
        Date d2 = DateUtils.newDate(2016,8,1,13,52);
        Date d3 = DateUtils.newDate(2016,8,1,13);
        Date d4 = DateUtils.newDate(2016,8,1);

        Date d5 = DateUtils.newDate("2016-08-01");

        Date d6 = DateUtils.newDate("2016-08-01","yyyy-MM");

    }

    @Test
    public void getYearMonthDayHourMinuteSecond(){
        Date d1 = DateUtils.newDate(2016,8,1,13,52,35);

        int year = DateUtils.getYear(d1);
        int month = DateUtils.getMonth(d1);
        int day = DateUtils.getDay(d1);
        int hour = DateUtils.getHour(d1);
        int minute = DateUtils.getMinute(d1);
        int second = DateUtils.getSecond(d1);
        int dayofweek = DateUtils.getDayOfWeek(d1);
        int dayofyear = DateUtils.getDayOfYear(d1);
    }

    @Test
    public void tryParse(){
        Date current = DateUtils.getNow();

        Date a = DateUtils.addDays(current, -1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Date the = cal.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(a);
        cal2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        Date last = cal2.getTime();

        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(current);
        cal3.add(Calendar.DATE, -1);
        cal3.add(Calendar.WEEK_OF_YEAR, -1);
        cal3.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Date first = cal3.getTime();
    }

    @Test
    public void hasRereat() {
        Date begindt1 = DateUtils.newDate(2016,12,3);
        Date enddt1 = DateUtils.newDate(2016,12,8);

        Date begindt2 = DateUtils.newDate(2016,12,1);
        Date enddt2 = DateUtils.newDate(2016,12,3);

        boolean r = DateUtils.hasRereat(begindt1, enddt1, begindt2, enddt2);

        System.out.println(r);
    }

    @Test
    public void getDiffOfDays(){
        Date dt1 = DateUtils.newDate(2016,12,28, 11);
        Date dt2 = DateUtils.newDate(2017,1,3, 6);


        long days = DateUtils.getDiffOfDays(dt1, dt2);
        System.out.println(days);
    }
}
