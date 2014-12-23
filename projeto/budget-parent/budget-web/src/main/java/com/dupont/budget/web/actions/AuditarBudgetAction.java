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
public class AuditarBudgetAction extends AreaBudgetAction implements Serializable {

	@PostConstruct
	@Override
	public void init() {
		super.init();
	}

	public void adicionarBudgetsEstipulados() throws Exception
	{
		List<BudgetEstipuladoAno> listaBudgetAno = new ArrayList<>();
		for(int i =0 ; i<budgetsArea.size();i++)
		{
			BudgetAreaDTO bDto = budgetsArea.get(i);
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

}
