package com.dupont.budget.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.GenericService;

@Stateless
public class BudgetServiceBean extends GenericService implements BudgetService {
	
	@Inject
	private Logger logger;
	@Override
	public List<Despesa> obterDespesaAgrupadas(Long budgetId) {

		return em.createNamedQuery("Despesa.agruparPorTipoDeDespesa", Despesa.class)
				.setParameter("id", budgetId).getResultList();
	}

	@Override
	public Budget insertBudget(Budget budget) {
		em.persist(budget);
		return budget;
	}

	@Override
	public void insertItemDespesa(Despesa despesa) {
		em.persist(despesa);
	}

	@Override
	public Budget findByAnoAndCentroDeCusto(String ano, Long centroDeCustoId) {
		Budget budget = null;
		try
		{
			budget =em.createNamedQuery("Budget.findByAnoAndCentroDeCusto", Budget.class)
				.setParameter("centroDeCustoId", centroDeCustoId).setParameter("ano", ano).getSingleResult();
		}
		catch(NoResultException e)
		{
			logger.info("Nenhum resultado encontrado");
		}
		
		return  budget;
	}

}
