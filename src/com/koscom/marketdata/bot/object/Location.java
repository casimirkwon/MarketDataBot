package com.koscom.marketdata.bot.object;

public class Location extends DefaultObject {

	private double longitude;
	
	private double latitude;

	/**
	 * @param longitude
	 * @param latitude
	 */
	public Location(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
}
