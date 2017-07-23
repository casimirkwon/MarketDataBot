package com.koscom.marketdata.bot.command;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koscom.marketdata.bot.BotConfiguration;
import com.koscom.marketdata.bot.api.BotAPICaller;
import com.koscom.marketdata.bot.object.Message;

public class PriceCommand extends DefaultCommand {
	private static Log logger = LogFactory.getLog(PriceCommand.class);

	public PriceCommand(String command) {
		super(command);
	}

	@Override
	public JsonObject handle(JsonObject command) {
		if(!canHandle(command))
			throw new IllegalArgumentException("illegal command");
		
		Message msg = Message.fromJsonString(command.toString(), Message.class);
		
		// processing
		
		String [] params = getCommandParams(command);

		if(params.length == 0) {
			return buildSendMessageReply(msg, "가격을 조회할 종목명을 입력해주세요.");
		}
		
		
		String uri = config.getString(BotConfiguration.KEY_ENDPOINT_MARKETDATA_URL);
		String apikey = config.getString(BotConfiguration.KEY_ENDPOINT_MARKETDATA_APIKEY);
		
		String replyMsg = "";
		
		for(String item : params) {
			logger.info("price command params : " + item);
			
			String issueCode = marketDataMasterHolder.findIssueShortCode(item);
			String market = marketDataMasterHolder.findMarket(item);

			if(issueCode == null || market == null) {
				return buildSendMessageReply(msg, "[" + item + "]의 종목 정보를 찾을 수 없습니다.");
			}
			
			String fetchUrl = uri + "/" + market + "/" + issueCode + "/price?apikey=" + apikey;
			
			JsonObject result = getPriceAsJsonObject(fetchUrl);
			
			if(result == null) {
				return buildSendMessageReply(msg, "[" + item + "]의 가격 정보를 가져올 수 없습니다.");
			}
			
			replyMsg += "입력한 종목 : " + item + "\n";
			replyMsg += "현재가 : " + result.get("trdPrc").getAsString() + "\n";
			replyMsg += "\n";
		}
		
		logger.info("price API uri : " + uri);
		
		
		return buildSendMessageReply(msg, replyMsg);
	}

	private JsonObject getPriceAsJsonObject(String uri) {
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		JsonObject result = null;
		try {
			response =  httpClientManager.executeGet(uri);
			entity = response.getEntity();
			
			if (response.getStatusLine().getStatusCode() == 200) {
				
				String body = IOUtils.toString(entity.getContent(), "UTF-8");
				
				result = new JsonParser().parse(body).getAsJsonObject();
				
				return result.get("result").getAsJsonObject();
			}
		} catch (IOException e) {
			logger.error("error while fetching price." , e);
		} finally {
			try { EntityUtils.consume(entity); } catch (Exception ignore) {}
			try { response.close(); } catch (Exception ignore) {}
		}
		
		return null;
	}
	
}
