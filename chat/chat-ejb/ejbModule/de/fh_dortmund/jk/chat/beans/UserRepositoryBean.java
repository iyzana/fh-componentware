package de.fh_dortmund.jk.chat.beans;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.fh_dortmund.inf.cw.chat.server.entities.User;
import de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryLocal;
import de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryRemote;

@Stateless
public class UserRepositoryBean implements UserRepositoryLocal, UserRepositoryRemote {
	@PersistenceContext
	private EntityManager em;

	@Override
	public User save(User user) {
		em.persist(user);

		return user;
	}

	@Override
	public User update(User user) {
		return em.merge(user);
	}

	@Override
	public List<User> findAll() {
		return em.createNamedQuery("User.findAll", User.class).getResultList();
	}

	@Override
	public Optional<User> findUserByName(String username) {
		try {
			return Optional.of(em.createNamedQuery("User.findByName", User.class).setParameter("name", username).getSingleResult());
		} catch(NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public void delete(User user) {
		em.remove(user);
	}
	
	@Override
	public void deleteAll() {
		findAll().forEach(this::delete);
	}
}
