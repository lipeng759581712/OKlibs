package com.max.log;

import android.util.Log;


/*
使用说明：
ALog.e("tag","list === null")；
*/


public class ALog {

	/**
	 * 以下方法具体调用哪个，是由接口Impl决定的
	 * @param tag
	 * @param message
	 */
	public static void v(String tag, String message) {
		getImpl().v(tag, message);
	}
	
	public static void d(String tag, String message) {
		getImpl().d(tag, message);
	}
	
	public static void i(String tag, String message) {
		getImpl().i(tag, message);
	}
	
	public static void w(String tag, String message) {
		getImpl().w(tag, message);
	}
	
	public static void e(String tag, String message) {
		getImpl().e(tag, message);
	}


	private static LoggerInterface getImpl() {
		return Accessor.getImpl();
	}
	
	public static class Accessor {
		private static LoggerInterface sLoggerImpl;
		private static LoggerInterface getImpl() {
			if (sLoggerImpl == null) {
				/**
				 * 这里指出了是AndroidLogcatImpl接口的实现
				 */
				sLoggerImpl = new AndroidLogcatImpl();
			}
			return sLoggerImpl;
		}

		/**
		 * 也可以set一个接口进来
		 * @param impl
		 */
		public static void setImpl(LoggerInterface impl) {
			sLoggerImpl = impl;
		}
	}




	/*
	使用说明：

	private static final ALog.ALogger logger = new ALog.ALogger("tag", "QTConnector");
	logger.i("open connection");

	*/

	/**
	 * 这个日志类是能够打印出详细的信息的,(tag,类名，前缀，线程名称)
	 */
	public static class ALogger {

		/**
		 * 是否显示日志（true 不显示）
		 */
		private boolean mIsDisabled;

		/**
		 * 日志开关
		 * @param isEnabled
		 * @return
		 */
		public ALogger setEnabeState(boolean isEnabled) {
			mIsDisabled = !isEnabled;
			return this;
		}

		/**
		 *
		 * @param tag  log tag
		 * @param clasz 类名
		 * @param prefix 前缀
		 */
		
		private String mTag;
		private String mPrefix;
		private Class<?> mClasz;

		public ALogger(String tag) {
			this(tag, null, null);
		}
		
		public ALogger(String tag, String prefix) {
			this(tag, null, prefix);
		}


		public ALogger(String tag, Class<?> clasz, String prefix) {
			mTag = tag;
			mPrefix = prefix;
			mClasz = clasz;
			
			if (tag == null && clasz != null) {
				mTag = clasz.getSimpleName();
				mClasz = null;
			}
		}
		

		/**
		 * 日志接口还是使用（Android Log）
		 * @return
		 */
		private LoggerInterface getImpl() {
			return ALog.getImpl();
		}

		public void v(String text) {
			if (mIsDisabled) return;
			getImpl().v(mTag, formatText(text));
		}

		
		public void d(String text) {
			if (mIsDisabled) return;
			getImpl().d(mTag, formatText(text));
		}
		
		public void i(String text) {
			if (mIsDisabled) return;
			getImpl().i(mTag, formatText(text));
		}
		
		public void w(String text) {
			if (mIsDisabled) return;
			getImpl().w(mTag, formatText(text));
		}
		
		public void e(String text) {
			if (mIsDisabled) return;
			getImpl().e(mTag, formatText(text));
		}
		
		private String formatText(String text) {
			final StringBuilder builder = new StringBuilder();


			if (mClasz != null){
				builder.append("[");
				builder.append(mClasz.getSimpleName());
				builder.append("]");
			}
			
			if (mPrefix != null) {
				builder.append("<");
				builder.append(mPrefix);
				builder.append(">");
			}

			// 1.线程ID改为线程名
			// 2.只有使用了ALogger，日志中才有线程名
			builder.append("(");
			builder.append(Thread.currentThread().getName());
			builder.append(") ");
			
			return builder.append(text).toString();
		}
	}



	/**
	 * 日志接口的实现类（Android日志）
	 */
	public static class AndroidLogcatImpl implements LoggerInterface {

		@Override
		public void v(String tag, String message) {
			Log.v(tag, message);
		}

		@Override
		public void d(String tag, String message) {
			Log.d(tag, message);
		}

		@Override
		public void i(String tag, String message) {
			Log.i(tag, message);
		}

		@Override
		public void w(String tag, String message) {
			Log.w(tag, message);
		}

		@Override
		public void e(String tag, String message) {
			Log.e(tag, message);
		}
	}

	/**
	 * 日志接口
	 */
	public interface LoggerInterface {
		void v(String tag, String message);
		void d(String tag, String message);
		void i(String tag, String message);
		void w(String tag, String message);
		void e(String tag, String message);
	}
}
