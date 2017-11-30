package de.fh_dortmund.jk.chat.beans;

import static java.util.Collections.synchronizedList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Singleton;

import de.fh_dortmund.jk.chat.beans.exception.UserExistsException;
import de.fh_dortmund.jk.chat.beans.exception.UserNotFoundException;
import de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryRemote;
import de.fh_dortmund.jk.chat.entity.User;

@Singleton
public class UserRepositoryBean implements UserRepositoryLocal, UserRepositoryRemote {
	private List<User> users = synchronizedList(new LinkedList<>());

	@Override
	public User save(User user) throws UserExistsException {
		if (findUserByName(user.getName()).isPresent())
			throw new UserExistsException("Der Nutzer " + user.getName() + " existiert bereits");

		users.add(user);

		return user;
	}

	@Override
	public User update(User user) throws UserNotFoundException {
		delete(user);
		users.add(user);
		
		return user;
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(users);
	}

	@Override
	public Optional<User> findUserByName(String username) {
		return users.stream().filter(user -> user.getName().equals(username)).findFirst();
	}

	@Override
	public void delete(User user) throws UserNotFoundException {
		User actualUser = findUserByName(user.getName())
				.orElseThrow(() -> new UserNotFoundException("Der Nutzer " + user.getName() + " wurde nicht gefunden"));

		users.remove(actualUser);
	}
	
	@Override
	public void deleteAll() {
		users.clear();
	}
}
