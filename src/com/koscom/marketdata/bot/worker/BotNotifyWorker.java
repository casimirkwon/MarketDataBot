package com.koscom.marketdata.bot.worker;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koscom.marketdata.bot.api.BotAPICaller;
import com.koscom.marketdata.bot.command.BotCommand;
import com.koscom.marketdata.bot.command.BotCommandHandler;
import com.koscom.marketdata.bot.mq.DefaultQueueManager;
import com.koscom.marketdata.bot.mq.MessageQueueManager;
import com.koscom.marketdata.bot.mq.RabbitMQManager;

public class BotNotifyWorker extends Thread implements Worker {
	private static Log logger = LogFactory.getLog(BotNotifyWorker.class);
	private MessageQueueManager mqManager;
	
	private BotAPICaller apiCaller;
	
	private BotCommandHandler commandHandler;
	
	private boolean inited = false;

	public BotNotifyWorker() {
	}
	
	synchronized public void init() {
		// XXX : factory 제공 또는 spring f/w 사용 등으로..
		if( mqManager == null)
			//mqManager = RabbitMQManager.getInstance(MessageQueueManager.QUEUE_NAME_FOR_NOTIFY);
			mqManager = DefaultQueueManager.getNotifyQueueManager();
		
		if(apiCaller == null)
			apiCaller = new BotAPICaller();
		
		if(commandHandler == null)
			commandHandler = BotCommandHandler.getInstance();
		
		inited = false;
	}
	
	public boolean isInited() {
		return inited;
	}

	public void run() {
		if(!inited) init();
		
		while (true) {
			
			if ( Thread.currentThread().isInterrupted() ) {
				logger.info("thread interrupted. stop working... : " + Thread.currentThread().getName());
				break;
			}
			
			try {
				String msg = (String) mqManager.receiveWait();
				logger.debug("received notify : " + msg);
				
				JsonObject msgObject = new JsonParser().parse(msg).getAsJsonObject();

				// build reply
				JsonObject reply = new JsonObject();
				reply.addProperty("method", BotAPICaller.METHOD_SEND_MESSAGE);
				
				JsonObject data = new JsonObject();
				
				data.addProperty("chat_id", msgObject.get("id").getAsInt());
				data.addProperty("text", msgObject.get("text").getAsString());
				
				reply.add("data", data);
				
				logger.info("notifying message built : " + reply);
					
				String response = apiCaller.executeAPI(reply.get("method").getAsString(), reply.getAsJsonObject("data"));
					
				logger.info("API call result : " + response);
			} catch (InterruptedException e) {
				logger.info("thread interrupted. stop working... : " + Thread.currentThread().getName());
				break;
			} catch(IllegalArgumentException e) {
				logger.warn("wrong command argument", e);
			} catch (Exception e) {
				logger.error("error occured while processing received message", e);
			}
		}
	}

}
