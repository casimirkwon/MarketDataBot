package com.koscom.marketdata.bot.api;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.reflect.TypeToken;
import com.koscom.marketdata.bot.BotConfiguration;
import com.koscom.marketdata.bot.io.HttpClientManager;
import com.koscom.marketdata.bot.mq.DefaultQueueManager;
import com.koscom.marketdata.bot.mq.MessageQueueManager;
import com.koscom.marketdata.bot.mq.RabbitMQManager;
import com.koscom.marketdata.bot.object.Result;
import com.koscom.marketdata.bot.object.Update;

public class BotMessageReceiver extends Thread {
	private static Log logger = LogFactory.getLog(BotMessageReceiver.class);
	
	private static Configuration config = BotConfiguration.getInstance();
	private static BotMessageReceiver instance;
	
	private MessageQueueManager mqManager;
	private HttpClientManager httpClientManager;
	
	private String dispatchUrl;
	private long offset = -1;
	private int limit = -1;
	private int timeout = -1;
	
	private boolean alive;
	
	
	/**
	 * @param dispatchUrl
	 * @param limit
	 * @param timeout
	 */
	private BotMessageReceiver(HttpClientManager clientFactory, String dispatchUrl, int limit, int timeout) {
		this.httpClientManager = clientFactory;
		this.dispatchUrl = dispatchUrl;
		this.limit = limit;
		this.timeout = timeout;
		this.offset = 0;
		
		this.alive = false;
		
		// mqManager = RabbitMQManager.getInstance(MessageQueueManager.QUEUE_NAME_FOR_MESSAGE);
		mqManager = DefaultQueueManager.getReceiveQueueManager();
	}

	synchronized public static BotMessageReceiver getInstance() {
		return getInstance(HttpClientManager.getInstance(),
				config.getString(BotConfiguration.KEY_API_URL_PREFIX) +
				config.getString(BotConfiguration.KEY_API_KEY) +
				"/getUpdates",
				config.getInt(BotConfiguration.KEY_API_UPDATE_LIMIT), 
				config.getInt(BotConfiguration.KEY_API_UPDATE_TIMEOUT));
	}
	
	synchronized public static BotMessageReceiver getInstance(HttpClientManager clientFactory, 
			String dispatchUrl, 
			int limit, 
			int timeout) {
		if( instance == null ) {
			instance = new BotMessageReceiver(clientFactory, dispatchUrl, limit, timeout);
			instance.setName("message dispatcher thread");
		}
		
		return instance;
	}
	
	public void startReceive() {
		if(instance == null) return;
		
		logger.info("starting message receiver...");
		instance.start();
		
		alive = true;
		logger.info("message receiver started");
	}
	
	public void stopDispatch() {
		if(instance == null) return;
		
		logger.info("stopping message receiver...");
		instance.interrupt();

		alive = false;
		logger.info("message receiver stopped");
	}

	public void run() {

		try {
			//JSONParser parser = new JSONParser();
			
			while( true ) {
				
				if ( instance.isInterrupted() ) {
					logger.info("dispatcher thread interrupted. stop dispatching...");
					break;
				}
				
				String query = "";
				query += (offset >= 0 ) ?  "&offset=" + offset : "" ;
				query += (limit >= 0 ) ?  "&limit=" + limit : "" ;
				query += (timeout >= 0 ) ?  "&timeout=" + timeout : "" ;
				
				if(query != null) query = "?" + query;
				
				logger.debug("request uri : " + dispatchUrl + query);
	
				CloseableHttpResponse response = null;
				HttpEntity entity = null;
				
				try  {
					
					response = httpClientManager.executeGet(dispatchUrl + query);
					entity = response.getEntity();
					
					if (response.getStatusLine().getStatusCode() == 200) {
						
						String body = IOUtils.toString(entity.getContent(), "UTF-8");
						
						// json response parsing
						TypeToken<Result<List<Update>>> resultTypeToken = new TypeToken<Result<List<Update>>>() {};
						Result<List<Update>> result = Result.fromJsonString(body, resultTypeToken.getType());
						
						logger.debug(result);
						
						// 응답 코드 체크 
						if(!result.isOk()) {
							logger.error("response is not ok : " + result);
							continue;
						}
						
						List<Update> updates = result.getResult();

						// 데이터가 없으면 pass 
						if(updates.size() == 0) {
							logger.debug("there is no updates. wait for next...");
							continue;
						}

						// 수신된 메시지를 message queue에 각각 담는다.
						try {
							Iterator<Update> itr = updates.iterator();
							while(itr.hasNext()) {
								Update update = itr.next();
								
								// worker에게 메세지 전달
								sendMessage(update.getMessage().toJsonString());
								
								// 메시지 전송 후 offset update 처리 (다음 fetch를 위해)
								updateOffset(update.getUpdateId());
							}
						} catch (InterruptedException e) {
							logger.info("thread interrupted. stop dispatching... : " + Thread.currentThread().getName());
							break;
						} catch (Exception e) {
							logger.error(e);
						}
					}
				} catch (Exception e) {
						logger.warn("exception occured while receiving message.", e);
				} finally {
					try { EntityUtils.consume(entity); } catch (Exception ignore) {}
					try { response.close(); } catch (Exception ignore) {}
				}
			}
		} finally {
		}
	}

	public String getDispatchUrl() {
		return dispatchUrl;
	}

	public void setDispatchUrl(String dispatchUrl) {
		this.dispatchUrl = dispatchUrl;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	private void updateOffset(long receivedOffset) {
		if(receivedOffset < this.offset) {
			logger.warn("received offset is less than current offset."
					+ " receivedOffset : " + receivedOffset 
					+ " currentOffset : " + this.offset);
			return;
		}
		
		// offset update
		this.offset = receivedOffset + 1;
	}
	
	private void sendMessage(String msg) throws Exception {
		mqManager.send(msg);
	}
}
