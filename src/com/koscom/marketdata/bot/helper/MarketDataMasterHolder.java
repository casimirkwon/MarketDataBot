package com.koscom.marketdata.bot.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koscom.marketdata.bot.BotConfiguration;
import com.koscom.marketdata.bot.io.HttpClientManager;

public class MarketDataMasterHolder {
	private static Log logger = LogFactory.getLog(MarketDataMasterHolder.class);
	
	public static String NAME_ISSUE_LISTS = "isuLists";
	public static String NAME_ISSUE_MARKET = "isuMarket";
	public static String NAME_ISSUE_SHORT_CODE = "isuSrtCd";
	public static String NAME_ISSUE_CODE = "isuCd";
	public static String NAME_ISSUE_KOR_NAME = "isuKorNm";
	public static String NAME_ISSUE_KOR_NAME_ABBREVIATION = "isuKorAbbr";
	
	private List<Map<String, String>> issueList;
	
	private HttpClientManager httpClientManager;
	
	private static MarketDataMasterHolder instance;
	private static Configuration config = BotConfiguration.getInstance();
	
	private MarketDataMasterHolder() {
		this.issueList = new ArrayList<Map<String,String>>();
		this.httpClientManager = HttpClientManager.getInstance();
	}
	
	public synchronized static MarketDataMasterHolder getInstance() {
		if(instance == null) {
			instance = new MarketDataMasterHolder();
			instance.init();
		}
		
		return instance;
	}

	private void init() {
		// kospi, kosdaq 종목 리스트 수신
		String uri = config.getString(BotConfiguration.KEY_ENDPOINT_MARKETDATA_URL);
		String apikey = config.getString(BotConfiguration.KEY_ENDPOINT_MARKETDATA_APIKEY);
		
		// load kospi
		loadIssueList(uri, "kospi", apikey);

		// load kosdaq
		loadIssueList(uri, "kosdaq", apikey);

	}

	private void loadIssueList(String uri, String marketType, String apikey) {
		
		String loadUrl = uri + "/" + marketType + "/lists?apikey=" + apikey;
		
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		
		try  {
			
			response = httpClientManager.executeGet(loadUrl);
			entity = response.getEntity();
			
			if (response.getStatusLine().getStatusCode() == 200) {
				
				String body = IOUtils.toString(entity.getContent(), "UTF-8");
				
				JsonArray jsonArray = new JsonParser().parse(body).getAsJsonObject().get(NAME_ISSUE_LISTS).getAsJsonArray();
				
				if(jsonArray != null) {
					for(JsonElement element : jsonArray) {
						HashMap<String, String> issueInfo = new HashMap<String, String>();
						JsonObject isuObject = element.getAsJsonObject();
						issueInfo.put(NAME_ISSUE_MARKET, marketType);
						issueInfo.put(NAME_ISSUE_CODE, isuObject.get(NAME_ISSUE_CODE).getAsString());
						issueInfo.put(NAME_ISSUE_SHORT_CODE, isuObject.get(NAME_ISSUE_SHORT_CODE).getAsString());
						issueInfo.put(NAME_ISSUE_KOR_NAME, isuObject.get(NAME_ISSUE_KOR_NAME).getAsString());
						issueInfo.put(NAME_ISSUE_KOR_NAME_ABBREVIATION, isuObject.get(NAME_ISSUE_KOR_NAME_ABBREVIATION).getAsString());
						
						this.issueList.add(issueInfo);
					}
				}
			}
		} catch (Exception e) {
				logger.warn("exception occured while receiving message.", e);
		} finally {
			try { EntityUtils.consume(entity); } catch (Exception ignore) {}
			try { response.close(); } catch (Exception ignore) {}
		}
	}

	private String findObj(String searchObj, String dstObjName) {
		for(Map<String,String> value : this.issueList) {
			if(value.containsValue(searchObj)) {
				return value.get(dstObjName);
			}
		}
		return null;
	}
	
	public String findIssueCode(String issueName) {
		return findObj(issueName, NAME_ISSUE_CODE);
	}


	public String findIssueShortCode(String issueName) {
		return findObj(issueName, NAME_ISSUE_SHORT_CODE);
	}

	public String findIssueName(String issueCode) {
		return findObj(issueCode, NAME_ISSUE_KOR_NAME);		
	}
	
	public String findIssueNameAbbr(String issueCode) {
		return findObj(issueCode, NAME_ISSUE_KOR_NAME_ABBREVIATION);		
	}
	
	public String findMarket(String issueCodeOrName) {
		return findObj(issueCodeOrName, NAME_ISSUE_MARKET);		
	}
}

