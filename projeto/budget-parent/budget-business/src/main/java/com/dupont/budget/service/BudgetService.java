package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;

public interface BudgetService {
	public List<Despesa> obterDespesaAgrupadas(Long budgetId);
	public Budget insertBudget(Budget budget);
	public void insertItemDespesa(Despesa despesa);
}
