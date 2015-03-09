package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetEstipuladoAnoArea;
import com.dupont.budget.model.BudgetEstipuladoAnoCC;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.service.BudgetService;

@ConversationScoped
@Named
public class AjustarValoresBudgetAction extends BudgetAction implements Serializable{

	@Inject
	private Conversation conversation;

	@Inject
	private BudgetService budgetService;

	private AreaDTO area;

	private List<Budget> budgets;

	private BudgetEstipuladoAnoArea budgetEstipuladoAno;

	private Budget budgetSelecionado;

	@PostConstruct
	private void init()
	{
		if(conversation.isTransient())
			conversation.begin();

	}
	public void obterDadosBudget() {
		try
		{
		   area = (AreaDTO)  bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "areaAtual");
		   ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
		   budgetEstipuladoAno = budgetService.obterValoresAprovadosESubmetidos(area.getId(), ano);
		   if(budgets==null)
			   budgets = budgetService.obterBudgetsPorArea(area.getId(), ano);
		}
		catch(Exception e )
		{
			facesUtils.addErrorMessage("Erro ao obter informações da tarefa");
			logger.error("Erro ao obter as informacoes da tarefa",e);

		}

	}
	public void obterBudgetNoDetalhe(){
		try {
			despesasNoDetalhe = budgetService.obterDespesaAprovadasNoDetalheBudget(budgetSelecionado.getId());
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter informações do budget");
			logger.error("Erro ao obter informações do budget",e);
		}
	}

	public BigDecimal calcularTotalValorProposto()
	{
		BigDecimal valor =new BigDecimal(0d);
		if(despesasNoDetalhe!=null)
		{
			for(Despesa despesa: despesasNoDetalhe)
			{
				BigDecimal valorProposto = despesa.getValorProposto()==null ? new BigDecimal(0d): despesa.getValorProposto();
				despesa.setValorProposto(valorProposto);
				valor=valor.add(valorProposto);
			}
		}
		return valor;
	}


	private boolean validarPreenchimentoDespesas() {
		for(Budget budget : budgets)
		{
			for(Despesa despesa : budget.getDespesas())
			{
				if( despesa!=null && !(despesa.isFirstLine()) && !(despesa.isPreeenchimentoCompleto()))
				{
					return false;
				}
			}
		}
		return true;

	}

	public String concluir()
	{
		try {
			List<BudgetEstipuladoAnoCC> lista = new ArrayList<>();

			for(Budget budget: budgets)
			{

				BudgetEstipuladoAnoCC  budgetEstipuladoCC= budgetService.obterValoresAprovadosESubmetidosCC(budget.getCentroCusto().getId(), ano);
				budgetEstipuladoCC.setValorAprovado(budget.getValorAprovadoBudget());
				lista.add(budgetEstipuladoCC);
			}
			budgetService.adicionarBudgetsSubmetidosCC(lista);

			if(!(getValorTotalBudgetArea().setScale(2,BigDecimal.ROUND_HALF_UP).equals(budgetEstipuladoAno.getValorAprovado().setScale(2,BigDecimal.ROUND_HALF_UP))))
			{
				facesUtils.addErrorMessage("O valor do budget da Area deve ser igual ao valor aprovado");
				return null;

			}

			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,params);
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
			conversation.end();
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao inserir o budget aprovado");
			return null;
		}
		return "minhasTarefas";
	}


	public BigDecimal getValorTotalBudgetArea()
	{
		BigDecimal valor = new BigDecimal(0d);
		if(budgets !=null)
		{
			for(Budget budget : budgets)
			{
				if(budget.getValorAprovadoBudget() !=null)
					valor = valor.add(budget.getValorAprovadoBudget());
			}
		}
		return valor;
	}

	public BigDecimal getValorTotalPropostoBudgetArea()
	{
		BigDecimal valor = new BigDecimal(0d);
		if(budgets !=null)
		{
			for(Budget budget : budgets)
			{
				if(budget.getValorTotalProposto() !=null)
					valor = valor.add(budget.getValorTotalProposto());
			}
		}
		return valor;
	}

	public AreaDTO getArea() {
		return area;
	}
	public void setArea(AreaDTO area) {
		this.area = area;
	}
	public BudgetEstipuladoAnoArea getBudgetEstipuladoAno() {
		return budgetEstipuladoAno;
	}
	public void setBudgetEstipuladoAno(BudgetEstipuladoAnoArea budgetEstipuladoAno) {
		this.budgetEstipuladoAno = budgetEstipuladoAno;
	}
	public List<Budget> getBudgets() {
		return budgets;
	}
	public void setBudgets(List<Budget> budgets) {
		this.budgets = budgets;
	}
	public Budget getBudgetSelecionado() {
		return budgetSelecionado;
	}
	public void setBudgetSelecionado(Budget budgetSelecionado) {
		this.budgetSelecionado = budgetSelecionado;
	}


}
