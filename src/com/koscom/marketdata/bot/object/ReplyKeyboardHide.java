package com.koscom.marketdata.bot.object;

public class ReplyKeyboardHide extends DefaultObject {
	
	private boolean hideKeyboard;
	
	private boolean selective;

	/**
	 * @param hideKeyboard
	 * @param selective
	 */
	public ReplyKeyboardHide(boolean hideKeyboard, boolean selective) {
		super();
		this.hideKeyboard = hideKeyboard;
		this.selective = selective;
	}

	public boolean isHideKeyboard() {
		return hideKeyboard;
	}

	public void setHideKeyboard(boolean hideKeyboard) {
		this.hideKeyboard = hideKeyboard;
	}

	public boolean isSelective() {
		return selective;
	}

	public void setSelective(boolean selective) {
		this.selective = selective;
	}
	
	
}
