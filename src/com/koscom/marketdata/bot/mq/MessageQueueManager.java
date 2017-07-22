package com.koscom.marketdata.bot.mq;

public interface MessageQueueManager {
	public static final String QUEUE_NAME_FOR_MESSAGE = "message_queue";
	public static final String QUEUE_NAME_FOR_NOTIFY = "notify_queue";
	
	public void init() throws Exception;
	
	public void destroy() throws Exception;
	
	public void send(Object msg) throws Exception;
	
	public Object receiveWait() throws Exception;
	
	public void confirm() throws Exception;
}
