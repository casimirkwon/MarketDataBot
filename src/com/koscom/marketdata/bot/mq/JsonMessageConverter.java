package com.koscom.marketdata.bot.mq;


public class JsonMessageConverter {
	public static  byte [] toByteArray(String msg) throws Exception {
		return toByteArray(msg, "UTF-8");
	}
	
	public static byte [] toByteArray(String msg, String encoding) throws Exception {
		return msg.getBytes(encoding);
	}
	
	public static String fromByteArray(byte [] msg) throws Exception {
		return fromByteArray(msg, "UTF-8");
	}
	
	public static String fromByteArray(byte [] msg, String encoding) throws Exception {
		return new String(msg, encoding);
	}
}
