package com.koscom.marketdata.bot;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.koscom.marketdata.bot.api.BotMessageReceiver;
import com.koscom.marketdata.bot.command.BotCommandHandler;
import com.koscom.marketdata.bot.command.CapitalCommand;
import com.koscom.marketdata.bot.command.PriceCommand;
import com.koscom.marketdata.bot.command.StartCommand;
import com.koscom.marketdata.bot.mq.DefaultQueueManager;
import com.koscom.marketdata.bot.mq.MessageQueueManager;
import com.koscom.marketdata.bot.mq.RabbitMQManager;
import com.koscom.marketdata.bot.notifier.BotNotifierManager;
import com.koscom.marketdata.bot.worker.BotCommandWorker;
import com.koscom.marketdata.bot.worker.BotNotifyWorker;
import com.koscom.marketdata.bot.worker.WorkerPool;

public class TelegramBot {
	private static Log logger = LogFactory.getLog(TelegramBot.class);
	
	private static Configuration config = BotConfiguration.getInstance();
	
	private static BotCommandHandler commandHandler;
	
	private static BotNotifierManager notifierManager;
	
	private static BotMessageReceiver msgReceiver;
	
	private static WorkerPool<BotCommandWorker> commandWorkerPool;

	private static WorkerPool<BotNotifyWorker> notifyWorkerPool;

	public static void main(String [] args) {
		// command handler init
		commandHandler = BotCommandHandler.getInstance();
		
		// register commands
		commandHandler.register(new PriceCommand("price"));
		commandHandler.register(new StartCommand("start"));
		// TODO [실습 4-01] 입력 종목 기업의 자본금 정보를 가져오는 CapitalCommand를 작성하여 추가한다.
		commandHandler.register(new CapitalCommand("capital"));
		
		// notifier manager init
		notifierManager = BotNotifierManager.getInstance();
		
		// register notifier
		
		// start notifiers
		notifierManager.start();
		
		// command worker pool init
		commandWorkerPool = new WorkerPool<BotCommandWorker>(BotCommandWorker.class, 10);
		
		// init
		try {
			commandWorkerPool.init();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error(e);
		}
		
		// start worker thread
		commandWorkerPool.start();
		
		// notify worker pool init
		notifyWorkerPool = new WorkerPool<BotNotifyWorker>(BotNotifyWorker.class, 10);
		
		// init
		try {
			notifyWorkerPool.init();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error(e);
		}
		
		// start worker thread
		notifyWorkerPool.start();
		
		// message updater (receiver) init
		msgReceiver = BotMessageReceiver.getInstance();
		
		// message updater start
		msgReceiver.startReceive();
	}
	
	private static void stopBot() {
		try {
			msgReceiver.stopDispatch();
			commandWorkerPool.destroy();
			notifierManager.stop();
//			MessageQueueManager messageMqManager = RabbitMQManager.getInstance(MessageQueueManager.QUEUE_NAME_FOR_MESSAGE);
			MessageQueueManager messageMqManager = DefaultQueueManager.getReceiveQueueManager();
			messageMqManager.destroy();
//			MessageQueueManager notifyMqManager = RabbitMQManager.getInstance(MessageQueueManager.QUEUE_NAME_FOR_NOTIFY);
			MessageQueueManager notifyMqManager = DefaultQueueManager.getNotifyQueueManager();
			notifyMqManager.destroy();
		} catch(Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
}
