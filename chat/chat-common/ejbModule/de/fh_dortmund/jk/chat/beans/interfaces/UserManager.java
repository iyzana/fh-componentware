package de.fh_dortmund.jk.chat.beans.interfaces;

import java.util.List;

import de.fh_dortmund.jk.chat.beans.exception.UserExistsException;

public interface UserManager {
	public void register(String username, String pw) throws UserExistsException;

	public int getNumberOfOnlineUsers();

	public int getNumberOfRegisteredUsers();

	public List<String> getOnlineUsers();
}
