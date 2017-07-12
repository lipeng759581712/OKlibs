package com.max.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.tencent.common.log.TLog;

public class TLogImpl implements ALog.LoggerInterface {

	private Handler mBackHandler;
    private final HandlerThread mBackThread = new HandlerThread("TLog");

    public TLogImpl() {
		initBackThread();
	}

	private void initBackThread() {
		mBackThread.start();

        //等待线程消息循环建立
        synchronized (mBackThread){
            try {
                mBackThread.wait();
            }catch (Exception e){
                Log.e("TLogImpl", e.toString() + "");
            }
        }

		mBackHandler = new Handler(mBackThread.getLooper(), new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				LogMessage logMessage = (LogMessage) msg.obj;
				switch (logMessage.getLevel()) {
					case VERBOSE:
						TLog.v(logMessage.getTag(), logMessage.getMessage());
						break;
					case DEBUG:
						TLog.d(logMessage.getTag(), logMessage.getMessage());
						break;
					case INFO:
						TLog.i(logMessage.getTag(), logMessage.getMessage());
						break;
					case WARN:
						TLog.w(logMessage.getTag(), logMessage.getMessage());
						break;
					case ERROR:
						TLog.e(logMessage.getTag(), logMessage.getMessage());
						break;
					default:
						break;
				}
				return true;
			}
		});
	}

	@Override
	public void v(String tag, String message) {
		if (message == null) {
			message = "";
		}
		Log.v(tag, message);
		mBackHandler.obtainMessage(0, new LogMessage(LogMessage.LogLevel.VERBOSE, tag, message)).sendToTarget();
	}

	@Override
	public void d(String tag, String message) {
		if (message == null) {
			message = "";
		}
		Log.d(tag, message);
		mBackHandler.obtainMessage(0, new LogMessage(LogMessage.LogLevel.DEBUG, tag, message)).sendToTarget();
	}

	@Override
	public void i(String tag, String message) {
		if (message == null) {
			message = "";
		}
		Log.i(tag, message);
		mBackHandler.obtainMessage(0, new LogMessage(LogMessage.LogLevel.INFO, tag, message)).sendToTarget();
	}

	@Override
	public void w(String tag, String message) {
		if (message == null) {
			message = "";
		}
		Log.w(tag, message);
		mBackHandler.obtainMessage(0, new LogMessage(LogMessage.LogLevel.WARN, tag, message)).sendToTarget();
	}

	@Override
	public void e(String tag, String message) {
		if (message == null) {
			message = "";
		}
		Log.e(tag, message);
		mBackHandler.obtainMessage(0, new LogMessage(LogMessage.LogLevel.ERROR, tag, message)).sendToTarget();
	}

	private static class LogMessage {
		public enum LogLevel {
			VERBOSE,
			DEBUG,
			INFO,
			WARN,
			ERROR,
		}

		LogLevel level;
		String tag;
		String message;

		public LogMessage(LogLevel level, String tag, String message) {
			this.level = level;
			this.tag = tag;
			this.message = message;
		}

		public LogLevel getLevel() {
			return level;
		}

		public String getTag() {
			return tag;
		}

		public String getMessage() {
			return message;
		}
	}
}
