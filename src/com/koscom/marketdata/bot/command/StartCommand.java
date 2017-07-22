package com.koscom.marketdata.bot.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonObject;
import com.koscom.marketdata.bot.BotConfiguration;
import com.koscom.marketdata.bot.api.BotAPICaller;
import com.koscom.marketdata.bot.io.HttpClientManager;
import com.koscom.marketdata.bot.object.Message;

public class StartCommand extends DefaultCommand {
	private static Log logger = LogFactory.getLog(StartCommand.class);

	private static String START_MESSAGE = config.getString(BotConfiguration.KEY_MESSAGE_START);

	public StartCommand(String command) {
		super(command);
	}
		
	@Override
	public boolean canHandle(JsonObject command) {
		Message msg = Message.fromJsonString(command.toString(), Message.class);
		
		String commandText = msg.getText();
		
		// start command는 /로 시작하는 command가 아닌 경우도 처리한다.
		if(!commandText.startsWith("/")) return true;
		
		return (commandText.startsWith("/" + this.command)) ? true : false;
	}

	@Override
	public JsonObject handle(JsonObject command) {
		if(!canHandle(command))
			throw new IllegalArgumentException("illegal command");
		
		Message msg = Message.fromJsonString(command.toString(), Message.class);
		
		// processing
		
		// build reply
		JsonObject reply = new JsonObject();
		reply.addProperty("method", BotAPICaller.METHOD_SEND_MESSAGE);
		
		JsonObject data = new JsonObject();
		
		data.addProperty("chat_id", msg.getChat().getId());
		data.addProperty("text", START_MESSAGE);
		
		reply.add("data", data);
		
		return reply;
	}
}
