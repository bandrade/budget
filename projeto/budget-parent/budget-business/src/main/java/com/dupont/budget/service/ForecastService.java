package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.Forecast;

public interface ForecastService {

	void criarPrimeiroForecast(String budgetId) throws Exception;

	 void criarForecastSeguinte(String idForecast) throws Exception;

	List<Forecast> findForecastsByBudgetId(Long id);

	List<DespesaForecast> obterDespesasForecast(String mes, String  ano, Long idCentroCusto) throws Exception;

	void incluirDespesaForecast(DespesaForecast despesaForecast) throws Exception;

	void atualizarDespesaForecast(DespesaForecast despesaForecast) throws Exception;

}
