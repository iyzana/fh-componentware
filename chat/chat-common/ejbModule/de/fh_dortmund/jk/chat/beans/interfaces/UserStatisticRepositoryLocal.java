package de.fh_dortmund.jk.chat.beans.interfaces;

import java.util.List;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

@Local
public interface UserStatisticRepositoryLocal extends UserStatisticRepository {
	UserStatistic save(UserStatistic statistic);

	List<UserStatistic> findAll();

	void delete(UserStatistic statistic);
}
