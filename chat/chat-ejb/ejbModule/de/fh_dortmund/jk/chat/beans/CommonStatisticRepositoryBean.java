package de.fh_dortmund.jk.chat.beans;

import static java.util.Collections.synchronizedList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.jk.chat.beans.interfaces.CommonStatisticRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.CommonStatisticRepositoryRemote;

@Singleton
public class CommonStatisticRepositoryBean implements CommonStatisticRepositoryLocal, CommonStatisticRepositoryRemote {
	private List<CommonStatistic> statistics = synchronizedList(new LinkedList<>());

	@Override
	public CommonStatistic save(CommonStatistic statistic) {
		statistics.add(statistic);
		
		return statistic;
	}

	@Override
	public List<CommonStatistic> findAll() {
		return new ArrayList<>(statistics);
	}

	@Override
	public void delete(CommonStatistic statistic) {
		statistics.remove(statistic);
	}
}
