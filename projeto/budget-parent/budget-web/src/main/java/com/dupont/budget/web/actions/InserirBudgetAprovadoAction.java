package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import com.dupont.budget.dto.BudgetAreaDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.BudgetEstipuladoAno;

@ConversationScoped
@Named
public class InserirBudgetAprovadoAction extends AreaBudgetAction implements
		Serializable {

	private Double valorTotalAprovadoBudget;

	@PostConstruct
	@Override
	public void init() {
		super.init();
	}

	public String concluir()
	{
		try {

			adicionarBudgetsAprovados();
			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,new HashMap<String, Object>());
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
			return "minhasTarefas";
		} catch (Exception e) {
			logger.error("Erro ao concluir a tarefa de Auditoria de Budget",e);
			facesUtils.addErrorMessage("Erro ao concluir a tarefa de Inserir Budgets Aprovados");
			return null;
		}

	}


	public void adicionarBudgetsAprovados() throws Exception
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
			budget.setValorAprovado(bDto.getValorTotalAprovadoBudget());
			listaBudgetAno.add(budget);
		}
		budgetService.adicionarBudgetsSubmetidos(listaBudgetAno);

	}

	public void calcularValorTotalBudgetAprovado()
	{

		valorTotalAprovadoBudget= 0d;
		for(BudgetAreaDTO budgetArea : budgetsArea)
		{
			valorTotalAprovadoBudget+=budgetArea.getValorTotalAprovadoBudget();
		}
	}

	public Double getValorTotalAprovadoBudget() {
		return valorTotalAprovadoBudget;
	}

	public void setValorTotalAprovadoBudget(Double valorTotalAprovadoBudget) {
		this.valorTotalAprovadoBudget = valorTotalAprovadoBudget;
	}




}
