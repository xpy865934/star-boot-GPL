package com.star.starboot.common.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.qcnt.qcnt.utils
 * @Description: 日期工具类
 * @Author: xpy
 * @Date: Created in 2019年06月11日 上午10:25
 */
public class DateUtils {
    /**
     * 根据固定的格式，将字符串转化为Date
     * @param str
     * @param ftm
     * @return
     */
    public static DateTime parseDate(String str, String ftm) {
        if (str == null){
            return null;
        }
        try {
            return DateTimeFormat.forPattern(ftm).parseDateTime(str);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取过去的天数
     * @param dateTime
     * @return
     */
    public static int pastDays(DateTime dateTime) {
        return getDistanceOfTwoDate(dateTime,DateTime.now());
    }

    /**
     * 获取过去的小时
     * @param dateTime
     * @return
     */
    public static int pastHour(DateTime dateTime) {
        return getDistHoursOfTwoDate(dateTime,DateTime.now());
    }

    /**
     * 获取过去的分钟
     * @param dateTime
     * @return
     */
    public static long pastMinutes(DateTime dateTime) {
        return diffMinute(dateTime,DateTime.now());
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis){
        long day = timeMillis/(24*60*60*1000);
        long hour = (timeMillis/(60*60*1000)-day*24);
        long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
        long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
        long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
        return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }

    /**
     * 获取两个日期之间的天数，DateTime类型
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDistanceOfTwoDate(DateTime start, DateTime end) {
        return Days.daysBetween(start,end).getDays();
    }
    /**
     * 获取两个日期之间的天数，字符串格式
     *
     * @param start
     * @param end
     * @param fmt : 字符串的日期格式
     * @return
     */
    public static int getDistanceOfTwoDate(String start, String end, String fmt){
        DateTime startD = parseDate(start, fmt);
        DateTime endD = parseDate(end, fmt);
        return getDistanceOfTwoDate(startD, endD);
    }
    /**
     * @Description: 获取两个日期之间的小时数
     * @param start
     * @param end
     * @return
     */
    public static int getDistHoursOfTwoDate(DateTime start, DateTime end){
        return  Hours.hoursBetween(start,end).getHours();
    }
    /**
     * @Description: 获取两个时间相差的分钟数
     * @param start
     * @param end
     * @return
     */
    public static int diffMinute(DateTime start, DateTime end){
        return Minutes.minutesBetween(start,end).getMinutes();
    }

    /**
     * 通过毫秒时间戳获取小时数和分钟数
     * @param time
     * @return
     */
    public static String getHourAndMinute(long time){
        int minute = (int)Math.ceil((double)(time) / (1000 * 60));
        int hours = (int) Math.floor((double)minute / 60);
        minute = minute % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0){
            sb.append(hours).append("小时");
        }
        if (minute > 0){
            sb.append(minute).append("分");
        }
        return sb.toString();
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDate() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    public static int getCurrentMilliSecond() {
        return Calendar.getInstance().get(Calendar.MILLISECOND);
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
