package com.koscom.marketdata.bot.notifier;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BotNotifierManager {
	private static Log logger = LogFactory.getLog(BotNotifierManager.class);
	
	private ArrayList<BotNotifier> notifierList;
	
	private static BotNotifierManager instance;
	
	private BotNotifierManager() {
		notifierList = new ArrayList<BotNotifier>();
	}
	
	synchronized public static BotNotifierManager getInstance() {
		if(instance == null)
			instance = new BotNotifierManager();
		
		return instance;
	}
	
	synchronized public void register(BotNotifier notifier) {
		if(!notifierList.contains(notifier))
			notifierList.add(notifier);
		
		logger.info("notify ( id : " + notifier.getNotifyerId() + ", class : " + notifier.getClass().getName() + " ) registered.");
	}
	
	synchronized public void unregister(BotNotifier notify) {
		if(notifierList.contains(notify))
			notifierList.remove(notify);
	}
	
	synchronized public void unregisterAll() {
		notifierList.clear();
	}

	public BotNotifier get(String notiferId) {
		for(BotNotifier item : notifierList) {
			if(item.getNotifyerId().equalsIgnoreCase(notiferId)) return item;
		}
		
		return null;
	}
	
	public BotNotifier get(int index) {
		if(index >= notifierList.size() ) return null;
		return notifierList.get(index);
	}
	
	public void start() {
		logger.info("starting notifier threads...");

		for(BotNotifier notifier : notifierList) {
			notifier.start();
		}
	}
	
	public void stop() {
		
		logger.info("interrupting notifier threads...");
		
		for(BotNotifier notifier : notifierList) {
			try { notifier.interrupt(); } catch (Exception e) { logger.error(e); }
		}
		
		notifierList.clear();
		notifierList = null;
	}
}
