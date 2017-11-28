package de.fh_dortmund.jk.chat.beans.interfaces;

import javax.ejb.Remote;

@Remote
public interface UserRepositoryRemote extends UserRepository {
	void deleteAll();
}
