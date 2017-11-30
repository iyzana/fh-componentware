package de.fh_dortmund.jk.chat.beans;

import static java.util.Collections.synchronizedList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.jk.chat.beans.interfaces.UserStatisticRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserStatisticRepositoryRemote;

@Singleton
public class UserStatisticRepositoryBean implements UserStatisticRepositoryLocal, UserStatisticRepositoryRemote {
	private List<UserStatistic> statistics = synchronizedList(new LinkedList<>());

	@Override
	public UserStatistic save(UserStatistic statistic) {
		statistics.add(statistic);
		
		return statistic;
	}

	@Override
	public List<UserStatistic> findAll() {
		return new ArrayList<>(statistics);
	}

	@Override
	public void delete(UserStatistic statistic) {
		statistics.remove(statistic);
	}
}
