package com.koscom.marketdata.bot.object;

import java.util.List;

public class Message extends DefaultObject {

	private long messageId;
	
	private User from;
	
	private long date;
	
	private User chat;
	
	private User forwardFrom;
	
	private long forwardDate;
	
	private Message replyToMessage;
	
	private String text;
	
	private Audio audio;
	
	private Document document;
	
	private List<PhotoSize> photo;
	
	private Sticker sticker;
	
	private Video video;
	
	private Contact contact;
	
	private Location location;
	
	private User newChatParticipant;
	
	private User leftChatParticipant;
	
	private String newChatTitle;
	
	private List<PhotoSize> newChatPhoto;
	
	private boolean deleteChatPhoto;
	
	private boolean groupChatCreated;

	/**
	 * @param messageId
	 * @param from
	 * @param date
	 * @param chat
	 * @param forwardFrom
	 * @param forwardDate
	 * @param replyToMessage
	 * @param text
	 * @param audio
	 * @param document
	 * @param photo
	 * @param sticker
	 * @param video
	 * @param contact
	 * @param location
	 * @param newChatParticipant
	 * @param leftChatParticipant
	 * @param newChatTitle
	 * @param newChatPhoto
	 * @param deleteChatPhoto
	 * @param groupChatCreated
	 */
	public Message(long messageId, User from, long date, User chat,
			User forwardFrom, long forwardDate, Message replyToMessage,
			String text, Audio audio, Document document, List<PhotoSize> photo,
			Sticker sticker, Video video, Contact contact, Location location,
			User newChatParticipant, User leftChatParticipant,
			String newChatTitle, List<PhotoSize> newChatPhoto,
			boolean deleteChatPhoto, boolean groupChatCreated) {
		super();
		this.messageId = messageId;
		this.from = from;
		this.date = date;
		this.chat = chat;
		this.forwardFrom = forwardFrom;
		this.forwardDate = forwardDate;
		this.replyToMessage = replyToMessage;
		this.text = text;
		this.audio = audio;
		this.document = document;
		this.photo = photo;
		this.sticker = sticker;
		this.video = video;
		this.contact = contact;
		this.location = location;
		this.newChatParticipant = newChatParticipant;
		this.leftChatParticipant = leftChatParticipant;
		this.newChatTitle = newChatTitle;
		this.newChatPhoto = newChatPhoto;
		this.deleteChatPhoto = deleteChatPhoto;
		this.groupChatCreated = groupChatCreated;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public User getChat() {
		return chat;
	}

	public void setChat(User chat) {
		this.chat = chat;
	}

	public User getForwardFrom() {
		return forwardFrom;
	}

	public void setForwardFrom(User forwardFrom) {
		this.forwardFrom = forwardFrom;
	}

	public long getForwardDate() {
		return forwardDate;
	}

	public void setForwardDate(long forwardDate) {
		this.forwardDate = forwardDate;
	}

	public Message getReplyToMessage() {
		return replyToMessage;
	}

	public void setReplyToMessage(Message replyToMessage) {
		this.replyToMessage = replyToMessage;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setAudio(Audio audio) {
		this.audio = audio;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public List<PhotoSize> getPhoto() {
		return photo;
	}

	public void setPhoto(List<PhotoSize> photo) {
		this.photo = photo;
	}

	public Sticker getSticker() {
		return sticker;
	}

	public void setSticker(Sticker sticker) {
		this.sticker = sticker;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public User getNewChatParticipant() {
		return newChatParticipant;
	}

	public void setNewChatParticipant(User newChatParticipant) {
		this.newChatParticipant = newChatParticipant;
	}

	public User getLeftChatParticipant() {
		return leftChatParticipant;
	}

	public void setLeftChatParticipant(User leftChatParticipant) {
		this.leftChatParticipant = leftChatParticipant;
	}

	public String getNewChatTitle() {
		return newChatTitle;
	}

	public void setNewChatTitle(String newChatTitle) {
		this.newChatTitle = newChatTitle;
	}

	public List<PhotoSize> getNewChatPhoto() {
		return newChatPhoto;
	}

	public void setNewChatPhoto(List<PhotoSize> newChatPhoto) {
		this.newChatPhoto = newChatPhoto;
	}

	public boolean isDeleteChatPhoto() {
		return deleteChatPhoto;
	}

	public void setDeleteChatPhoto(boolean deleteChatPhoto) {
		this.deleteChatPhoto = deleteChatPhoto;
	}

	public boolean isGroupChatCreated() {
		return groupChatCreated;
	}

	public void setGroupChatCreated(boolean groupChatCreated) {
		this.groupChatCreated = groupChatCreated;
	}
}
