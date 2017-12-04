package de.fh_dortmund.jk.chat.beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.jk.chat.beans.interfaces.UserStatisticRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserStatisticRepositoryRemote;

@Stateless
public class UserStatisticRepositoryBean implements UserStatisticRepositoryLocal, UserStatisticRepositoryRemote {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public UserStatistic save(UserStatistic statistic) {
		em.persist(statistic);
		
		return statistic;
	}

	@Override
	public List<UserStatistic> findAll() {
		return em.createNamedQuery("UserStatistic.findAll", UserStatistic.class).getResultList();
	}
	
	@Override
	public void delete(UserStatistic statistic) {
		em.remove(statistic);
	}
}
