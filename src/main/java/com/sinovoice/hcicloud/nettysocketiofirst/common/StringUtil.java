package com.sinovoice.hcicloud.nettysocketiofirst.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {
    

    /**
     * 将字符串转换为时间格式
     * @param dateStr
     * @param formatStr
     * @return
     */
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串是否为空
	 * @param input
	 * @return
	 */
	public boolean isNullOrEmpty(String input) {
		return input == null || input.length() == 0;
	}
	
	/**
	 * 普通文本转为普通日期
	 * @param dateStr
	 * @return
	 */
	public static Date stringToNormalDate(String dateStr){
        String format = "yyyy-MM-dd HH:mm:ss";
        return StringToDate(dateStr,format);
    }

	/**
	 * 时间格式转化
	 * @param oriStr
	 * @param oriFormat
	 * @param resultFormat
	 * @return
	 */
	public static String StringFormataChange(String oriStr, String oriFormat, String resultFormat){
	    Date resultDate = StringToDate(oriStr, oriFormat);
	    return dateToString(resultDate, resultFormat);
	}
	
	/**
	 * @Description 后台返回页面的信息中可能有换行,这里进行替换存取
	 * @param singleRow
	 * @return
	 */
	public static String setMultiRows(String singleRow) {
		return singleRow.replace("\r\n", "<br/>");
	}
	
	/**
	 * 数据库里的换行替换为页面的换行
	 * @param multiRow
	 * @return
	 */
	public static String getMultiRows(String multiRow) {
		return multiRow.replace("<br/>", "\\r\\n");
	}
	
	/**
	 * 获取几天前的日期字符串
	 * @param today
	 * @param count
	 * @return
	 */
	public static String getDaysBefore(Date today, int count) {
		if (today == null) {
			return null;
		}
		Calendar now = Calendar.getInstance();
		now.setTime(today);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - count);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String destFileStr = dateFormat.format(now.getTime());
		return destFileStr;
	}
	
	/**
	 * 将时间转换为字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 将时间转换为通用格式的字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		String format = "yyyy-MM-dd hh:mm:ss";
		DateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 将字符串的首字母改为大写
	 * @param name
	 * @return
	 */
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
        
    }
    
    /**
	 * 去除头尾的全角和半角空格
	 * 
	 * @param value
	 * @return
	 */
	public static String trimC(String value) {
		int len = value.length();
		int st = 0;
		char[] val = value.toCharArray(); /* avoid getfield opcode */

		while ((st < len) && (val[st] == '　' || val[st] <= ' ')) {
			st++;
		}
		while ((st < len) && (val[len - 1] == '　' || val[len - 1] <= ' ')) {
			len--;
		}
		return ((st > 0) || (len < value.length())) ? value.substring(st, len)
				: value;
	}
}

