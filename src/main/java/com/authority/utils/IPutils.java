package com.authority.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPutils {

	/**
	 * 判断是否为合法的ip地址
	 * @param ipAddress
	 * @return
	 */
	public static boolean checkIp(String ipAddress) {
		String reg = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(reg);  
        Matcher matcher = pattern.matcher(ipAddress);  
        return matcher.matches();  
	}
	
	public static boolean checkPort(String port) {
		String reg = "^[0-9]*[1-9][0-9]*$";
		Pattern pattern = Pattern.compile(reg);  
        Matcher matcher = pattern.matcher(port);  
        return matcher.matches();  
	}
	
	public static void main(String[] args) {
		System.out.println(checkPort("3306"));
	}
}
