package de.fh_dortmund.jk.chat.beans;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.fh_dortmund.jk.chat.beans.interfaces.CommonStatisticRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.StatisticServiceLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.StatisticServiceRemote;

@Startup
@Stateless
public class StatisticServiceBean implements StatisticServiceLocal, StatisticServiceRemote {
	@EJB
	private CommonStatisticRepositoryLocal statistics;

	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/ChatReceiving")
	private Topic chat;
	@Resource
	TimerService timers;

	@Override
	public void createFirstStatistic() {
		if (statistics.findLast() == null) {
			CommonStatistic stat = new CommonStatistic();

			LocalDateTime start = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
			LocalDateTime end = start.plusHours(1).minusNanos(1);

			stat.setStartingDate(toLegacyDate(start));
			stat.setEndDate(toLegacyDate(end));

			statistics.save(stat);
		}
	}

	@Override
	public void createIntervalTimer() {
		String timerName = "half-hours";

		for (Timer timer : timers.getTimers()) {
			if (timerName.equals(timer.getInfo()))
				return;
		}

		TimerConfig config = new TimerConfig();
		config.setInfo(timerName);
		config.setPersistent(false);

		LocalDateTime firstExpiration = LocalDateTime.now().withMinute(30);
		if (firstExpiration.isBefore(LocalDateTime.now()))
			firstExpiration = firstExpiration.plusHours(1);

		timers.createIntervalTimer(toLegacyDate(firstExpiration), 1000 * 60 * 60, config);
	}

	@Schedule(minute = "*/30", hour = "*", persistent = false)
	public void nextStatistic() {
		sendStatistic();

		CommonStatistic newStat = new CommonStatistic();

		LocalDateTime start = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
		LocalDateTime end = start.plusHours(1).minusNanos(1);

		newStat.setStartingDate(toLegacyDate(start));
		newStat.setEndDate(toLegacyDate(end));

		statistics.save(newStat);
	}

	@Timeout
	@Override
	public void sendStatistic() {
		createFirstStatistic();
		CommonStatistic stat = statistics.findLast();

		int minute = LocalDateTime.now().getMinute();
		String timeFrame = minute < 15 ? "Stunde" : "halben Stunde";
		String text = String.format(
				"Statistik der letzten %s\n" + "Anzahl der Anmeldungen: %d\n" + "Anzahl der Abmeldungen: %d\n"
						+ "Anzahl der geschriebenen Nachrichten: %d",
				timeFrame, stat.getLogins(), stat.getLogouts(), stat.getMessages());

		ChatMessage chatMessage = new ChatMessage(ChatMessageType.STATISTIC, "Statistik", text, new Date());

		Message message = jmsContext.createObjectMessage(chatMessage);

		jmsContext.createProducer().send(chat, message);
	}

	private Date toLegacyDate(LocalDateTime time) {
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}
}
