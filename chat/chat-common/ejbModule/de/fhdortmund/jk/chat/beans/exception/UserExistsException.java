package de.fhdortmund.jk.chat.beans.exception;

public class UserExistsException extends Exception {
	public UserExistsException(String message) {
		super(message);
	}
}
