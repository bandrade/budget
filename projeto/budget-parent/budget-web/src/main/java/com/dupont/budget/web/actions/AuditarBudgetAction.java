package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.BudgetAreaDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.BudgetEstipuladoAno;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;


@ConversationScoped
@Named
public class AuditarBudgetAction implements Serializable {
	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	private Long idTarefa;

	private Long idInstanciaProcesso;

	@Inject
	private Conversation conversation;

	private String ano;

	@Inject
    private BPMSProcessService bpmsProcesso;

	@Inject
	protected BPMSTaskService bpmsTask;

	@Inject
	private BudgetService budgetService;

	private List<BudgetAreaDTO> budgetsArea ;

	private Double valorTotalBudget;

	@PostConstruct
	private void init()
	{
		if(conversation.isTransient())
				conversation.begin();

	}

	public void obterDadosBudget()
	{
		try {
			ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "anoBudget");
			budgetsArea = budgetService.listarBudgetsAprovadosPorArea(ano);
			calcularValorTotalBudget();

		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter os dados da tarefa");
			logger.error("erro ao obter as variaveis de processo",e);
		}
	}

	public void calcularValorTotalBudget()
	{

		valorTotalBudget= 0d;
		for(BudgetAreaDTO budgetArea : budgetsArea)
		{
			valorTotalBudget+=budgetArea.getValorTotalBudget();
		}
	}

	public void adicionarBudgetsEstipulados() throws Exception
	{
		List<BudgetEstipuladoAno> listaBudgetAno = new ArrayList<>();
		for(BudgetAreaDTO bDto : budgetsArea)
		{
			BudgetEstipuladoAno budget = new BudgetEstipuladoAno();
			budget.setAno(ano);
			Area area = new Area();
			area.setId(bDto.getIdArea());
			budget.setArea(area);
			budget.setValorSubmetido(bDto.getValorTotalBudget());
			budget.setValorAprovado(null);
			listaBudgetAno.add(budget);
		}
		budgetService.adicionarBudgetsSubmetidos(listaBudgetAno);

	}
	public String concluir()
	{
		try {

			adicionarBudgetsEstipulados();
			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,new HashMap<String, Object>());
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
			return "minhasTarefas";
		} catch (Exception e) {
			logger.error("Erro ao concluir a tarefa de Auditoria de Budget",e);
			facesUtils.addErrorMessage("Erro ao concluir a tarefa de Auditoria de Budget");
			return null;
		}

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

	public Double getValorTotalBudget() {
		return valorTotalBudget;
	}

	public void setValorTotalBudget(Double valorTotalBudget) {
		this.valorTotalBudget = valorTotalBudget;
	}


}
