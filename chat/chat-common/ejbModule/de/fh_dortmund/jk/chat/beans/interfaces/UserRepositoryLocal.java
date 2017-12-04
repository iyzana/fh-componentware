package de.fh_dortmund.jk.chat.beans.interfaces;

import java.util.List;
import java.util.Optional;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.entities.User;
import de.fh_dortmund.jk.chat.beans.exception.UserExistsException;
import de.fh_dortmund.jk.chat.beans.exception.UserNotFoundException;

@Local
public interface UserRepositoryLocal extends UserRepository {
	User save(User user);

	User update(User user);

	List<User> findAll();

	Optional<User> findUserByName(String username);

	void delete(User user) throws UserNotFoundException;
}
