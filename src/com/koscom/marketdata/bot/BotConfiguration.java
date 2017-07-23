package com.koscom.marketdata.bot;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BotConfiguration {
	private static Log logger = LogFactory.getLog(BotConfiguration.class);
	
//	private static String CONFIG_FILE = "bot_config.properties";
	private static String CONFIG_FILE = "bot_config.xml";
	private static Configuration config;
	private static boolean inited;
	
	public static String KEY_API_URL_PREFIX = "bot.api.url_prefix";
	public static String KEY_API_KEY = "bot.api.key";
	
	public static String KEY_API_UPDATE_LIMIT = "bot.api.update.limit";
	public static String KEY_API_UPDATE_TIMEOUT = "bot.api.update.timeout";

	public static String KEY_ENDPOINT_MARKETDATA_URL = "bot.endpoint.marketdata.url";
	public static String KEY_ENDPOINT_MARKETDATA_APIKEY = "bot.endpoint.marketdata.apikey";

	public static String KEY_ENDPOINT_CORPINFO_URL = "bot.endpoint.corpinfo.url";
	public static String KEY_ENDPOINT_CORPINFO_APIKEY = "bot.endpoint.corpinfo.apikey";

	public static String KEY_PROXY_USE = "bot.proxy.use";
	public static String KEY_PROXY_IP = "bot.proxy.ipaddr";
	public static String KEY_PROXY_PORT = "bot.proxy.port";
	
	
	public static String KEY_DB_CONN_URL = "bot.db.conn_url";
	public static String KEY_DB_DEFAULT_DB = "bot.db.default_db";
	
	public static String KEY_MESSAGE_START = "bot.messages.start";
	public static String KEY_MESSAGE_REGISTER_SUCCESS = "bot.messages.register_success";
	public static String KEY_MESSAGE_UNREGISTER_SUCCESS = "bot.messages.unregister_success";
	
	synchronized private static void init() {
		if(inited) return;
		
		try {
			logger.info("initializing configuration : " + CONFIG_FILE);
			//config = new PropertiesConfiguration(CONFIG_FILE);
			config = new XMLConfiguration(CONFIG_FILE);
			inited = true;
		} catch (ConfigurationException e) {
			logger.error(e);
		}
	}
	
	public static Configuration getInstance() {
		if(!inited) init();
		return config;
	}
}
