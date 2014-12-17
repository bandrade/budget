package com.dupont.budget.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;

import com.dupont.budget.exception.ExistingNameRuntimeException;
import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.model.NamedAbstractEntity;

public abstract class GenericService {
	
	@PersistenceContext(unitName="budget-pu")
	protected EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findAll(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity<?>> List<T> findAll(Class<T> t) {
		String query = String.format("select o from %s o", t.getSimpleName());
		return (List<T>) em.createQuery(query).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#create(com.dupont.budget.model.AbstractEntity)
	 */
	public <T extends AbstractEntity<?>> T create(T t) {
		if (t instanceof NamedAbstractEntity && !findByName((NamedAbstractEntity<?>)t).isEmpty()) {
			throw new ExistingNameRuntimeException(t);
		}
		em.persist(t);
		return t;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findByName(java.lang.Class, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T extends NamedAbstractEntity<?>> List<T> findByName(T t) {
		String name = t.getNome();
		if (StringUtils.isBlank(name)) {
			return (List<T>) findAll(t.getClass());
		}

		List<T> result = (List<T>) em.createQuery(
					String.format("select o from %s o where o.nome like :nome", t.getClass().getSimpleName())
				).setParameter("nome", name.trim() + "%").getResultList();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findByName(java.lang.Class, java.lang.String)
	 */
	public <T extends AbstractEntity<?>> void delete(T t) {	
		t = em.merge(t); // attaching the entity again
		em.remove(t);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findByName(java.lang.Class, java.lang.String)
	 */
	public <T extends AbstractEntity<?>> T update(T t) {
		if (t instanceof NamedAbstractEntity && !findByName((NamedAbstractEntity<?>)t).isEmpty()) {
			throw new ExistingNameRuntimeException(t);
		}
		return em.merge(t);
	}

}
