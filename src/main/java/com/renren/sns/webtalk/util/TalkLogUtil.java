package com.renren.sns.webtalk.util;

import org.apache.log4j.Logger;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-4
 */
public class TalkLogUtil {
	private static Logger inshion_log = org.apache.log4j.Logger.getLogger("WebtalkLogger");
	private static final int DEBUG = 5;
	private static final int INFO = 4;
	private static final int WARN = 3;
	private static final int ERROR = 2;
	private static final int FATAL = 1;
	
	public static void debug(String message) {
		wt(message,DEBUG);
	}
	public static void info(String message) {
		wt(message,INFO);
	}
	public static void error(String message) {
		wt(message,ERROR);
	}
	public static void fatal(String message) {
		wt(message,FATAL);
	}
	public static void warn(String message) {
		wt(message,WARN);
	}

	private static void wt(String message, int level) {
		String path = InvokeUtil.getInvokingPathForLog();
		message = path + message;
		//System.out.println(message);
		switch(level){
		case DEBUG:
			if(Globals.DEBUG_ENV){
				inshion_log.info("<D>"+message);
			}
			else{
				inshion_log.debug(message);
			}
			break;
		case ERROR:
			inshion_log.error(message);
			break;
		case INFO:
			inshion_log.info(message);
			break;
		case FATAL:
			inshion_log.fatal(message);
			break;
		case WARN:
			inshion_log.warn(message);
			break;
		default:
			inshion_log.debug(message);
		}
	}
	
	public static void main(String[] args) {
		debug("asdf");
	}
}
