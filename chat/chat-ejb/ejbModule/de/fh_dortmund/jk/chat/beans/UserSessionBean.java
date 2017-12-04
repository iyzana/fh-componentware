package de.fh_dortmund.jk.chat.beans;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import de.fh_dortmund.inf.cw.chat.server.entities.User;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.jk.chat.beans.exception.AlreadyLoggedInException;
import de.fh_dortmund.jk.chat.beans.exception.NotAuthenticatedException;
import de.fh_dortmund.jk.chat.beans.exception.UserNotFoundException;
import de.fh_dortmund.jk.chat.beans.interfaces.UserManagerLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserSessionLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserSessionRemote;

@Stateful
public class UserSessionBean implements UserSessionLocal, UserSessionRemote {

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
	public void login(String username, String pw) throws NotAuthenticatedException, UserNotFoundException, AlreadyLoggedInException {
		User user = users.findUserByName(username)
				.orElseThrow(() -> new UserNotFoundException("Der Nutzer " + username + " wurde nicht gefunden"));
		
		if (!passwordMatches(pw, user.getPw()))
			throw new NotAuthenticatedException("Falsches Passwort");
		
		if (this.user != null)
			throw new AlreadyLoggedInException("Du bist bereits eingeloggt");

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
	public void logout() throws NotAuthenticatedException, UserNotFoundException {
		if (user == null)
			throw new NotAuthenticatedException("Nicht eingeloggt");
		
		manager.userLoggedOut(user.getName());
		
		this.user = null;
	}

	@Remove
	@Override
	public void disconnect() {
		if (user != null) {
			try {
				logout();
			} catch (NotAuthenticatedException | UserNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void delete(String pw) throws NotAuthenticatedException, UserNotFoundException {
		requireFullAuthentication(pw);
		
		users.delete(user);
		logout();
	}
	
	@Override
	public UserStatistic getStat() throws NotAuthenticatedException {
		if (user == null)
			throw new NotAuthenticatedException("Nicht eingeloggt");
		
		return users.findUserByName(user.getName()).get().getStat();
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
