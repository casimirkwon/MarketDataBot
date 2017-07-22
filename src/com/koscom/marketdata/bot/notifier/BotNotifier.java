package com.koscom.marketdata.bot.notifier;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonObject;
import com.koscom.marketdata.bot.BotConfiguration;
import com.koscom.marketdata.bot.io.HttpClientManager;


public abstract class BotNotifier extends Thread {
	private static Log logger = LogFactory.getLog(BotNotifier.class);
	
	protected static Configuration config = BotConfiguration.getInstance();
	
	private String notifyerId;
	
	protected HttpClientManager httpClientManager;
	
	public BotNotifier(String id) {
		this.notifyerId = id;
		this.setName(id);
		
		this.httpClientManager = this.httpClientManager =HttpClientManager.getInstance();
	}
	
	public String getNotifyerId() {
		return this.notifyerId;
	}
	
	public abstract JsonObject processNotify();
}
