package com.koscom.marketdata.bot.api;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map.Entry;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koscom.marketdata.bot.BotConfiguration;
import com.koscom.marketdata.bot.io.HttpClientManager;
import com.koscom.marketdata.bot.mq.MessageQueueManager;
import com.koscom.marketdata.bot.mq.RabbitMQManager;

public class BotAPICaller {
	private static Log logger = LogFactory.getLog(BotAPICaller.class);
	
	public static String METHOD_GET_ME = "getMe";
	public static String METHOD_SEND_MESSAGE = "sendMessage";
	public static String METHOD_FORWARD_MESSAGE = "forwardMessage";
	public static String METHOD_SEND_PHOTO = "sendPhoto";
	public static String METHOD_SEND_AUDIO = "sendAudio";
	public static String METHOD_SEND_DOCUMENT = "sendDocument";
	public static String METHOD_SEND_STICKER = "sendSticker";
	public static String METHOD_SEND_VIDEO = "sendVideo";
	public static String METHOD_SEND_LOCATION = "sendLocation";
	public static String METHOD_SEND_CHAT_ACTION = "sendChatAction";
	public static String METHOD_GET_USER_PROFILE_PHOTOS = "getUserProfilePhotos";
	public static String METHOD_GET_UPDATES = "getUpdates";
	public static String METHOD_SET_WEBHOOK = "setWebhook";
	
	
	private static Configuration config = BotConfiguration.getInstance();
	
	private HttpClientManager httpClientManager;
	
	private String callUrlPrefix;
	
	public BotAPICaller(HttpClientManager httpClientManager, String callUrlPrefix) {
		this.httpClientManager = httpClientManager;
		this.callUrlPrefix = callUrlPrefix;
	}
	
	public BotAPICaller() {
		this(HttpClientManager.getInstance(), 
				config.getString(BotConfiguration.KEY_API_URL_PREFIX) +
				config.getString(BotConfiguration.KEY_API_KEY) +
				"/");
	}

	public String executeAPI( String methodName, JsonObject params ) throws IOException {
		
		String query = "?";

		for(Entry<String,JsonElement> entry : params.entrySet() ) {
			String key = entry.getKey();
			String value = URLEncoder.encode(entry.getValue().getAsString(), "UTF-8");
			
			query += "&" + key + "=" + value;
		 }
		
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		
		try  {
		
			response = httpClientManager.executeGet(callUrlPrefix + methodName + query);
			entity = response.getEntity();
			
			if (response.getStatusLine().getStatusCode() == 200) {
				String body = IOUtils.toString(entity.getContent(), "UTF-8");
				
				return body;
			} else
				throw new RuntimeException("API response code is not normal. status code : " + response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			logger.warn("exception occured while call API.", e);
			throw e;
		} finally {
			try { EntityUtils.consume(entity); } catch (Exception ignore) {}
			try { response.close(); } catch (Exception ignore) {}
		}
	}
}
