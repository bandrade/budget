package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.Produto;

@ConversationScoped
@Named
public class CriarBudgetAction implements Serializable {
	private TarefaDTO tarefa;
	private Budget budget;
	private Despesa despesa;
	
	
	@Inject
	private Conversation conversation;
	
	
	@PostConstruct
	public void init(){
		conversation.begin();
		despesa = new Despesa();
		despesa.setProduto(new Produto());
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
	
}
