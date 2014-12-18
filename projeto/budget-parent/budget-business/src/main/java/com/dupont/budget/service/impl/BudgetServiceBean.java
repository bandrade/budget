package com.dupont.budget.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.dupont.budget.dto.DespesasAgrupadasDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.GenericService;

@Stateless
public class BudgetServiceBean extends GenericService implements BudgetService {

	@Inject
	private Logger logger;
	@Override
	public List<DespesasAgrupadasDTO> obterDespesaAgrupadas(Long budgetId) {

		List<Object[]> result = em.createNamedQuery("Despesa.agruparPorTipoDeDespesa")
		.setParameter("id", budgetId).getResultList();
		List<DespesasAgrupadasDTO> despesas = new ArrayList<DespesasAgrupadasDTO>();
		for(Object[] object : result)
		{
			DespesasAgrupadasDTO despesa =  new DespesasAgrupadasDTO();
			TipoDespesa t = (TipoDespesa) object[0];
			despesa.setTipoDespesaId(t.getId());
			despesa.setNomeDespesa(t.getNome());
			despesa.setValorAgrupado((Double) object[1] );
			despesas.add(despesa);
		}

		return despesas;
	}


	@Override
	public List<Despesa> obterDespesaNoDetalhe(Long tipoDespesaId,Long budgetId) {

		return em.createNamedQuery("Despesa.obterDespesaNoDetalhe",Despesa.class).setParameter("budgetId", budgetId).
				setParameter("id", tipoDespesaId).getResultList();
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
