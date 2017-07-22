package com.koscom.marketdata.bot.command;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonObject;
import com.koscom.marketdata.bot.BotConfiguration;
import com.koscom.marketdata.bot.api.BotAPICaller;
import com.koscom.marketdata.bot.helper.MarketDataMasterHolder;
import com.koscom.marketdata.bot.io.HttpClientManager;
import com.koscom.marketdata.bot.object.Message;

public abstract class DefaultCommand implements BotCommand {
	private static Log logger = LogFactory.getLog(DefaultCommand.class);

	protected static Configuration config = BotConfiguration.getInstance();
	
	protected String command;
	
	protected HttpClientManager httpClientManager;

	protected MarketDataMasterHolder marketDataMasterHolder;

	public DefaultCommand(String command) {
		this.command = command;
		
		this.httpClientManager =HttpClientManager.getInstance();
		
		this.marketDataMasterHolder = MarketDataMasterHolder.getInstance();
	}
	
	public String [] getCommandParams(JsonObject command) {
		if(!canHandle(command))
			throw new IllegalArgumentException("illegal command");
		
		Message msg = Message.fromJsonString(command.toString(), Message.class);
		
		String commandText = msg.getText();
		
		if(!commandText.startsWith("/") || commandText.indexOf(' ') < 0) return new String[0];
		
		String paramStr = commandText.substring(commandText.indexOf(' ') + 1);
		
		String [] params = paramStr.split(" ");
		
		return params;
	}
	
	@Override
	public boolean canHandle(JsonObject command) {
		Message msg = Message.fromJsonString(command.toString(), Message.class);
		
		String commandText = msg.getText();
		
		if(!commandText.startsWith("/")) return false;
		
		return (commandText.startsWith("/" + this.command)) ? true : false;
	}

	@Override
	public boolean equals(Object obj) {
		if( ! (obj instanceof BotCommand) ) return false;
		BotCommand targetCmd = (BotCommand)obj;
		return this.command.equalsIgnoreCase(targetCmd.getCommand());
	}

	@Override
	public String getCommand() {
		return command;
	}
	

	protected JsonObject buildSendMessageReply(Message msg, String text) {
		// build reply
		JsonObject reply = new JsonObject();
		reply.addProperty("method", BotAPICaller.METHOD_SEND_MESSAGE);
		
		JsonObject data = new JsonObject();
		
		data.addProperty("chat_id", msg.getChat().getId());
		data.addProperty("text", text);
		
		reply.add("data", data);
		
		return reply;
	}

}
