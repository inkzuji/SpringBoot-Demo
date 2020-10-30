package com.zuji.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * jdk8 日期工具类
 *
 * @author Ink足迹
 * @create 2020-02-27 2:34 下午
 **/
public class LocalDateUtils {
    /**
     * flag -1
     */
    private static final transient int OWE_ONE = -1;

    /**
     * flag 0
     */
    private static final transient int ZERO = 0;

    /**
     * flag 1
     */
    private static final transient int ONE = 1;

    /**
     * date format yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * date format yyyy.MM.dd HH:mm:ss
     */
    public static final String DATE_TIME_PATTERN_POINT = "yyyy.MM.dd HH:mm:ss";

    /**
     * date format yyyy-MM-dd
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * date format yyyy.MM.dd
     */
    public static final String DATE_PATTERN_POINT = "yyyy.MM.dd";

    /**
     * date format yyyy-MM-dd
     */
    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * date format yyyy-MM
     */
    public static final String DATE_FMT_6 = "yyyy-MM";

    /**
     * date format dd/MM/yy HH:mm:ss
     */
    public static final String TIME_PATTERN_SHORT = "dd/MM/yy HH:mm:ss";

    /**
     * date format dd/MM/yy HH24:mm
     */
    public static final String TIME_PATTERN_SHORT_1 = "yyyy/MM/dd HH:mm";

    /**
     * date format yyyy/MM/dd
     */
    public static final String DATE_FMT_1 = "yyyy/MM/dd";

    /**
     * date format yyyyMMddHHmmss
     */
    public static final String TIME_PATTERN_SECOND = "yyyyMMddHHmmss";

    /**
     * localDateTime 转 自定义日期格式 String
     *
     * @param localDateTime 需要转换的时间
     * @param format        格式：例：yyyy-MM-dd hh:mm:ss
     * @return String
     */
    public static String formatLocalDateTimeToString(LocalDateTime localDateTime, String format) {
        String dateTimeStr = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            dateTimeStr = localDateTime.format(formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return dateTimeStr;
    }

    /**
     * String 转 LocalDateTime
     *
     * @param dateStr 例："2017-08-11 01:00:00"
     * @param format  例："yyyy-MM-dd HH:mm:ss"
     * @return LocalDateTime
     */
    public static LocalDateTime stringToLocalDateTime(String dateStr, String format) {
        LocalDateTime localDateTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            localDateTime = LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return localDateTime;
    }

    /**
     * Date 转 LocalDate
     *
     * @param date 需要转换的日期
     * @return LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        return dateToLocalDateTime(date).toLocalDate();
    }

    /**
     * Date 转 LocalDateTime
     *
     * @param date 需要转换的日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        /*
        long time = date.getTime();
        long nanoOfSecond = (time % 1000) * 1000000;
        return LocalDateTime.ofEpochSecond(time / 1000, (int) nanoOfSecond, ZoneOffset.of("+8"));
        */

        // An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        Instant instant = date.toInstant();
        // A time-zone ID, such as {@code Europe/Paris}.(时区)
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime 需要转换的时间
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        // Combines this date-time with a time-zone to create a  ZonedDateTime.
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * String 转 Date
     *
     * @param dateStr 例："2017-08-11 01:00:00"
     * @param format  例："yyyy-MM-dd HH:mm:ss"
     * @return Date
     */
    public static Date stringToDateTime(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date 转 String
     *
     * @param date   时间
     * @param format 例："yyyy-MM-dd HH:mm:ss"
     * @return Date
     */
    public static String dateToString(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据 时间 获取 当月有多少天数
     *
     * @param date 日期
     * @return int 当月天数
     */
    public static int getActualMaximum(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return localDate.getMonth().length(localDate.isLeapYear());
    }

    /**
     * 根据 日期 获得 星期
     *
     * @param date 日期
     * @return 1:星期一；2:星期二；3:星期三；4:星期四；5:星期五；6:星期六；7:星期日；
     */
    public static int getWeekOfDate(Date date) {
        return dateToLocalDateTime(date).getDayOfWeek().getValue();
    }

    /**
     * 计算两个时间LocalDateTime相差的 天数，不考虑日期前后，返回结果>=0
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return int 相差天数，绝对值
     */
    public static int getAbsTimeDiffDay(LocalDateTime before, LocalDateTime after) {
        return getAbsDateDiffDay(before.toLocalDate(), after.toLocalDate());
    }

    /**
     * 计算两个日期 LocalDate 相差的 天数，不考虑日期前后，返回结果>=0
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return int 相差天数，绝对值
     */
    public static int getAbsDateDiffDay(LocalDate before, LocalDate after) {
        return Math.abs(Period.between(before, after).getDays());
    }

    /**
     * 计算两个时间 LocalDateTime 相差的 月数，不考虑日期前后，返回结果>=0
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return int 返回相差月数，绝对值
     */
    public static int getAbsTimeDiffMonth(LocalDateTime before, LocalDateTime after) {
        return getAbsDateDiffMonth(before.toLocalDate(), after.toLocalDate());
    }

    /**
     * 计算两个时间 LocalDate 相差的 月数，不考虑日期前后，返回结果>=0
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return int 返回相差月数，绝对值
     */
    public static int getAbsDateDiffMonth(LocalDate before, LocalDate after) {
        return Math.abs(Period.between(before, after).getMonths());
    }

    /**
     * 计算两个时间 LocalDateTime 相差的 年数，不考虑日期前后，返回结果>=0
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return int 返回相差年数，绝对值
     */
    public static int getAbsTimeDiffYear(LocalDateTime before, LocalDateTime after) {
        return getAbsDateDiffYear(before.toLocalDate(), after.toLocalDate());
    }

    /**
     * 计算两个时间 LocalDate 相差的 年数，不考虑日期前后，返回结果>=0
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return int 返回相差年数，绝对值
     */
    public static int getAbsDateDiffYear(LocalDate before, LocalDate after) {
        return Math.abs(Period.between(before, after).getYears());
    }

    /**
     * 获取指定日期的当月的月份数
     *
     * @param date 日期
     * @return int 返回月份
     */
    public static int getAssignDateMonth(Date date) {
        return dateToLocalDateTime(date).getMonthValue();
    }


    /**
     * 获取指定日期的当月第一天
     *
     * @param date 日期
     * @return LocalDate 返回日期
     */
    public static LocalDate getMonthFirstDay(Date date) {
        final int firstDay = 1;
        return getMonthAssignDay(date, firstDay);
    }

    /**
     * 获取指定日期的当月最后一天
     *
     * @param date 日期
     * @return LocalDate 返回日期
     */
    public static LocalDate getMonthLastDay(Date date) {
        // 获取指定月份的正月天数
        int lastDay = getActualMaximum(date);
        return getMonthAssignDay(date, lastDay);
    }

    /**
     * 获取指定日期的当月第n天日期
     *
     * @param date 日期
     * @return LocalDate 返回日期
     */
    public static LocalDate getMonthAssignDay(Date date, int day) {
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), day);
    }


    /**
     * 获取指定日期年的第一天
     *
     * @param date 日期
     * @return LocalDate 返回日期
     */
    public static LocalDate newThisYear(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), 1, 1);

    }

    /**
     * 获取当前日期的时间戳
     *
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(Instant.now().toEpochMilli());
    }

    /**
     * Timestamp 转 LocalDateTime
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime timestampToLocalDateTime(Timestamp date) {
        return LocalDateTime.ofEpochSecond(date.getTime() / 1000, date.getNanos(), ZoneOffset.of("+8"));
    }


    /**
     * timestamp 转 LocalDateTime
     *
     * @param date
     * @return LocalDate
     */
    public static LocalDate timestampToLocalDate(Timestamp date) {
        return timestampToLocalDateTime(date).toLocalDate();
    }

    /**
     * 指定时区，获取日期时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai")));
    }


    /**
     * 修改日期时间的时间部分
     *
     * @param date       指定日期
     * @param customTime 必须为"hh:mm:ss"这种格式
     */
    public static LocalDateTime reserveDateCustomTime(Date date, String customTime) {
        String dateStr = dateToLocalDate(date).toString() + " " + customTime;
        return stringToLocalDateTime(dateStr, DATE_TIME_PATTERN);
    }

    /**
     * 修改日期时间的时间部分
     *
     * @param date       指定时间戳
     * @param customTime 必须为"hh:mm:ss"这种格式
     */
    public static LocalDateTime reserveDateCustomTime(Timestamp date, String customTime) {
        String dateStr = timestampToLocalDate(date).toString() + " " + customTime;
        return stringToLocalDateTime(dateStr, DATE_TIME_PATTERN);
    }

    /**
     * 把日期后的时间归0 变成(yyyy-MM-dd 00:00:00:000)
     *
     * @param date
     * @return LocalDateTime
     */
    public static final LocalDateTime zerolizedTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), ZERO, ZERO, ZERO, ZERO);
    }


    /**
     * 把时间变成(yyyy-MM-dd 00:00:00:000)
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime zerolizedTime(Timestamp date) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return changeDateOfTime(localDateTime, ZERO, ZERO, ZERO, ZERO);
    }

    /**
     * 把日期的时间变成(yyyy-MM-dd 23:59:59:999)
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime getEndTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return changeDateOfTime(localDateTime, 23, 59, 59, 999 * 1000000);
    }

    /**
     * 把时间变成(yyyy-MM-dd 23:59:59:999)
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime getEndTime(Timestamp date) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return changeDateOfTime(localDateTime, 23, 59, 59, 999 * 1000000);
    }

    /**
     * 修改日期后的 time 部分
     *
     * @param localDateTime 指定日期时间
     * @param hour          小时
     * @param minute        分
     * @param second        秒
     * @param nanoOfSecond  纳秒
     * @return LocalDateTime
     */
    public static LocalDateTime changeDateOfTime(LocalDateTime localDateTime, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), hour, minute, second, nanoOfSecond);
    }


    /**
     * 计算特定时间到 当天 23.59.59.999 的秒数
     *
     * @return
     */
    public static int calculateToEndTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime end = getEndTime(date);
        return (int) (end.toEpochSecond(ZoneOffset.UTC) - localDateTime.toEpochSecond(ZoneOffset.UTC));
    }


    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param localDateTime 指定日期时间 jdk8
     * @param chronoUnit    时间类型 例：ChronoUnit.DAYS
     * @param num           需增加的时间
     * @return LocalDateTime
     */
    public static LocalDateTime addTime(LocalDateTime localDateTime, ChronoUnit chronoUnit, int num) {
        return localDateTime.plus(num, chronoUnit);
    }

    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param date       指定日期时间
     * @param chronoUnit 时间类型 例：ChronoUnit.DAYS
     * @param num        需增加的时间
     * @return LocalDateTime
     */
    public static LocalDateTime addTime(Date date, ChronoUnit chronoUnit, int num) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.plus(num, chronoUnit);
    }

    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param date       指定时间戳
     * @param chronoUnit 时间类型 例：ChronoUnit.DAYS
     * @param num        需增加的时间
     * @return LocalDateTime
     */
    public static LocalDateTime addTime(Timestamp date, ChronoUnit chronoUnit, int num) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return localDateTime.plus(num, chronoUnit);
    }

    /**
     * 减少年/月/周/天/小时/分/秒数
     *
     * @param chronoUnit 例：ChronoUnit.DAYS
     * @param num
     * @return LocalDateTime
     */
    public static LocalDateTime minusTime(Date date, ChronoUnit chronoUnit, int num) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.minus(num, chronoUnit);
    }

    /**
     * 比较两个时间 LocalDateTime 大小
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return int 1 = 大于；0 = 相等；-1 = 小于
     */
    public static int compareTwoDateTime(LocalDateTime begin, LocalDateTime end) {
        int result = ZERO;
        if (begin.isAfter(end)) {
            result = ONE;
        } else if (begin.isBefore(end)) {
            result = OWE_ONE;
        }
        return result;
    }

    /**
     * 比较 Timestamp 两个时间相差的秒数，如果 begin <= end, 返回 -1
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return long
     */
    public static long getTwoTimeDiffSecond(Timestamp time1, Timestamp time2) {
        long diff = timestampToLocalDateTime(time1).toEpochSecond(ZoneOffset.UTC) - timestampToLocalDateTime(time2).toEpochSecond(ZoneOffset.UTC);
        return diff > ZERO ? diff : OWE_ONE;
    }

    /**
     * 比较 Timestamp 两个时间相差的分钟数，如果 begin <= end, 返回 -1
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return long
     */
    public static long getTwoTimeDiffMin(Timestamp time1, Timestamp time2) {
        long diff = getTwoTimeDiffSecond(time1, time2) / 60;
        return diff > ZERO ? diff : OWE_ONE;
    }

    /**
     * 比较 Timestamp 两个时间相差的小时数，如果time1<=time2,返回 -1
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return long
     */
    public static long getTwoTimeDiffHour(Timestamp time1, Timestamp time2) {
        long diff = getTwoTimeDiffSecond(time1, time2) / 3600;
        return diff > ZERO ? diff : OWE_ONE;
    }

    /**
     * 判断 当前时间 是否在时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return Boolean
     */
    public static boolean isTimeInRange(Date startTime, Date endTime) {
        LocalDateTime now = getCurrentLocalDateTime();
        LocalDateTime start = dateToLocalDateTime(startTime);
        LocalDateTime end = dateToLocalDateTime(endTime);
        return (start.isBefore(now) && end.isAfter(now)) || start.isEqual(now) || end.isEqual(now);
    }

    /**
     * 判断 当前时间 是否在时间范围内
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 在时间范围内 = true
     */
    public static boolean isTimeInRange(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = getCurrentLocalDateTime();
        return (start.isBefore(now) && end.isAfter(now)) || start.isEqual(now) || end.isEqual(now);
    }

    /**
     * 判断比较两个日期的大小
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return int -1 = 小于; 0 = 等于; 1 = 大于;
     */
    public static int compareDate(Date startDate, Date endDate) {
        LocalDate start = dateToLocalDate(startDate);
        LocalDate end = dateToLocalDate(endDate);
        int result = ONE;
        if (start.isBefore(end)) {
            result = OWE_ONE;
        } else if (start.isEqual(end)) {
            result = ZERO;
        }
        return result;
    }

    /**
     * 判断比较两个日期时间的大小
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return int -1 = 小于; 0 = 等于; 1 = 大于;
     */
    public static int compareDateTime(Date startDate, Date endDate) {
        LocalDateTime start = dateToLocalDateTime(startDate);
        LocalDateTime end = dateToLocalDateTime(endDate);
        return compareTwoDateTime(start, end);
    }

    /**
     * 获取今天还剩下多少秒
     *
     * @return
     */
    public static int getTodayLastSecond() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime maxToday = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return (int) ChronoUnit.SECONDS.between(currentTime, maxToday);
    }

    /**
     * 当前日期加上天数后的日期
     *
     * @param num 为增加的天数
     * @return
     */
    public static Date plusDay(int num) {
        return plusDay(new Date(), num);
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param date 日期
     * @param num  为增加的天数
     * @return
     */
    public static Date plusDay(Date date, int num) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currDate = format.format(date);
        System.out.println("现在的日期是：" + currDate);

        Calendar ca = Calendar.getInstance();
        // num为增加的天数，可以改变的
        ca.add(Calendar.DATE, num);
        date = ca.getTime();
        String endDate = format.format(date);
        System.out.println("增加天数以后的日期：" + endDate);
        return date;
    }
}
