package com.renren.sns.webtalk.util;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-4
 */
public class InvokeUtil {
	public static String getInvokingMethodName() {
		String classname = "";
		String method = "";
		try {
			StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
			String classnames[] = stack.getClassName().split("\\.");
			int max = classnames.length - 1;
			classname = classnames[max];
			if(classname != null && classname.indexOf('$')>=0){
			    try {
                    classname = classname.split("$")[0];
                } catch (Exception e) {
                    // TODO: handle exception
                }
			}
			if(classname == null){
				classname = "";
			}
			method = stack.getMethodName();
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return method;
	}
	
	public static String getInvokingPathForLog(){
		String classname = "";
		String method = "";
		//String packagename = "";
		int linenumber = -1;
		try {
			StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
			linenumber = stack.getLineNumber();
			String classnames[] = stack.getClassName().split("\\.");
			int max = classnames.length - 1;
			classname = classnames[max];
			if(classname != null && classname.indexOf('$')>=0){
			    try {
                    classname = classname.split("$")[0];
                } catch (Exception e) {
                    // TODO: handle exception
                }
			}
			if(classname == null){
				classname = "";
			}
			//packagename = stack.getClassName().substring(0,stack.getClassName().indexOf(classname)-1);
			method = stack.getMethodName();
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		String path = classname+"::"+method+"("+linenumber+"):";
		return path;
	}
}
