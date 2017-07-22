package com.koscom.marketdata.bot.object;

public class GroupChat extends DefaultObject {
	private long id;
	
	private String title;

	/**
	 * @param id
	 * @param title
	 */
	public GroupChat(long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
