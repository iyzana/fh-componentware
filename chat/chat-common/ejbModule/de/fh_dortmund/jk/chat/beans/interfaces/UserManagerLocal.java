package de.fh_dortmund.jk.chat.beans.interfaces;

import javax.ejb.Local;

@Local
public interface UserManagerLocal extends UserManager {
	void userLoggedIn(String username);
	
	void userLoggedOut(String username);
}
