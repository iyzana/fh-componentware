package de.fh_dortmund.jk.chat.beans.interfaces;

import javax.ejb.Local;

import de.fh_dortmund.jk.chat.beans.exception.UserNotFoundException;

@Local
public interface UserManagerLocal extends UserManager {
	void userLoggedIn(String username) throws UserNotFoundException;
	
	void userLoggedOut(String username) throws UserNotFoundException;
}
