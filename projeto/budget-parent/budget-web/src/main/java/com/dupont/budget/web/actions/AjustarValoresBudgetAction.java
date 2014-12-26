package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetEstipuladoAno;
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

	private BudgetEstipuladoAno budgetEstipuladoAno;

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
		   area = (AreaDTO) bpmsTask.obterConteudoTarefa(idTarefa).get("areaAtual");
		   ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "anoBudget");
		   budgetEstipuladoAno = budgetService.obterValoresAprovadosESubmetidos(area.getId(), ano);
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
			despesasNoDetalhe = budgetService.obterDespesaNoDetalheBudget(budgetSelecionado.getId());
			super.calcularTotalBudget();
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter informações do budget");
			logger.error("Erro ao obter informações do budget",e);
		}
	}

	@Override
	public void adicionarDespesa() {
		despesa.setAprovado(true);
		possuiBudgetSalvo=true;
		//CentroDeCustoDTO centroCusto = new CentroDeCustoDTO();
		//centroCusto.setId(budgetSelecionado.getCentroCusto().getId());
		//setCentroDeCusto(centroCusto);
		setBudget(budgetSelecionado);
		super.adicionarDespesa();
	}

	public String concluir()
	{
		if(getValorTotalBudgetArea().equals(budgetEstipuladoAno.getValorAprovado()))
		{
			return super.concluir();
		}
		else
		{
			facesUtils.addErrorMessage("O valor do budget da Area deve ser igual ao valor aprovado");
			return null;
		}

	}


	@Override
	protected void alterarDespesa() {
		super.alterarDespesa();
	}

	public void removerDespesa()
	{
		super.removerDespesa();
		obterBudgetNoDetalhe();
	}

	public Double getValorTotalBudgetArea()
	{
		Double valor = 0d;
		if(budgets !=null)
		{
			for(Budget budget : budgets)
			{
				valor+= budget.getValorTotalBudget();
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
	public BudgetEstipuladoAno getBudgetEstipuladoAno() {
		return budgetEstipuladoAno;
	}
	public void setBudgetEstipuladoAno(BudgetEstipuladoAno budgetEstipuladoAno) {
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
