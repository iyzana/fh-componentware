package de.fh_dortmund.jk.chat.beans.interfaces;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.entities.User;

@Local
public interface UserManagerLocal extends UserManager {
	void userLoggedIn(User user);
	
	void userLoggedOut(User user);
}
