package de.fh_dortmund.jk.chat.beans.exception;

public class UserExistsException extends Exception {
	public UserExistsException(String message) {
		super(message);
	}
}
