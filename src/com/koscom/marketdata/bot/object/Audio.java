package com.koscom.marketdata.bot.object;

public class Audio extends DefaultObject {

	private String fileId;
	
	private long duration;
	
	private String mimeType;
	
	private long fileSize;

	/**
	 * @param fileId
	 * @param duration
	 * @param mimeType
	 * @param fileSize
	 */
	public Audio(String fileId, long duration, String mimeType, long fileSize) {
		super();
		this.fileId = fileId;
		this.duration = duration;
		this.mimeType = mimeType;
		this.fileSize = fileSize;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
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

	
}
