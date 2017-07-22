package com.koscom.marketdata.bot.object;

public class Document extends DefaultObject {

	private String fileId;
	
	private PhotoSize thumb;
	
	private String fileName;
	
	private String mimeType;
	
	private long fileSize;

	/**
	 * @param fileId
	 * @param thumb
	 * @param fileName
	 * @param mimeType
	 * @param fileSize
	 */
	public Document(String fileId, PhotoSize thumb, String fileName,
			String mimeType, long fileSize) {
		super();
		this.fileId = fileId;
		this.thumb = thumb;
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.fileSize = fileSize;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public PhotoSize getThumb() {
		return thumb;
	}

	public void setThumb(PhotoSize thumb) {
		this.thumb = thumb;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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
