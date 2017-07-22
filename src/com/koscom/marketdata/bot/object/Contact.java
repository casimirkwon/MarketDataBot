package com.koscom.marketdata.bot.object;

public class Contact extends DefaultObject {

	private String phoneNumber;
	
	private String firstName;
	
	private String lastName;
	
	private String userId;

	/**
	 * @param phoneNumber
	 * @param firstName
	 * @param lastName
	 * @param userId
	 */
	public Contact(String phoneNumber, String firstName, String lastName,
			String userId) {
		super();
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
