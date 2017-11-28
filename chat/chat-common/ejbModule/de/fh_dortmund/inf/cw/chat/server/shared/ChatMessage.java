package de.fh_dortmund.inf.cw.chat.server.shared;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
	private String text;
	private String sender;
	private Date date;
	private ChatMessageType type;
	
	public ChatMessage(ChatMessageType type, String sender, String text, Date date) {
		this.type = type;
		this.sender = sender;
		this.text = text;
		this.date = date;
	}
	
	public ChatMessageType getType() {
		return type;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getText() {
		return text;
	}
	
	public Date getDate() {
		return date;
	}
}
