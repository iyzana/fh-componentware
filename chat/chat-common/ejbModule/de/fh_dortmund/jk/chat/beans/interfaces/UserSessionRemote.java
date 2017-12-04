package de.fh_dortmund.jk.chat.beans.interfaces;

import javax.ejb.Remote;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.jk.chat.beans.exception.NotAuthenticatedException;

@Remote
public interface UserSessionRemote extends UserSession {
	UserStatistic getStat() throws NotAuthenticatedException;
}
