package com.koscom.marketdata.bot.object;

public class Update extends DefaultObject {

	private long updateId;
	
	private Message message;
	
	/**
	 * @param updateId
	 * @param message
	 */
	public Update(long updateId, Message message) {
		super();
		this.updateId = updateId;
		this.message = message;
	}


	public long getUpdateId() {
		return updateId;
	}


	public void setUpdateId(long updateId) {
		this.updateId = updateId;
	}


	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}
}
