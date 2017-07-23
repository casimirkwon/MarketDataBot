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

public class BotCommandWorker extends Thread implements Worker {
	private static Log logger = LogFactory.getLog(BotCommandWorker.class);
	private MessageQueueManager mqManager;
	
	private BotAPICaller apiCaller;
	
	private BotCommandHandler commandHandler;
	
	private boolean inited = false;

	public BotCommandWorker() {
	}
	
	synchronized public void init() {
		if( mqManager == null)
//			mqManager = RabbitMQManager.getInstance(MessageQueueManager.QUEUE_NAME_FOR_MESSAGE);
			mqManager = DefaultQueueManager.getReceiveQueueManager();
		
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
				logger.info("received message : " + msg);
				
				JsonObject msgObject = new JsonParser().parse(msg).getAsJsonObject();
				
				List<BotCommand> commandList = commandHandler.canHandleThisCommand(msgObject);

				for(BotCommand command : commandList) {
					JsonObject reply = command.handle(msgObject);
					
					logger.info("command processing result : " + reply);
					
					String response = apiCaller.executeAPI(reply.get("method").getAsString(), reply.getAsJsonObject("data"));
					
					logger.info("API call result : " + response);
				}
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
