package de.fhdortmund.jk.chat.client;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.fhdortmund.jk.chat.beans.exception.NotAuthenticatedException;
import de.fhdortmund.jk.chat.beans.interfaces.UserManager;
import de.fhdortmund.jk.chat.beans.interfaces.UserSession;

public class ServiceHandlerImpl extends ServiceHandler implements UserSessionHandler {
	
	private static ServiceHandlerImpl instance;
	
	public static ServiceHandlerImpl getInstance() {
		return instance != null ? instance : (instance = new ServiceHandlerImpl());
	}
	
	private UserSession session;
	private UserManager manager;
	
	private ServiceHandlerImpl() {
		try {
			Context ctx = new InitialContext();
			
			session = (UserSession) ctx.lookup("java:global/chat-ear/chat-ejb/UserSession!de.fhdortmund.jk.chat.beans.interfaces.UserSessionRemote");
			manager = (UserManager) ctx.lookup("java:global/chat-ear/chat-ejb/UserManagerBean!de.fhdortmund.jk.chat.beans.interfaces.UserManagerRemote");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getUserName() {
		try {
			return session.getUserName();
		} catch (NotAuthenticatedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void login(String user, String pw) throws Exception {
		session.login(user, pw);
	}

	@Override
	public void register(String user, String pw) throws Exception {
		manager.register(user, pw);
	}

	@Override
	public int getNumberOfOnlineUsers() {
		return manager.getNumberOfOnlineUsers();
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		return manager.getNumberOfRegisteredUsers();
	}

	@Override
	public List<String> getOnlineUsers() {
		return manager.getOnlineUsers();
	}
	
	@Override
	public void changePassword(String oldPw, String newPw) throws Exception {
		session.changePassword(oldPw, newPw);
	}

	@Override
	public void logout() throws Exception {
		session.logout();
	}

	@Override
	public void disconnect() {
		session.disconnect();
	}

	@Override
	public void delete(String pw) throws Exception {
		session.delete(pw);
	}
}
