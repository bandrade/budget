package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetEstipuladoAno;
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
			despesasNoDetalhe = budgetService.obterDespesaAprovadasNoDetalheBudget(budgetSelecionado.getId());
			super.calcularTotalBudget();
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter informações do budget");
			logger.error("Erro ao obter informações do budget",e);
		}
	}
	
	public Double calcularTotalValorProposto()
	{
		Double valor =0d;
		if(despesasNoDetalhe!=null)
		{
			for(Despesa despesa: despesasNoDetalhe)
			{
				Double valorProposto = despesa.getValorProposto()==null ? despesa.getValor(): despesa.getValorProposto();
				despesa.setValorProposto(valorProposto);
				valor+=valorProposto;
			}
		}
		return valor;
	}

	@Override
	public boolean adicionarDespesa() {
		despesa.setAprovado(true);
		possuiBudgetSalvo=true;
		setBudget(budgetSelecionado);
		despesa.setValorProposto(despesa.getValor());
		if(super.adicionarDespesa())
		{
			obterBudgetNoDetalhe();
			return true;
		}
		else
		{
			return false;
		}
	}

	public String concluir()
	{
		if(!validarPreenchimentoDespesas())
		{
			facesUtils.addErrorMessage("Todas as despesas devem ser preenchidas por completo(Produto,Cultura,Acao,Distrito,Valor e Comentario)");
			return null;
		}
		if(!getValorTotalBudgetArea().equals(budgetEstipuladoAno.getValorAprovado()))
		{
			facesUtils.addErrorMessage("O valor do budget da Area deve ser igual ao valor aprovado");
			return null;

		}
		return super.concluir();
	}


	private boolean validarPreenchimentoDespesas() {
		for(Budget budget : budgets)
		{
			for(Despesa despesa : budget.getDespesas())
			{
				if(!despesa.isPreeenchimentoCompleto())
				{
					return false;
				}
			}
		}
		return true;

	}
	@Override
	protected boolean alterarDespesa() {
		setBudget(budgetSelecionado);
		if(super.alterarDespesa())
		{
			obterBudgetNoDetalhe();
			return true;
		}
		else
		{
			return false;
		}
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

	public Double getValorTotalPropostoBudgetArea()
	{
		Double valor = 0d;
		if(budgets !=null)
		{
			for(Budget budget : budgets)
			{
				valor+= budget.getValorTotalProposto();
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
