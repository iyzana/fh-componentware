package de.fh_dortmund.jk.chat.beans;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.fh_dortmund.jk.chat.beans.interfaces.CommonStatisticRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.MessageProcessorLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.MessageProcessorRemote;
import de.fh_dortmund.jk.chat.beans.interfaces.UserStatisticRepositoryLocal;

@MessageDriven(mappedName = "java:global/jms/ChatSending", messageListenerInterface = MessageListener.class, activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class MessageProcessorBean implements MessageProcessorLocal, MessageProcessorRemote, MessageListener {
	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/ChatReceiving")
	private Topic chat;

	@EJB
	private UserStatisticRepositoryLocal userStatistics;
	@EJB
	private CommonStatisticRepositoryLocal commonStatistics;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;

			ChatMessageType type = ChatMessageType.TEXT;
			String sender = textMessage.getStringProperty("SENDER");
			String content = textMessage.getText();
			Date when = new Date();

			StringBuilder sanitizedContent = new StringBuilder();
			for (String word : content.split("\\b")) {
				sanitizedContent.append(badWordReplacer(word));
			}

			UserStatistic userStat = userStatistics.findByUser(sender);
			if (userStat == null)
				userStat = new UserStatistic();
			userStat.setMessages(userStat.getMessages() + 1);
			userStatistics.save(sender, userStat);

			CommonStatistic commonStat = commonStatistics.findLast();
			commonStat.setMessages(commonStat.getMessages() + 1);
			commonStatistics.update(commonStat);

			sendMessage(new ChatMessage(type, sender, sanitizedContent.toString(), when));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(ChatMessage chatMessage) {
		Message message = jmsContext.createObjectMessage(chatMessage);

		jmsContext.createProducer().send(chat, message);
	}

	private Set<String> badWords = initBadWords();

	private String badWordReplacer(String word) {
		for (String badWord : badWords) {
			if (word.toLowerCase().contains(badWord))
				return "#!*$";
		}

		return word;
	}

	// ========== BEWARE, ACTUAL BAD WORDS ==========

	private Set<String> initBadWords() {
		return new HashSet<>(Arrays.asList("arsch", "looser", "fuck", "fck", "whore", "idiot", "fag", "turd", "twat",
				"shit", "cock", "dick", "pussy", "bitch", "slut", "scum", "cunt"));
	}
}
