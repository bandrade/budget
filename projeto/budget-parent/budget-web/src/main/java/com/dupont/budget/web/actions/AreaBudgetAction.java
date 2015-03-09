package com.dupont.budget.web.actions;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.dupont.budget.dto.BudgetAreaDTO;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;

public class AreaBudgetAction {
	@Inject
	protected Logger logger;

	@Inject
	protected FacesUtils facesUtils;

	protected Long idTarefa;

	protected Long idInstanciaProcesso;

	@Inject
	protected Conversation conversation;

	protected String ano;

	@Inject
	protected BPMSProcessService bpmsProcesso;

	@Inject
	protected BPMSTaskService bpmsTask;

	@Inject
	protected BudgetService budgetService;

	protected List<BudgetAreaDTO> budgetsArea ;

	protected BigDecimal valorTotalBudget;

	@PostConstruct
	public void init()
	{
		if(conversation.isTransient())
				conversation.begin();

	}

	public void obterDadosBudget()
	{
		try {
			ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "anoBudget");
			if(budgetsArea==null)
				budgetsArea = budgetService.listarBudgetsAprovadosPorArea(ano);
			calcularValorTotalBudget();

		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter os dados da tarefa");
			logger.error("erro ao obter as variaveis de processo",e);
		}
	}

	public void calcularValorTotalBudget()
	{

		valorTotalBudget= new BigDecimal(0d);
		for(BudgetAreaDTO budgetArea : budgetsArea)
		{
			valorTotalBudget =valorTotalBudget.add(budgetArea.getValorTotalBudget());
		}
	}

	public String concluir()
	{
		try
		{
			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,new HashMap<String,Object>());
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao concluir a tarefa");
			logger.error("Erro ao concluir a tarefa",e);
		}
		return "minhasTarefas";
	}

	public String getAno() {
		return ano;
	}


	public void setAno(String ano) {
		this.ano = ano;
	}

	public Long getIdTarefa() {
		return idTarefa;
	}

	public void setIdTarefa(Long idTarefa) {
		this.idTarefa = idTarefa;
	}

	public Long getIdInstanciaProcesso() {
		return idInstanciaProcesso;
	}

	public void setIdInstanciaProcesso(Long idInstanciaProcesso) {
		this.idInstanciaProcesso = idInstanciaProcesso;
	}

	public List<BudgetAreaDTO> getBudgetsArea() {
		return budgetsArea;
	}

	public void setBudgetsArea(List<BudgetAreaDTO> budgetsArea) {
		this.budgetsArea = budgetsArea;
	}

	public BigDecimal getValorTotalBudget() {
		return valorTotalBudget;
	}

	public void setValorTotalBudget(BigDecimal valorTotalBudget) {
		this.valorTotalBudget = valorTotalBudget;
	}


}
