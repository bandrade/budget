package com.dupont.budget.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;

import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetMes;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.DespesaForecastMes;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.service.GenericService;

@Stateless
@Path("forecast")
@TransactionManagement(TransactionManagementType.BEAN)
public class ForecastServiceBean extends GenericService implements ForecastService{
	@Inject
	private Logger logger;
	@Resource
	private UserTransaction tx;
	@Path("criar")
	@POST
	public void criarPrimeiroForecast(String budgetId) throws Exception {
		try
		{
			tx.begin();
			Budget budget =em.find(Budget.class, Long.valueOf(budgetId));
			Forecast forecast = new Forecast(1, budget.getUsuarioCriador(),budget.getCricao(),budget);
			em.persist(forecast);
			Set<DespesaForecast> despesas = new HashSet<>();
			for(Despesa despesa : budget.getDespesas())
			{

				if(!despesa.getAprovado())
					continue;
				BudgetMes budgetMes = despesa.getDespesaMensalisada();
				DespesaForecastMes despesaForecastMes = DespesaForecastMes.createFromBudgetMes(budgetMes);
				DespesaForecast despesaForecast = DespesaForecast.createFromDespesa(despesa);
				despesaForecast.setForecast(forecast);
				despesaForecast.setDespesaBudget(despesa);
				em.persist(despesaForecast);
				despesaForecastMes.setId(despesaForecast.getId());
				em.persist(despesaForecastMes);
				despesaForecast.setDespesaMensalisada(despesaForecastMes);
				despesas.add(despesaForecast);
				em.merge(despesaForecast);
			}
			forecast.setDespesas(despesas);
			em.merge(forecast);
			tx.commit();
		}
		catch(Exception e)
		{
			logger.error("Erro ao criar o forecast ",e);
			throw e;
		}


	}

	@Override
	public List<Forecast> findForecastsByBudgetId(Long id) {

		return em.createNamedQuery("Forecast.findByBudgetId", Forecast.class).setParameter("budgetId", id).getResultList();
	}

}
