package com.koscom.marketdata.bot.mq;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultQueueManager implements MessageQueueManager {
	private static Log logger = LogFactory.getLog(DefaultQueueManager.class);
	
	private static DefaultQueueManager receiveQueueManager;
	
	private static DefaultQueueManager notifyQueueManager;
	
	private DefaultQueueManager() {	}
	
	synchronized public static DefaultQueueManager getReceiveQueueManager() {
		if(receiveQueueManager == null) {
			receiveQueueManager = new DefaultQueueManager();
			try {
				receiveQueueManager.init();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return receiveQueueManager;
	}
	
	synchronized public static DefaultQueueManager getNotifyQueueManager() {
		if(notifyQueueManager == null) {
			notifyQueueManager = new DefaultQueueManager();
			try {
				notifyQueueManager.init();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return notifyQueueManager;
	}
	
	private LinkedBlockingQueue<Object> queue;
	
	@Override
	public void init() throws Exception {
		this.queue = new LinkedBlockingQueue<Object>();

	}

	@Override
	public void destroy() throws Exception {
		this.queue.clear();
		this.queue = null;

	}

	@Override
	public void send(Object msg) throws Exception {
		this.queue.put(msg);

	}

	@Override
	public Object receiveWait() throws Exception {
		return queue.take();
	}

	@Override
	public void confirm() throws Exception {
		// do nothing

	}

}
