package com.dupont.budget.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GenericService {
	
	@PersistenceContext(unitName="budget-pu")
	protected EntityManager em;
	
	

}
