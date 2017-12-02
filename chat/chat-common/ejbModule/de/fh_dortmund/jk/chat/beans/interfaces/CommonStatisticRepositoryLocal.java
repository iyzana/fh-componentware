package de.fh_dortmund.jk.chat.beans.interfaces;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;

@Local
public interface CommonStatisticRepositoryLocal extends CommonStatisticRepository {
	CommonStatistic save(CommonStatistic statistic);
	
	CommonStatistic update(CommonStatistic statistic);
	
	CommonStatistic findLast();

	void delete(CommonStatistic statistic);
}
