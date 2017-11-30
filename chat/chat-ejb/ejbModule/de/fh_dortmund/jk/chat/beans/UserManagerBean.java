package de.fh_dortmund.jk.chat.beans;

import static java.util.Collections.synchronizedList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.fh_dortmund.jk.chat.beans.exception.UserExistsException;
import de.fh_dortmund.jk.chat.beans.interfaces.UserManagerLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserManagerRemote;
import de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryLocal;
import de.fh_dortmund.jk.chat.entity.User;

@Singleton
public class UserManagerBean implements UserManagerLocal, UserManagerRemote {
	@EJB
	private UserRepositoryLocal users;
	@EJB
	private HashBean hasher;
	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/ChatReceiving")
	private Topic chat;
	@Resource(lookup = "java:global/jms/Disconnect")
	private Topic disconnect;

	private List<String> onlineUsers = synchronizedList(new LinkedList<>());

	@Override
	public void register(String username, String pw) throws UserExistsException {
		User user = new User();
		user.setName(username);
		user.setPw(hasher.computeHash(pw));
		users.save(user);

		sendMessageOfType(ChatMessageType.REGISTER, username);
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
		if (onlineUsers.contains(username))
			sendDisconnectMessage(username);

		onlineUsers.add(username);
		sendMessageOfType(ChatMessageType.LOGIN, username);
	}

	@Override
	public void userLoggedOut(String username) {
		onlineUsers.remove(username);
		sendMessageOfType(ChatMessageType.LOGOUT, username);
	}

	private void sendMessageOfType(ChatMessageType type, String sender) {
		ChatMessage chatMessage = new ChatMessage(type, sender, "", new Date());
		Message message = jmsContext.createObjectMessage(chatMessage);

		jmsContext.createProducer().send(chat, message);
	}

	private void sendDisconnectMessage(String sender) {
		try {
			ChatMessage chatMessage = new ChatMessage(ChatMessageType.DISCONNECT, sender, "", new Date());
			Message message = jmsContext.createObjectMessage(chatMessage);
			message.setStringProperty("USERNAME", sender);

			jmsContext.createProducer().send(disconnect, message);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}
