package de.fh_dortmund.jk.chat.beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.jk.chat.beans.interfaces.CommonStatisticRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.CommonStatisticRepositoryRemote;

@Stateless
public class CommonStatisticRepositoryBean implements CommonStatisticRepositoryLocal, CommonStatisticRepositoryRemote {
	@PersistenceContext
	private EntityManager em;

	@Override
	public CommonStatistic save(CommonStatistic statistic) {
		em.persist(statistic);

		return statistic;
	}

	@Override
	public CommonStatistic update(CommonStatistic statistic) {
		return em.merge(statistic);
	}

	@Override
	public List<CommonStatistic> findAll() {
		return em.createNamedQuery("CommonStatistic.findAll", CommonStatistic.class).getResultList();
	}

	@Override
	public CommonStatistic findLast() {
		try {
			return em.createNamedQuery("CommonStatistic.findLast", CommonStatistic.class).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void delete(CommonStatistic statistic) {
		em.remove(statistic);
	}
}
