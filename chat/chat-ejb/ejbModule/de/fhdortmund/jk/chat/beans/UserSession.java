package de.fhdortmund.jk.chat.beans;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import de.fhdortmund.jk.chat.beans.exception.NotAuthenticatedException;
import de.fhdortmund.jk.chat.beans.exception.UserNotFoundException;
import de.fhdortmund.jk.chat.beans.interfaces.User;
import de.fhdortmund.jk.chat.beans.interfaces.UserManagerLocal;
import de.fhdortmund.jk.chat.beans.interfaces.UserRepositoryLocal;
import de.fhdortmund.jk.chat.beans.interfaces.UserSessionLocal;
import de.fhdortmund.jk.chat.beans.interfaces.UserSessionRemote;

@Stateful
public class UserSession implements UserSessionLocal, UserSessionRemote {

	@EJB
	private UserRepositoryLocal users;
	@EJB
	private UserManagerLocal manager;
	@EJB
	private HashBean hasher;

	private User user;

	@Override
	public String getUserName() throws NotAuthenticatedException {
		if (user == null)
			throw new NotAuthenticatedException("Nicht eingeloggt");
		
		return user.getName();
	}

	@Override
	public void login(String username, String pw) throws NotAuthenticatedException, UserNotFoundException {
		User user = users.findUserByName(username)
				.orElseThrow(() -> new UserNotFoundException("Der Nutzer " + username + " wurde nicht gefunden"));
		
		if (!passwordMatches(pw, user.getPw()))
			throw new NotAuthenticatedException("Falsches Passwort");
		
		this.user = user;
		
		manager.userLoggedIn(user.getName());
	}

	@Override
	public void changePassword(String oldPw, String newPw) throws NotAuthenticatedException, UserNotFoundException {
		requireFullAuthentication(oldPw);
		
		user.setPw(hasher.computeHash(newPw));
		users.update(user);
	}

	@Remove
	@Override
	public void logout() throws NotAuthenticatedException {
		manager.userLoggedOut(getUserName());
		
		this.user = null;
	}

	@Remove
	@Override
	public void disconnect() {
		if (user != null) {
			try {
				logout();
			} catch (NotAuthenticatedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Remove
	@Override
	public void delete(String pw) throws NotAuthenticatedException, UserNotFoundException {
		requireFullAuthentication(pw);
		
		users.delete(user);
		logout();
	}
	
	private void requireFullAuthentication(String pw) throws NotAuthenticatedException {
		if (user == null)
			throw new NotAuthenticatedException("Nicht eingeloggt");
		
		if (!passwordMatches(pw, user.getPw()))
			throw new NotAuthenticatedException("Falsches Passwort");
	}

	private boolean passwordMatches(String pw, String hash) {
		return hasher.computeHash(pw).equals(hash);
	}

}
