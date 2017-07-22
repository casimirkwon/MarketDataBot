package com.koscom.marketdata.bot.object;

import java.util.List;

public class UserProfilePhotos extends DefaultObject {
	
	private long totalCount;
	
	private List<PhotoSize> photos;

	/**
	 * @param totalCount
	 * @param photos
	 */
	public UserProfilePhotos(long totalCount, List<PhotoSize> photos) {
		super();
		this.totalCount = totalCount;
		this.photos = photos;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<PhotoSize> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoSize> photos) {
		this.photos = photos;
	}
	
	
	
}
