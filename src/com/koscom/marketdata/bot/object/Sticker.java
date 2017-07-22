package com.koscom.marketdata.bot.object;

public class Sticker extends DefaultObject {

	private String fileId;
	
	private long width;
	
	private long height;
	
	private PhotoSize thumb;
	
	private long fileSize;

	/**
	 * @param fileId
	 * @param width
	 * @param height
	 * @param thumb
	 * @param fileSize
	 */
	public Sticker(String fileId, long width, long height, PhotoSize thumb,
			long fileSize) {
		super();
		this.fileId = fileId;
		this.width = width;
		this.height = height;
		this.thumb = thumb;
		this.fileSize = fileSize;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public PhotoSize getThumb() {
		return thumb;
	}

	public void setThumb(PhotoSize thumb) {
		this.thumb = thumb;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	
}
