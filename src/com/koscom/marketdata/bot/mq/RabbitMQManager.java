package com.koscom.marketdata.bot.mq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMQManager implements MessageQueueManager {
	private static Log logger = LogFactory.getLog(RabbitMQManager.class);

	private static Map<String, MessageQueueManager> instanceMap = new HashMap<String, MessageQueueManager>();

	private String queueName;
	private Connection connection;
	private Map<String, Channel> channelMap;

	public static synchronized MessageQueueManager getInstance(String queueName) {
		if (!instanceMap.containsKey(queueName)) {
			
			MessageQueueManager instance = new RabbitMQManager(queueName);
			
			try {
				instance.init();
			} catch (Exception e) {
				logger.error(e);
			}
			
			instanceMap.put(queueName, instance);
		}

		return instanceMap.get(queueName);
	}
	
	private RabbitMQManager(String queueName) {
		this.queueName = queueName;
	}

	@Override
	public void init() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();

		channelMap = new HashMap<String, Channel>();
	}

	@Override
	public void destroy() throws Exception {
		for (Channel channel : channelMap.values()) {
			channel.close();
		}

		if (connection != null)
			connection.close();
	}

	@Override
	public void send(Object msg) throws Exception {
		
			logger.debug("send message : " + msg);
			try {
				byte[] msgBytes = JsonMessageConverter.toByteArray((String)msg);
				getChannel(false).basicPublish("", queueName, null, msgBytes);
			} catch (ShutdownSignalException e ) {
				logger.error("channel may be shutdown. throwing interrupted exception...");
				throw new InterruptedException("channel may be shutdown. throwing interrupted exception...");
			} catch (Exception e) {
				logger.error(e);
				throw e;
			}
	}

	@Override
	public Object receiveWait() throws Exception {
		String msg = null;
		String rawMsg = null;
		try {
			Consumer consumer = getChannel(true).getDefaultConsumer();

			QueueingConsumer.Delivery delivery = ((QueueingConsumer) consumer).nextDelivery();
			rawMsg = new String(delivery.getBody());
			msg = JsonMessageConverter.fromByteArray(delivery.getBody());

			logger.debug("received message : " + msg);
			return msg;

		} catch (ShutdownSignalException e) {
			logger.error("channel may be shutdown. throwing interrupted exception...");
			throw new InterruptedException(
					"channel may be shutdown. throwing interrupted exception...");
		} catch (Exception e) {
			logger.info("[" +Thread.currentThread().getName() + "] received message (raw) : " + rawMsg);
			logger.error("[" +Thread.currentThread().getName() + "] error", e);
			throw e;
		}
	}
	

	@Override
	public void confirm() throws Exception {
		// do nothing
		
	}

	private Channel getChannel(boolean bind) throws IOException {
		String threadName = Thread.currentThread().getName();

		logger.debug("threadName : " + threadName);

		if (channelMap.containsKey(threadName))
			return channelMap.get(threadName);

		// 신규 채널을 생성
		Channel channel = null;
		try {
			channel = connection.createChannel();

			channel.queueDeclare(queueName, true, false, false, null);

			if (bind) {
				channel.basicQos(1);
				QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.setDefaultConsumer(consumer);
				channel.basicConsume(queueName, true, consumer);
			}

			channelMap.put(threadName, channel);

		} catch (RuntimeException e) {
			logger.error(e);
			throw e;
		} catch (IOException e) {
			logger.error(e);
			throw e;
		}

		return channel;
	}

}
