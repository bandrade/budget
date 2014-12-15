package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.Produto;
import com.dupont.budget.service.BudgetService;

@ConversationScoped
@Named
public class CriarBudgetAction implements Serializable {
	private TarefaDTO tarefa;
	private Budget budget;
	private Despesa despesa;
	private List<Despesa> despesasAgrupadas;
	private Despesa despesaSelecionada;
	@Inject
	private BudgetService budgetService;
	
	
	@Inject
	private Conversation conversation;
	
	
	@PostConstruct
	private void init(){
		conversation.begin();
		despesa = new Despesa();
		despesa.setProduto(new Produto());
		budget = new Budget();
		carregarDespesasBudget();
	}
	
	private void carregarDespesasBudget() {
		if(budget !=null && budget.getId() !=null)
		{
			budgetService.obterDespesaAgrupadas(budget.getId());
		}
	}

	public void adicionarDespesa()
	{
		
	}
	
	public String navegar()
	{
		
		return "incluirBudget";
	}
	public String criarBudget()
	{
		
		return null;
	}
	
	
	public Budget getBudget() {
		return budget;
	}
	public void setBudget(Budget budget) {
		this.budget = budget;
	}
	public TarefaDTO getTarefa() {
		return tarefa;
	}
	public void setTarefa(TarefaDTO tarefa) {
		this.tarefa = tarefa;
	}
	public Despesa getDespesa() {
		return despesa;
	}
	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public Despesa getDespesaSelecionada() {
		return despesaSelecionada;
	}

	public void setDespesaSelecionada(Despesa despesaSelecionada) {
		this.despesaSelecionada = despesaSelecionada;
	}

	public List<Despesa> getDespesasAgrupadas() {
		return despesasAgrupadas;
	}

	public void setDespesasAgrupadas(List<Despesa> despesasAgrupadas) {
		this.despesasAgrupadas = despesasAgrupadas;
	}
	
	
}
