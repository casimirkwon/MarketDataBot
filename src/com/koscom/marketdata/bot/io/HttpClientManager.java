package com.koscom.marketdata.bot.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import javax.net.ssl.SSLContext;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import com.koscom.marketdata.bot.BotConfiguration;

public class HttpClientManager {
	private static Log logger = LogFactory.getLog(HttpClientManager.class);
	
	private static Configuration config = BotConfiguration.getInstance();

	private static boolean USE_PROXY = config.getBoolean(BotConfiguration.KEY_PROXY_USE);
	private static String PROXY_IP = config.getString(BotConfiguration.KEY_PROXY_IP);
	private static int PROXY_PORT = config.getInt(BotConfiguration.KEY_PROXY_PORT);
	
	private static HttpClientManager instance;
	
	private HttpClientContext proxyContext;
	private CloseableHttpClient httpClient;
	private String proxyIp;
	private int proxyPort;
	private boolean useProxy;
	

	private HttpClientManager() {
		this(PROXY_IP, PROXY_PORT, USE_PROXY);
	}
	
	private HttpClientManager(String proxyIp, int proxyPort, boolean useProxy) {
		this.proxyIp = proxyIp;
		this.proxyPort = proxyPort;
		this.useProxy = useProxy;
		
		if(useProxy ) {
			logger.info("use proxy : " + this.useProxy);
			logger.info("socks proxy ip addr: " + this.proxyIp);
			logger.info("socks proxy port: " + this.proxyPort);
			proxyContext = HttpClientContext.create();
			InetSocketAddress socksaddr = new InetSocketAddress(this.proxyIp,
					this.proxyPort);
			proxyContext.setAttribute("socks.address", socksaddr);
		}
		
		makeHttpClient();
	}

	synchronized public static  HttpClientManager getInstance(String proxyIp, int proxyPort, boolean useProxy) {
		if(instance == null)
			instance = new HttpClientManager(proxyIp, proxyPort, useProxy);
		
		return instance;
	}

	synchronized public static  HttpClientManager getInstance() {
		return getInstance(PROXY_IP, PROXY_PORT, USE_PROXY);
	}

	private void makeHttpClient() {
		if (useProxy) {
			Registry<ConnectionSocketFactory> reg = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http", new MyConnectionSocketFactory())
					.register("https", new MySSLConnectionSocketFactory(SSLContexts.createSystemDefault())).build();
			
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
			cm.setDefaultMaxPerRoute(10);
			cm.setMaxTotal(100);
			httpClient = HttpClients.custom().setConnectionManager(cm).build();
		} else {
			httpClient = HttpClients.custom().setMaxConnPerRoute(10).setMaxConnTotal(100).build();
		}
	}
	
	public CloseableHttpResponse executeGet(String uri) throws IOException {
		HttpGet httpGet = new HttpGet(uri);
		
		logger.debug("uri = " + uri);
		if(useProxy)
			return httpClient.execute(httpGet, proxyContext);
		else 
			return httpClient.execute(httpGet);
	}
	
	public void destroy() {
		try {
			httpClient.close();
			httpClient = null;
		} catch (IOException e) {
			logger.warn(e);
		}
	}	

	class MyConnectionSocketFactory extends PlainConnectionSocketFactory {

		@Override
		public Socket createSocket(HttpContext context) throws IOException {
			InetSocketAddress socksaddr = (InetSocketAddress) context
					.getAttribute("socks.address");
			Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
			return new Socket(proxy);
		}
	}
	
	class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {

		
		public MySSLConnectionSocketFactory(final SSLContext sslContext) {
			super(sslContext);
		}

		@Override
		public Socket createSocket(final HttpContext context)
				throws IOException {
			InetSocketAddress socksaddr = (InetSocketAddress) context
					.getAttribute("socks.address");
			Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
			return new Socket(proxy);
		}

	}
}
