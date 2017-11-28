package de.fh_dortmund.jk.chat.beans.exception;

public class UserNotFoundException extends Exception {
	public UserNotFoundException(String message) {
		super(message);
	}
}
