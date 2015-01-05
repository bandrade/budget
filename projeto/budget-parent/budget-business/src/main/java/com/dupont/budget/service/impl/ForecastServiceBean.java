package com.dupont.budget.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
public class ForecastServiceBean extends GenericService implements ForecastService{

	@Path("criar")
	@POST
	public void criarPrimeiroForecast(String budgetId) {

		Budget budget =em.find(Budget.class, Long.valueOf(budgetId));
		Forecast forecast = new Forecast(1, budget.getUsuarioCriador(),budget.getCricao(),budget);
		em.persist(forecast);
		Set<DespesaForecast> despesas = new HashSet<>();
		for(Despesa despesa : budget.getDespesas())
		{
			BudgetMes budgetMes = despesa.getDespesaMensalisada();
			DespesaForecastMes despesaForecastMes = DespesaForecastMes.createFromBudgetMes(budgetMes);
			DespesaForecast despesaForecast = DespesaForecast.createFromDespesa(despesa);
			despesaForecast.setForecast(forecast);
			em.persist(despesaForecast);
			despesaForecastMes.setId(despesaForecast.getId());
			em.persist(despesaForecastMes);
			despesaForecast.setDespesaMensalisada(despesaForecastMes);
			em.merge(despesaForecast);
			despesas.add(despesaForecast);
		}
		forecast.setDespesas(despesas);
		em.merge(forecast);
	}

	@Override
	public List<Forecast> findForecastsByBudgetId(Long id) {
		
		return em.createNamedQuery("Forecast.findByBudgetId", Forecast.class).setParameter("budgetId", id).getResultList();
	}

}
