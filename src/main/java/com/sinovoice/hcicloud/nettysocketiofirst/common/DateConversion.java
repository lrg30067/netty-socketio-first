package com.sinovoice.hcicloud.nettysocketiofirst.common;


import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateConversion {

	/**
	 * 获得几天前的日期
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 获得几月前的日期
	 * 
	 * @param d
	 * @param month
	 * @return
	 */
	public static Date getMonthBefore(Date d, int month) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.MONTH, now.get(Calendar.MONTH) - month);
		return now.getTime();
	}

	/**
	 * 获得几年前的日期
	 * 
	 * @param d
	 * @param year
	 * @return Date
	 */
	public static Date getYearBefore(Date d, int year) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.YEAR, now.get(Calendar.YEAR) - year);
		return now.getTime();
	}

	/**
	 * 获得两个日期之间的所有日期字符串
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public static List<String> getDay(Calendar startDay, Calendar endDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar currentPrintDay = startDay;
		List<String> list = new ArrayList<String>();
		list.add(sdf.format(currentPrintDay.getTime()));
		// 如果起始日期大于等于结束日期则返回
		if (startDay.getTimeInMillis() >= endDay.getTimeInMillis()) {
			return list;
		}
		while (true) {
			// 日期加一
			currentPrintDay.add(Calendar.DATE, 1);
			// 日期加一后判断是否达到终了日，达到则终止打印
			if (currentPrintDay.compareTo(endDay) == 0) {
				list.add(sdf.format(currentPrintDay.getTime()));
				break;
			}
			list.add(sdf.format(currentPrintDay.getTime()));
		}
		return list;
	}

	/**
	 * 获得两个日期之间的所有月份字符串
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public static List<String> getMonth(Calendar startDay, Calendar endDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar currentPrintDay = startDay;
		List<String> list = new ArrayList<String>();
		list.add(sdf.format(currentPrintDay.getTime()));
		// 如果起始日期大于等于结束日期则返回
		if (startDay.getTimeInMillis() >= endDay.getTimeInMillis()) {
			return list;
		}
		while (true) {
			// 月份加一
			currentPrintDay.add(Calendar.MONTH, 1);
			// 月份加一后判断是否达到终了日，达到则终止打印
			if (currentPrintDay.compareTo(endDay) == 0) {
				list.add(sdf.format(currentPrintDay.getTime()));
				break;
			}
			list.add(sdf.format(currentPrintDay.getTime()));
		}
		return list;
	}

	/**
	 * 获得两个日期之间的所有年份字符串
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public static List<String> getYear(Calendar startDay, Calendar endDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar currentPrintDay = startDay;
		List<String> list = new ArrayList<String>();
		list.add(sdf.format(currentPrintDay.getTime()));
		// 如果起始日期大于等于结束日期则返回
		if (startDay.getTimeInMillis() >= endDay.getTimeInMillis()) {
			return list;
		}
		while (true) {
			// 年份加一
			currentPrintDay.add(Calendar.YEAR, 1);
			// 年份加一后判断是否达到终了日，达到则终止打印
			if (currentPrintDay.compareTo(endDay) == 0) {
				list.add(sdf.format(currentPrintDay.getTime()));
				break;
			}
			list.add(sdf.format(currentPrintDay.getTime()));
		}
		return list;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSpecifiedDayAfter(Date date) {
		Calendar c = Calendar.getInstance();
		if (date == null) {
			c.setTime(new Date());
		} else {
			c.setTime(date);
		}
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);
		return c.getTime();
	}
/**
 * @Title: getTimeStamp 
 * @Description: 字符串转换为时间戳
 * @param date
 * @return Timestamp
 * @throws
 */
	public static Timestamp getTimeStamp(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long times = 0;
		try {
			times = sdf.parse(date).getTime();
		} catch (ParseException e) {
		}
		return new Timestamp(times);
	}

	/**
	 * @Title: getTimeString
	 * @Description: 格式化日期
	 * @param date
	 * @param pattern
	 * @return String
	 * @throws
	 */
	public static String getTimeString(Date date, String pattern) {
		SimpleDateFormat sFormat = new SimpleDateFormat();
		sFormat.applyPattern(pattern);
		return sFormat.format(date);
	}

	/**
	 * @Title: DateToStr
	 * @Description: 日期转为字符串
	 * @param date
	 * @param formatStr
	 * @return String
	 * @throws
	 */
	public static String DateToStr(Date date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String str = format.format(date);
		return str;
	}

	/**
	 * 使用参数Format将字符串转为Date,yyyy-MM-dd HH:mm:ss
	 */
	public static Date StrToDate(String strDate, String pattern)  throws ParseException
	{
		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
	}


	/**
	 * 格式化时间
	 * @param ms
	 * @return
	 */
	public static String formatTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;
		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		Long milliSecond = ms - day * dd - hour * hh - minute * mi - second
				* ss;
		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day + "天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}
		if (second <=0&&milliSecond > 0) {
			sb.append(milliSecond + "毫秒");
		}
		return sb.toString();
	}
	
	/**
	 * @Title: getDateByTime 
	 * @Description: 毫秒时间戳UNIX数获取时间
	 * @param time
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getDateByTime(Long time, String format){
			Date d = new Date(time);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(d);
		}

	/**
	 *
	 * @param data
	 * @param format
	 * @Description: 字符串时间格式转换为毫秒
	 * @return
	 */

	public static Long getTimeInMillis(String data, String format){
		Calendar c = Calendar.getInstance();
		Long timeInMillis=null;
		try {
			c.setTime(new SimpleDateFormat(format).parse(data));
			 timeInMillis = c.getTimeInMillis();
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return timeInMillis;
	}

	/**
	 * 获取前几天 00：00：00
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDayBeforeBegin(Date d, int day){
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		now.set(Calendar.HOUR_OF_DAY,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);
		now.set(Calendar.MILLISECOND,0);

		return now.getTime();
	}

	/**
	 * 获取前几天 23:59:59
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDayBeforeEnd(Date d, int day){
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		now.set(Calendar.HOUR_OF_DAY,23);
		now.set(Calendar.MINUTE,59);
		now.set(Calendar.SECOND,59);
		now.set(Calendar.MILLISECOND,999);
		return now.getTime();
	}

//	public static void main(String[] args){
//		Calendar nowTime = Calendar.getInstance();
//		nowTime.set(Calendar.MONTH, nowTime.get(Calendar.MONTH) + 1);
//		System.out.println((nowTime.get(Calendar.MONTH)+1) < 10 ?  "0" + (nowTime.get(Calendar.MONTH)+1) :  (nowTime.get(Calendar.MONTH)+1));
//	}

}
