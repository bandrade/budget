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
import com.dupont.budget.model.BudgetEstipuladoAnoArea;

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
			
			if(!validarBudgetAprovado())
			{
				facesUtils.addErrorMessage("A valor aprovado deve ser maior do que zero");
				return null;
			}
			adicionarBudgetsAprovados();
			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,new HashMap<String, Object>());
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
			conversation.end();
			return "minhasTarefas";
		} catch (Exception e) {
			logger.error("Erro ao concluir a tarefa de Auditoria de Budget",e);
			facesUtils.addErrorMessage("Erro ao concluir a tarefa de Inserir Budgets Aprovados");
			return null;
		}

	}

	public boolean validarBudgetAprovado()
	{
		for(BudgetAreaDTO bDto : budgetsArea)
		{
			if(bDto.getValorTotalAprovadoBudget()==null || bDto.getValorTotalAprovadoBudget()<=0d)
				return false;
		}
		return true;
			
	}
	public void adicionarBudgetsAprovados() throws Exception
	{
		List<BudgetEstipuladoAnoArea> listaBudgetAno = new ArrayList<>();
		for(BudgetAreaDTO bDto : budgetsArea)
		{
			BudgetEstipuladoAnoArea budget = new BudgetEstipuladoAnoArea();
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
			if(budgetArea.getValorTotalAprovadoBudget()!=null)
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
