package de.fh_dortmund.jk.chat.beans.interfaces;

import java.util.List;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;

@Local
public interface CommonStatisticRepositoryLocal extends CommonStatisticRepository {
	CommonStatistic save(CommonStatistic statistic);

	List<CommonStatistic> findAll();

	void delete(CommonStatistic statistic);
}
