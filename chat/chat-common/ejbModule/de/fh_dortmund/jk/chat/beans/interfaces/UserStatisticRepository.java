package de.fh_dortmund.jk.chat.beans.interfaces;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

public interface UserStatisticRepository {
	UserStatistic findByUser(String username);
}
