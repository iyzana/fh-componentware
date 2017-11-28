package de.fhdortmund.jk.chat.beans;

import static java.util.stream.Collectors.toList;
import static java.util.Collections.synchronizedList;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import de.fhdortmund.jk.chat.beans.exception.UserExistsException;
import de.fhdortmund.jk.chat.beans.interfaces.User;
import de.fhdortmund.jk.chat.beans.interfaces.UserManagerLocal;
import de.fhdortmund.jk.chat.beans.interfaces.UserManagerRemote;
import de.fhdortmund.jk.chat.beans.interfaces.UserRepositoryLocal;

@Singleton
public class UserManagerBean implements UserManagerLocal, UserManagerRemote {
	@EJB
	private UserRepositoryLocal users;
	@EJB
	private HashBean hasher;
	
	private List<String> onlineUsers = synchronizedList(new LinkedList<>());

	@Override
	public void register(String username, String pw) throws UserExistsException {
		User user = new User();
		user.setName(username);
		user.setPw(hasher.computeHash(pw));
		users.save(user);
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		return users.findAll().size();
	}

	@Override
	public List<String> getOnlineUsers() {
		return onlineUsers;
	}

	@Override
	public int getNumberOfOnlineUsers() {
		return onlineUsers.size();
	}

	@Override
	public void userLoggedIn(String username) {
		System.out.println("login callback " + username);
		onlineUsers.add(username);
	}

	@Override
	public void userLoggedOut(String username) {
		System.out.println("logout callback " + username);
		onlineUsers.remove(username);
		
	}

}
