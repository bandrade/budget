package com.dupont.budget.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.GenericService;

@Stateless
public class BudgetServiceBean extends GenericService implements BudgetService {

	@Override
	public List<Despesa> obterDespesaAgrupadas(Long budgetId) {

		return em.createNamedQuery("Despesa.agruparPorTipoDeDespesa", Despesa.class)
				.setParameter("budgetId", budgetId).getResultList();
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

}
