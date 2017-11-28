package de.fh_dortmund.jk.chat.beans;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.fh_dortmund.jk.chat.beans.interfaces.MessageProcessorLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.MessageProcessorRemote;

@MessageDriven(mappedName = "java:global/jms/ChatIncoming", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic") })
public class MessageProcessorBean implements MessageProcessorLocal, MessageProcessorRemote, MessageListener {
	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/ChatOutgoing")
	private Topic chat;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;

			ChatMessageType type = ChatMessageType.TEXT;
			String sender = textMessage.getStringProperty("SENDER");
			String content = textMessage.getText();
			Date when = new Date();

			String sanitizedContent = Arrays.stream(content.split("\\b"))
				.map(this::badWordReplacer)
				.collect(joining());

			sendMessage(new ChatMessage(type, sender, sanitizedContent, when));
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
		boolean containsBadWord = badWords.stream().anyMatch(badWord -> word.toLowerCase().contains(badWord));
		return containsBadWord ? "#!*$" : word;
	}

	// ========== BEWARE, ACTUAL BAD WORDS ==========
	
	private Set<String> initBadWords() {
		Set<String> badWords = new HashSet<>();
		badWords.add("arsch");
		badWords.add("loser");
		badWords.add("fuck");
		badWords.add("fck");
		badWords.add("whore");
		badWords.add("idiot");
		badWords.add("fag");
		badWords.add("turd");
		badWords.add("twat");
		badWords.add("shit");
		badWords.add("cock");
		badWords.add("dick");
		badWords.add("pussy");
		badWords.add("bitch");
		badWords.add("slut");
		badWords.add("scum");
		badWords.add("cunt");
		return badWords;
	}
}
