package com.koscom.marketdata.bot.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonObject;

public class BotCommandHandler {
	private static Log logger = LogFactory.getLog(BotCommandHandler.class);
	
	private ArrayList<BotCommand> commandList;
	
	private static BotCommandHandler instance;
	
	private BotCommandHandler() {
		commandList = new ArrayList<BotCommand>();
	}
	
	synchronized public static BotCommandHandler getInstance() {
		if(instance == null)
			instance = new BotCommandHandler();
		
		return instance;
	}
	
	synchronized public void register(BotCommand command) {
		if(!commandList.contains(command))
			commandList.add(command);
		
		logger.info("command ( command : " + command.getCommand() + ", class : " + command.getClass().getName() + " ) registered.");
	}
	
	synchronized public void unregister(BotCommand command) {
		if(commandList.contains(command))
			commandList.remove(command);
	}
	
	synchronized public void unregisterAll() {
		commandList.clear();
	}

	public BotCommand get(String command) {
		for(BotCommand item : commandList) {
			if(item.getCommand().equalsIgnoreCase(command)) return item;
		}
		
		return null;
	}
	
	public BotCommand get(int index) {
		if(index >= commandList.size() ) return null;
		return commandList.get(index);
	}
	
	public List<BotCommand> canHandleThisCommand(JsonObject command) {
		List<BotCommand> ret = new ArrayList<BotCommand>();
		
		for(BotCommand item : commandList) {
			if(item.canHandle(command)) ret.add(item);
		}
		
		return ret;
	}
}
