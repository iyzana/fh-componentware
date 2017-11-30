package de.fh_dortmund.jk.chat.client;

import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.fh_dortmund.inf.cw.chat.client.shared.ChatMessageHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.jk.chat.beans.exception.NotAuthenticatedException;
import de.fh_dortmund.jk.chat.beans.interfaces.UserManagerRemote;
import de.fh_dortmund.jk.chat.beans.interfaces.UserSessionRemote;

public class ServiceHandlerImpl extends ServiceHandler
		implements UserSessionHandler, ChatMessageHandler, MessageListener {

	private static ServiceHandlerImpl instance;

	public static ServiceHandlerImpl getInstance() {
		return instance != null ? instance : (instance = new ServiceHandlerImpl());
	}

	private UserSessionRemote session;
	private UserManagerRemote manager;
	private JMSContext jmsContext;
	private Topic chat;
	private Queue disconnect;

	public ServiceHandlerImpl() {
		try {
			Context ctx = new InitialContext();

			session = (UserSessionRemote) ctx.lookup(
					"java:global/chat-ear/chat-ejb/UserSessionBean!de.fh_dortmund.jk.chat.beans.interfaces.UserSessionRemote");
			manager = (UserManagerRemote) ctx.lookup(
					"java:global/chat-ear/chat-ejb/UserManagerBean!de.fh_dortmund.jk.chat.beans.interfaces.UserManagerRemote");

			ConnectionFactory con = (ConnectionFactory) ctx.lookup("java:comp/DefaultJMSConnectionFactory");
			jmsContext = con.createContext();

			chat = (Topic) ctx.lookup("java:global/jms/ChatSending");

			Queue chatIncoming = (Queue) ctx.lookup("java:global/jms/ChatReceiving");
			jmsContext.createConsumer(chatIncoming).setMessageListener(this);
			
			disconnect = (Queue) ctx.lookup("java:global/jms/Disconnect");
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
		
		jmsContext.createConsumer(disconnect, "USERNAME = '" + user + "'").setMessageListener(this);
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

	@Override
	public void sendChatMessage(String text) {
		try {
			TextMessage message = jmsContext.createTextMessage(text);
			message.setStringProperty("SENDER", getUserName());

			jmsContext.createProducer().send(chat, message);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onMessage(Message message) {
		try {
			ChatMessage chatMessage = (ChatMessage) ((ObjectMessage) message).getObject();
			
			setChanged();
			notifyObservers(chatMessage);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
