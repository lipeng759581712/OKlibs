package com.max.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具，提供了时间日期转换、获取时间标签等方法
 * @author Created by elwinxiao on 2015/4/18.
 */
public class TimeUtils
{
	/**
	 * 一天的秒数
	 */
	public static final int SECONDS_IN_DAY = 60 * 60 * 24;

	/**
	 * 一天的毫秒数
	 */
	public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

	/**
	 * 比较两个时间值（毫秒为单位）是否处于同一天
	 */
	public static boolean isSameDay (long millis1, long millis2)
	{
		return diffDays(millis1, millis2) == 0;
	}

	/**
	 * 获取两个时间相隔天数（millis1相对于millis2）
	 * <p/>
	 * 以自然天来计算，而不是以时间差值。如2015年11月4日凌晨1时与2015年11月3日23时只相差两小时，不足一天。
	 * 但按自然天算，这两个时间相差一天
	 */
	public static long diffDays (long millis1, long millis2)
	{
		return diffDays(new Date(millis1), new Date(millis2));
	}

	/**
	 * 比较两个日期相隔天数（date1相对于date2）
	 * <p/>
	 * 与{@link TimeUtils#diffDays(long, long)}一样是以自然天来计算
	 */
	public static int diffDays (Date date1, Date date2)
	{
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date1);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(date2);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		return day1 - day2;
	}

	/**
	 * 获取一个时间值相对于系统默认时区的1970/1/1 00:00:00的天数
	 * @param millis 时间值，毫秒为单位，以格林尼治时间1970/1/1 00:00:00为时间基准
	 */
	public static long toDay (long millis)
	{
		return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
	}

	/**
	 * 把时间（毫秒为单位）转化成日期（采用输入格式表示）
	 * @param dateFormat 日期格式，例如：MM/ dd/yyyy HH:mm:ss)
	 * @param millis     毫秒数
	 * @return
	 */
	public static String transferLongToDate (String dateFormat, Long millis)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millis);
		return sdf.format(date);
	}

	/**
	 * 获取当前日期
	 * @return 返回当前日期，其格式为"yyyy-MM-dd"
	 */
	public static String getCurDate ()
	{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(now);
	}

	/**
	 * 将时间值转化为北京时间格式（毫秒为单位），格式为"yyyy-MM-dd"。
	 * 如果withTime为true，那么还会显示具体时间，格式为" HH:mm:ss"
	 * @param withTime 是否显示具体时间
	 */
	public static String chinaFormatTime (long timestampMillsec, boolean withTime)
	{
		String formatString = "yyyy-MM-dd";
		if (withTime)
		{
			formatString += " HH:mm:ss";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatString, Locale.ROOT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
		String easyReadTime = dateFormat.format(new Date(timestampMillsec));
		return easyReadTime;
	}

	/**
	 * 弃用，请使用{@link #FormatTime(long)}
	 * <p/>
	 * 获取时间标签
	 * 假设系统当前时间与传入时间的差值为timeInterval，那么：
	 * <p/>
	 * 当 timeInterval < 0 时，返回具体日期时间，具体日期时间由{@link #chinaFormatTime(long, boolean)}方法得到；
	 * <p/>
	 * 当 timeInterval < 1min 时，返回“刚刚”；
	 * <p/>
	 * 当 1min < timeInterval < 1hour 时，返回字符串采用“xx分钟前”格式；
	 * <p/>
	 * 当 1hour < timeInterval < 1day 时，返回字符串采用“xx小时前”格式；
	 * <p/>
	 * 当 1hour < timeInterval < 1week 时，返回字符串采用“xx天前”格式；
	 * <p/>
	 * 当 timeInterval > 1week ，返回具体日期时间，具体日期时间由{@link #chinaFormatTime(long, boolean)}方法得到
	 * @param timestampMillsec 时间戳（毫秒）
	 * @param withTime         是否显示具体时间
	 */
	@Deprecated
	public static String easyReadTime (long timestampMillsec, boolean withTime)
	{
		long delta = System.currentTimeMillis() - timestampMillsec;
		final long second = 1000; // a second
		final long minute = second * 60; // a minute
		final long hour = minute * 60; // an hour
		final long day = hour * 24; // a day
		final long week = day * 7; // a week

		if (delta < 0)
		{
			String easyReadTime = chinaFormatTime(timestampMillsec, withTime);
			return easyReadTime;
		}
		else if (delta < minute)
		{
			return "刚刚";
		}
		else if (delta < hour)
		{
			return (delta / minute) + "分钟前";
		}
		else if (delta < day)
		{
			return (delta / hour) + "小时前";
		}
		else if (delta < week)
		{
			return (delta / day) + "天前";
		}
		else
		{
			String easyReadTime = chinaFormatTime(timestampMillsec, withTime);
			return easyReadTime;
		}
	}

	private static final String LESSONEMINUTE  = "1分钟前";
	private static final long   iLESSONEMINUTE = 60 * 1000;
	private static final String LESSONEHOUR    = "分钟前";
	private static final long   iLESSONEHOUR   = 60 * 60 * 1000;
	private static final String THESAMEDAY     = "今天 ";
	private static final long   iTHESAMEDAY    = 24 * 60 * 60 * 1000;
	private static final String YESTERDAY      = "昨天 ";
	private static final long   iYESTERDAY     = 48 * 60 * 60 * 1000;

	/**
	 * 获取时间标签
	 * 假设系统当前时间与传入时间的差值为timeInterval，那么：
	 * <p/>
	 * 当 timeInterval < 0 时，采用“MM-dd HH:mm”格式
	 * <p/>
	 * 当 timeInterval < 1min 时，返回“1分钟前”；
	 * <p/>
	 * 当 1min < timeInterval < 1hour 时，返回字符串采用“xx分钟前”格式；
	 * <p/>
	 * 当 1hour < timeInterval < 1day 时，需要判断是否处于同一天：如果是，采用“今天 HH:mm”格式，否则采用“昨天 HH:mm”格式；
	 * <p/>
	 * 当 1day < timeInterval < 2day 时，需要判断是否是前一天：如果是，采用采用“昨天 HH:mm”格式，否则按照大于两天进行处理；
	 * <p/>
	 * 当 timeInterval > 2day 时，需要判断是否处于同一年，如果是，采用“MM-dd HH:mm”格式，否则采用“yyyy-MM-dd”格式；
	 * @param timeSec 时间戳（秒为单位）
	 */
	public static String FormatTime (long timeSec)
	{
		long curTime = System.currentTimeMillis();
		timeSec = timeSec * 1000;

		if (timeSec > curTime)
		{ //比本地时间小
			SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
			return df.format(new Date(timeSec));
		}

		long intervalTime = curTime - timeSec;

		if (intervalTime < iLESSONEMINUTE)
		{
			return LESSONEMINUTE;  //小于1分钟前
		}

		if (intervalTime < iLESSONEHOUR)
		{

			long showTime = intervalTime / 60 / 1000;

			return showTime + LESSONEHOUR; //小于60分钟
		}

		if (intervalTime < iTHESAMEDAY)
		{
			SimpleDateFormat curdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

			String cursString = curdf.format(new Date(curTime));
			String tString = tdf.format(new Date(timeSec));

			String showTime = THESAMEDAY; //当天

			if (!cursString.equals(tString))
			{
				showTime = YESTERDAY;
			}

			SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.CHINA);
			showTime += df.format(new Date(timeSec));

			return showTime;
		}

		if (intervalTime < iYESTERDAY)
		{
			SimpleDateFormat yesdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

			long yestime = curTime - 1000 * 60 * 60 * 24;
			String yesString = yesdf.format(new Date(yestime));
			String tString = tdf.format(new Date(timeSec));

			if (yesString.equals(tString))
			{
				String showTime = YESTERDAY; //昨天

				SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.CHINA);
				showTime += df.format(new Date(timeSec));

				return showTime;
			}
		}

		//当前时间
		SimpleDateFormat curYear = new SimpleDateFormat("yyyy", Locale.CHINA);
		String sCurYear = curYear.format(new Date(curTime));

		SimpleDateFormat Year = new SimpleDateFormat("yyyy", Locale.CHINA);
		String sYear = Year.format(new Date(timeSec));

		if (sCurYear.equals(sYear))
		{
			SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
			return df.format(new Date(timeSec));
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

		return df.format(new Date(timeSec));
	}

	/**
	 * 秒  转换成  xx:xx:xx  时：分：秒格式
	 * @param time
	 * @return
	 */
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	private static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将时间戳转为时间字符串
	 * <p>格式为yyyy-MM-dd HH:mm:ss</p>
	 *
	 * @param millis 毫秒时间戳
	 * @return 时间字符串
	 */
	public static String millis2String(long millis) {
		return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(millis));
	}

	/**
	 * 将时间戳转为时间字符串
	 * <p>格式为pattern</p>
	 *
	 * @param millis  毫秒时间戳
	 * @param pattern 时间格式
	 * @return 时间字符串
	 */
	public static String millis2String(long millis, String pattern) {
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(millis));
	}








	/**
	 * 将Date类型转为时间字符串
	 * <p>格式为yyyy-MM-dd HH:mm:ss</p>
	 *
	 * @param date Date类型时间
	 * @return 时间字符串
	 */
	public static String date2String(Date date) {
		return date2String(date, DEFAULT_PATTERN);
	}

	/**
	 * 将Date类型转为时间字符串
	 * <p>格式为pattern</p>
	 *
	 * @param date    Date类型时间
	 * @param pattern 时间格式
	 * @return 时间字符串
	 */
	public static String date2String(Date date, String pattern) {
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
	}

	/**
	 * 将Date类型转为时间戳
	 *
	 * @param date Date类型时间
	 * @return 毫秒时间戳
	 */
	public static long date2Millis(Date date) {
		return date.getTime();
	}

	/**
	 * 将时间戳转为Date类型
	 *
	 * @param millis 毫秒时间戳
	 * @return Date类型时间
	 */
	public static Date millis2Date(long millis) {
		return new Date(millis);
	}














	/**
	 * 获取当前毫秒时间戳
	 *
	 * @return 毫秒时间戳
	 */
	public static long getNowTimeMills() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当前时间字符串
	 * <p>格式为yyyy-MM-dd HH:mm:ss</p>
	 *
	 * @return 时间字符串
	 */
	public static String getNowTimeString() {
		return millis2String(System.currentTimeMillis(), DEFAULT_PATTERN);
	}

	/**
	 * 获取当前时间字符串
	 * <p>格式为pattern</p>
	 *
	 * @param pattern 时间格式
	 * @return 时间字符串
	 */
	public static String getNowTimeString(String pattern) {
		return millis2String(System.currentTimeMillis(), pattern);
	}

	/**
	 * 获取当前Date
	 *
	 * @return Date类型时间
	 */
	public static Date getNowTimeDate() {
		return new Date();
	}






	/**
	 * 判断是否闰年
	 *
	 * @param date Date类型时间
	 * @return {@code true}: 闰年<br>{@code false}: 平年
	 */
	public static boolean isLeapYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return isLeapYear(year);
	}

	/**
	 * 判断是否闰年
	 *
	 * @param millis 毫秒时间戳
	 * @return {@code true}: 闰年<br>{@code false}: 平年
	 */
	public static boolean isLeapYear(long millis) {
		return isLeapYear(millis2Date(millis));
	}

	/**
	 * 判断是否闰年
	 *
	 * @param year 年份
	 * @return {@code true}: 闰年<br>{@code false}: 平年
	 */
	public static boolean isLeapYear(int year) {
		return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
	}




	/**
	 * 获取星期
	 *
	 * @param date Date类型时间
	 * @return 星期
	 */
	public static String getWeek(Date date) {
		return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
	}

	/**
	 * 获取星期
	 *
	 * @param millis 毫秒时间戳
	 * @return 星期
	 */
	public static String getWeek(long millis) {
		return getWeek(new Date(millis));
	}





	/**
	 * 获取星期
	 * <p>注意：周日的Index才是1，周六为7</p>
	 *
	 * @param date Date类型时间
	 * @return 1...7
	 */
	public static int getWeekIndex(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取星期
	 * <p>注意：周日的Index才是1，周六为7</p>
	 *
	 * @param millis 毫秒时间戳
	 * @return 1...7
	 */
	public static int getWeekIndex(long millis) {
		return getWeekIndex(millis2Date(millis));
	}


	/**
	 * 获取月份中的第几周
	 * <p>注意：国外周日才是新的一周的开始</p>
	 *
	 * @param date Date类型时间
	 * @return 1...5
	 */
	public static int getWeekOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取月份中的第几周
	 * <p>注意：国外周日才是新的一周的开始</p>
	 *
	 * @param millis 毫秒时间戳
	 * @return 1...5
	 */
	public static int getWeekOfMonth(long millis) {
		return getWeekOfMonth(millis2Date(millis));
	}




	/**
	 * 获取年份中的第几周
	 * <p>注意：国外周日才是新的一周的开始</p>
	 *
	 * @param date Date类型时间
	 * @return 1...54
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取年份中的第几周
	 * <p>注意：国外周日才是新的一周的开始</p>
	 *
	 * @param millis 毫秒时间戳
	 * @return 1...54
	 */
	public static int getWeekOfYear(long millis) {
		return getWeekOfYear(millis2Date(millis));
	}

	private static final String[] CHINESE_ZODIAC = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};




	/**
	 * 获取生肖
	 *
	 * @param date Date类型时间
	 * @return 生肖
	 */
	public static String getChineseZodiac(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
	}

	/**
	 * 获取生肖
	 *
	 * @param millis 毫秒时间戳
	 * @return 生肖
	 */
	public static String getChineseZodiac(long millis) {
		return getChineseZodiac(millis2Date(millis));
	}

	/**
	 * 获取生肖
	 *
	 * @param year 年
	 * @return 生肖
	 */
	public static String getChineseZodiac(int year) {
		return CHINESE_ZODIAC[year % 12];
	}

	private static final String[] ZODIAC       = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
	private static final int[]    ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};





	/**
	 * 获取星座
	 *
	 * @param date Date类型时间
	 * @return 星座
	 */
	public static String getZodiac(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return getZodiac(month, day);
	}

	/**
	 * 获取星座
	 *
	 * @param millis 毫秒时间戳
	 * @return 星座
	 */
	public static String getZodiac(long millis) {
		return getZodiac(millis2Date(millis));
	}

	/**
	 * 获取星座
	 *
	 * @param month 月
	 * @param day   日
	 * @return 星座
	 */
	public static String getZodiac(int month, int day) {
		return ZODIAC[day >= ZODIAC_FLAGS[month - 1]
				? month - 1
				: (month + 10) % 12];
	}


}
