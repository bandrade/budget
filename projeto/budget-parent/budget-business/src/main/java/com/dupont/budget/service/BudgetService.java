package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.dto.DespesasAgrupadasDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;

public interface BudgetService {
	public List<DespesasAgrupadasDTO> obterDespesaAgrupadas(Long budgetId);
	public List<Despesa> obterDespesaNoDetalhe(Long tipoDespesaId,Long budgetId);
	public Budget insertBudget(Budget budget);
	public Budget findByAnoAndCentroDeCusto(String ano,Long centroDeCustoId);
	public void insertItemDespesa(Despesa despesa);
}
