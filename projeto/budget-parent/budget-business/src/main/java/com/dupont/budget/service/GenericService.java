package com.dupont.budget.service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.dupont.budget.exception.ExistingNameRuntimeException;
import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.model.NamedAbstractEntity;

public abstract class GenericService {
	
	@PersistenceContext(unitName="budget-pu")
	protected EntityManager em;
	
	@Inject
	private Logger logger;
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findAll(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity<?>> List<T> findAll(Class<T> t) {
		String query = String.format("select o from %s o ", t.getSimpleName());
		if (t.getSuperclass().equals(NamedAbstractEntity.class)) {
			query = query.concat("order by o.nome");
		}
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
		String name = t != null ? t.getNome() : "";
		if (StringUtils.isBlank(name)) {
			return (List<T>) findAll(t.getClass());
		}

		List<T> result = (List<T>) em.createQuery(
					String.format("select o from %s o where lower(o.nome) like :nome order by nome", t.getClass().getSimpleName())
				).setParameter("nome", "%".concat(name.trim().toLowerCase()).concat("%")).getResultList();

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends NamedAbstractEntity<?>> List<T> findByExactName(T t) {
		String name = t != null ? t.getNome() : "";
		if (StringUtils.isBlank(name)) {
			return (List<T>) findAll(t.getClass());
		}

		List<T> result = (List<T>) em.createQuery(
					String.format("select o from %s o where lower(o.nome) like :nome order by nome", t.getClass().getSimpleName())
				).setParameter("nome", name.trim().toLowerCase()).getResultList();

		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findByExample(com.dupont.budget.model.AbstractEntity)
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity<?>> T findById(T t) {
		Object id = t.getId();
		T result = (T) em.createQuery(
					String.format("select o from %s o where o.id = :id", t.getClass().getSimpleName())
				).setParameter("id", id).getSingleResult();

		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findByExample(com.dupont.budget.model.AbstractEntity)
	 */
	public <T extends AbstractEntity<?>> List<T> findByExample(T t) {
		List<T> list = null;
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<T> cq = cb.createQuery(clazz);
	        Root<T> e = cq.from(clazz);
	        Predicate p = cb.conjunction();
	        Metamodel mm = em.getMetamodel();
	        EntityType<T> et = mm.entity(clazz);
	        Set<Attribute<? super T, ?>> attrs = et.getAttributes();
	        Object val = null;
	        for (Attribute<? super T, ?> a: attrs) {
	            String name = a.getName();
	            if ((val = getValue(a, clazz, t)) !=  null) {
	            	if (val instanceof String) {
	            		p = cb.and(p, cb.like(e.<String>get(name), "%".concat(val.toString()).concat("%")));
	            	} else if (val instanceof AbstractEntity) {
	            		EntityType<?> st = mm.entity(val.getClass());
	            		Object tmp = null;
	            		for (Attribute<?, ?> at: st.getAttributes()) {
	            			if ((tmp = getValue(at, val.getClass(), val)) !=  null) {
	            				if (tmp instanceof String) {
	        	            		p = cb.and(p, cb.like(e.get(name).<String>get(at.getName()), "%".concat(tmp.toString()).concat("%")));
	        	            	} else if (!(tmp instanceof Collection)) {
	        	            		p = cb.and(p, cb.equal(e.get(name).get(at.getName()), tmp));
	        	            	}
	            			}
	            		}	            		
	            	} else if (!(val instanceof Collection)) {
	            		p = cb.and(p, cb.equal(e.get(name), val));
	            	}
	            }
	        }
	        cq.select(e).where(p);
	        TypedQuery<T> query = em.createQuery(cq);
	        list = query.getResultList();
		} catch (Exception e) {
			list = new LinkedList<>();
		}
        return list;
	}
	
	private Object getValue(Attribute<?, ?> a, Class<?> clazz, Object t) throws Exception {
        String javaName = a.getJavaMember().getName();
        String getter = "get" + javaName.substring(0,1).toUpperCase() + javaName.substring(1);
        Method m = clazz.getMethod(getter, (Class<?>[]) null);
        return m.invoke(t, (Object[]) null);
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
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity<?>> T update(T t) {
		if (t instanceof NamedAbstractEntity) {
			List<T> list = (List<T>) findByExactName((NamedAbstractEntity<?>) t);
			list.remove(t);
			if (!list.isEmpty()) {
				throw new ExistingNameRuntimeException(t);
			}
		}
		t = em.merge(t);
		return t;
	}

}
