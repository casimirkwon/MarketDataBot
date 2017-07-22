package com.koscom.marketdata.bot.object;

public class ForceReply extends DefaultObject {
	
	private boolean forceReply;
	
	private boolean selective;

	/**
	 * @param forceReply
	 * @param selective
	 */
	public ForceReply(boolean forceReply, boolean selective) {
		super();
		this.forceReply = forceReply;
		this.selective = selective;
	}

	public boolean isForceReply() {
		return forceReply;
	}

	public void setForceReply(boolean forceReply) {
		this.forceReply = forceReply;
	}

	public boolean isSelective() {
		return selective;
	}

	public void setSelective(boolean selective) {
		this.selective = selective;
	}
	
	
}
