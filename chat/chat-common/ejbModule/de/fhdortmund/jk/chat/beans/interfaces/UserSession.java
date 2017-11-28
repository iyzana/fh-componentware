package de.fhdortmund.jk.chat.beans.interfaces;

import de.fhdortmund.jk.chat.beans.exception.AlreadyLoggedInException;
import de.fhdortmund.jk.chat.beans.exception.NotAuthenticatedException;
import de.fhdortmund.jk.chat.beans.exception.UserNotFoundException;

public interface UserSession {
	public String getUserName() throws NotAuthenticatedException;

	public void login(String username, String pw) throws NotAuthenticatedException, UserNotFoundException, AlreadyLoggedInException;

	public void changePassword(String oldPw, String newPw) throws NotAuthenticatedException, UserNotFoundException;

	public void logout() throws NotAuthenticatedException;

	public void disconnect();

	public void delete(String pw) throws NotAuthenticatedException, UserNotFoundException;
}
