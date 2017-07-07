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
}
