package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.model.Despesa;

public interface BudgetService {
	public List<Despesa> obterDespesaAgrupadas(Long budgetId);
	
}
