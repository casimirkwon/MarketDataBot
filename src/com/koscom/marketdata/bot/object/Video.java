package com.koscom.marketdata.bot.object;

public class Video extends DefaultObject {

	private String fileId;
	
	private long width;
	
	private long height;
	
	private long duration;
	
	private PhotoSize thumb;
	
	private String mimeType;
	
	private long fileSize;
	
	private String caption;

	/**
	 * @param fileId
	 * @param width
	 * @param height
	 * @param duration
	 * @param thumb
	 * @param mimeType
	 * @param fileSize
	 * @param caption
	 */
	public Video(String fileId, long width, long height, long duration,
			PhotoSize thumb, String mimeType, long fileSize, String caption) {
		super();
		this.fileId = fileId;
		this.width = width;
		this.height = height;
		this.duration = duration;
		this.thumb = thumb;
		this.mimeType = mimeType;
		this.fileSize = fileSize;
		this.caption = caption;
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

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public PhotoSize getThumb() {
		return thumb;
	}

	public void setThumb(PhotoSize thumb) {
		this.thumb = thumb;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
		
}
