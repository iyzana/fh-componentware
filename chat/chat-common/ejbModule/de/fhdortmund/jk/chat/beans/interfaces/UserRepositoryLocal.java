package de.fhdortmund.jk.chat.beans.interfaces;

import java.util.List;
import java.util.Optional;

import javax.ejb.Local;

import de.fhdortmund.jk.chat.beans.exception.UserExistsException;
import de.fhdortmund.jk.chat.beans.exception.UserNotFoundException;

@Local
public interface UserRepositoryLocal extends UserRepository {
	User save(User user) throws UserExistsException;

	User update(User user) throws UserNotFoundException;

	List<User> findAll();

	Optional<User> findUserByName(String username);

	void delete(User user) throws UserNotFoundException;
}
