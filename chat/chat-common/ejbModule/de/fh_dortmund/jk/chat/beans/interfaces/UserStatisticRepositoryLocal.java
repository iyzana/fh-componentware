package de.fh_dortmund.jk.chat.beans.interfaces;

import java.util.Map;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

@Local
public interface UserStatisticRepositoryLocal extends UserStatisticRepository {
	UserStatistic save(String username, UserStatistic statistic);

	Map<String, UserStatistic> findAll();

	void delete(String username);
}
