package de.fh_dortmund.jk.chat.beans.interfaces;

import javax.ejb.Local;

@Local
public interface StatisticServiceLocal extends StatisticService {
	void createFirstStatistic();
	
	void createIntervalTimer();
	
	void sendStatistic();
}
