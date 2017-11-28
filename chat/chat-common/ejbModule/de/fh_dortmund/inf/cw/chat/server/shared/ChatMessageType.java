package de.fh_dortmund.inf.cw.chat.server.shared;

public enum ChatMessageType {
	TEXT,
	REGISTER,
	LOGIN,
	LOGOUT,
	DISCONNECT,
	STATISTIC;
	
	ChatMessageType getChatMessageType(int value) {
		return ChatMessageType.values()[value];
	}
	
	public int getValue() {
		return ordinal();
	}
}
