package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.BudgetAreaDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetEstipuladoAnoArea;
import com.dupont.budget.model.BudgetEstipuladoAnoCC;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.service.centrodecusto.CentroDeCustoService;


@ConversationScoped
@Named
public class AuditarBudgetAction extends AreaBudgetAction implements Serializable {

	private List<Budget> budgets;
	private BudgetAreaDTO budgetAreaSelecionado;
	
	@Inject
	private CentroDeCustoService centroCustoService;

	@PostConstruct
	@Override
	public void init() {
		super.init();
	}

	public void obterBudgetsArea(){
		try {
			setBudgets(budgetService.obterBudgetsPorArea(budgetAreaSelecionado.getIdArea(), ano));
		} catch (Exception e) {
			logger.error("Erro ao obter budgets da area",e);
			facesUtils.addErrorMessage("Erro ao obter budgets da area");
		}
	}

	public void adicionarBudgetsEstipulados() throws Exception
	{
		List<BudgetEstipuladoAnoArea> listaBudgetAno = new ArrayList<>();
		for(int i =0 ; i<budgetsArea.size();i++)
		{
			BudgetAreaDTO bDto = budgetsArea.get(i);
			BudgetEstipuladoAnoArea budget = new BudgetEstipuladoAnoArea();
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
	
	public void adicionarBudgetsCCEstipulados() throws Exception
	{
		List<BudgetEstipuladoAnoCC> listaBudgetEstipuladoCC = new ArrayList<>();
		for(int i =0 ; i<budgetsArea.size();i++)
		{
			BudgetAreaDTO bDto = budgetsArea.get(i);
			Area area = new Area();
			area.setId(bDto.getIdArea());
			List<CentroCusto> centrosDeCusto = centroCustoService.findByArea(area.getId());
			for(CentroCusto centroCusto :centrosDeCusto)
			{
				BudgetEstipuladoAnoCC budgetEstipuladoCC = new BudgetEstipuladoAnoCC();
				Budget budget = budgetService.findByAnoAndCentroDeCusto(ano, centroCusto.getId());
				budgetEstipuladoCC.setAno(ano);
				budgetEstipuladoCC.setCentroCusto(centroCusto);
				budgetEstipuladoCC.setValorSubmetido(budget.getValorTotalDespesa());
				budgetEstipuladoCC.setValorAprovado(null);
				listaBudgetEstipuladoCC.add(budgetEstipuladoCC);
				
			}
			
		}
		budgetService.adicionarBudgetsSubmetidosCC(listaBudgetEstipuladoCC);

	}
	public String concluir()
	{
		try {

			adicionarBudgetsEstipulados();
			adicionarBudgetsCCEstipulados();
			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,new HashMap<String, Object>());
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
			return "minhasTarefas";
		} catch (Exception e) {
			logger.error("Erro ao concluir a tarefa de Auditoria de Budget",e);
			facesUtils.addErrorMessage("Erro ao concluir a tarefa de Auditoria de Budget");
			return null;
		}

	}

	public List<Budget> getBudgets() {
		return budgets;
	}

	public void setBudgets(List<Budget> budgets) {
		this.budgets = budgets;
	}

	public BudgetAreaDTO getBudgetAreaSelecionado() {
		return budgetAreaSelecionado;
	}

	public void setBudgetAreaSelecionado(BudgetAreaDTO budgetAreaSelecionado) {
		this.budgetAreaSelecionado = budgetAreaSelecionado;
	}


}
