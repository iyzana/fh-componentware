package de.fh_dortmund.jk.chat.beans;

import static java.util.Collections.synchronizedMap;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.jk.chat.beans.interfaces.UserStatisticRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserStatisticRepositoryRemote;

@Singleton
public class UserStatisticRepositoryBean implements UserStatisticRepositoryLocal, UserStatisticRepositoryRemote {
	private Map<String, UserStatistic> statistics = synchronizedMap(new HashMap<>());

	@Override
	public UserStatistic save(String username, UserStatistic statistic) {
		statistics.put(username, statistic);
		
		return statistic;
	}

	@Override
	public Map<String, UserStatistic> findAll() {
		return new HashMap<>(statistics);
	}

	@Override
	public UserStatistic findByUser(String username) {
		return statistics.get(username);
	}
	
	@Override
	public void delete(String username) {
		statistics.remove(username);
	}
}
