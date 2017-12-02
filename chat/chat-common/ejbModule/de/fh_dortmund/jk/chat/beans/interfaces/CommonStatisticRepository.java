package de.fh_dortmund.jk.chat.beans.interfaces;

import java.util.List;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;

public interface CommonStatisticRepository {
	List<CommonStatistic> findAll();
}
