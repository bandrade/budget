package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.model.Forecast;

public interface ForecastService {

	void criarPrimeiroForecast(String budgetId);
	
	List<Forecast> findForecastsByBudgetId(Long id);
}
