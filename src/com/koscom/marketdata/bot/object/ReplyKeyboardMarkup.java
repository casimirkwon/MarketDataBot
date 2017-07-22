package com.koscom.marketdata.bot.object;

import java.util.List;

public class ReplyKeyboardMarkup extends DefaultObject {
	
	private List<List<String>> keyboard;
	
	private boolean resizeKeyboard;
	
	private boolean oneTimeKeyboard;
	
	private boolean selective;

	/**
	 * @param keyboard
	 * @param resizeKeyboard
	 * @param oneTimeKeyboard
	 * @param selective
	 */
	public ReplyKeyboardMarkup(List<List<String>> keyboard,
			boolean resizeKeyboard, boolean oneTimeKeyboard, boolean selective) {
		super();
		this.keyboard = keyboard;
		this.resizeKeyboard = resizeKeyboard;
		this.oneTimeKeyboard = oneTimeKeyboard;
		this.selective = selective;
	}

	public List<List<String>> getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(List<List<String>> keyboard) {
		this.keyboard = keyboard;
	}

	public boolean isResizeKeyboard() {
		return resizeKeyboard;
	}

	public void setResizeKeyboard(boolean resizeKeyboard) {
		this.resizeKeyboard = resizeKeyboard;
	}

	public boolean isOneTimeKeyboard() {
		return oneTimeKeyboard;
	}

	public void setOneTimeKeyboard(boolean oneTimeKeyboard) {
		this.oneTimeKeyboard = oneTimeKeyboard;
	}

	public boolean isSelective() {
		return selective;
	}

	public void setSelective(boolean selective) {
		this.selective = selective;
	}
	
	
}
