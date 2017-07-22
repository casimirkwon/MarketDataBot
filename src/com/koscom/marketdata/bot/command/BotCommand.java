package com.koscom.marketdata.bot.command;

import com.google.gson.JsonObject;

public interface BotCommand {

	public boolean canHandle(JsonObject command);
	
	public JsonObject handle(JsonObject command);
	
	public String getCommand();
}
