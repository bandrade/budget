package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.dto.BudgetAreaDTO;
import com.dupont.budget.dto.DespesaMesDTO;
import com.dupont.budget.dto.DespesasAgrupadasDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetEstipuladoAno;
import com.dupont.budget.model.Despesa;

public interface BudgetService {
	public List<DespesasAgrupadasDTO> obterDespesaAgrupadas(Long budgetId) throws Exception;
	public List<Despesa> obterDespesaNoDetalhe(Long tipoDespesaId,Long budgetId) throws Exception;
	public Budget insertBudget(Budget budget) throws Exception;
	public Budget findByAnoAndCentroDeCusto(String ano,Long centroDeCustoId);
	public void insertItemDespesa(Despesa despesa) throws Exception;
	public void updateItemDespesa(Despesa despesa) throws Exception;
	public List<Despesa> obterDespesaNoDetalheBudget(Long budgetId)throws Exception ;
	public void atualizarDespesas(List<Despesa> despesasNoDetalhe) throws Exception;
	public void submeterBudget(String budgetId);
	public List<BudgetAreaDTO> listarBudgetsAprovadosPorArea(String ano) throws Exception;
	public void adicionarBudgetsSubmetidos(List<BudgetEstipuladoAno> budgets) throws Exception;
	public BudgetEstipuladoAno obterValoresAprovadosESubmetidos(Long areaId, String ano) throws Exception;
	public List<Budget> obterBudgetsPorArea (Long areaId, String ano) throws Exception;
	public List<DespesaMesDTO> obterDespesaNoDetalheBudgetAsDTO(Long budgetId) throws Exception ;
	public void mensalisarBudget(List<DespesaMesDTO> despesas) throws Exception;
	public void aprovarDespesasBudget(String budgetId);
}
