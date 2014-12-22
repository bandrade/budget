
package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.DespesasAgrupadasDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Despesa;

/**
 * @author bandrade
 *
 */
@ConversationScoped
@Named
public class CriarBudgetAction extends BudgetAction implements Serializable {


	private List<DespesasAgrupadasDTO> despesasAgrupadas;
	private DespesasAgrupadasDTO despesaSelecionada;
	private Double valorTotalAgrupado;
	@Inject
	private Conversation conversation;

	public void obterDadosBudget()
	{
			if(conversation.isTransient())
				conversation.begin();
			try {
			  super.obterDadosBudget();

			   if(possuiBudgetSalvo)
			   {
				   despesasAgrupadas = budgetService.obterDespesaAgrupadas(budget.getId());
				   valorTotalAgrupado=0d;
					for(DespesasAgrupadasDTO d: despesasAgrupadas)
					{
						valorTotalAgrupado +=d.getValorAgrupado();
					}
			   }
			   else
			   {
				   budget = new Budget();
			   }

			} catch (Exception e) {
				facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
				logger.error("Erro ao obter tarefas do usuario.", e);
			}
	}

	public void adicionarDespesa()
	{

		super.adicionarDespesa();
		budget.getDespesas().add(despesa);
		try
		{
			despesasAgrupadas = budgetService.obterDespesaAgrupadas(budget.getId());
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao obter despesas.");
			logger.error("Erro ao obter despesas", e);
		}
	}


	public void alterarDespesa(){

		super.alterarDespesa();
		try
		{
			despesasAgrupadas = budgetService.obterDespesaAgrupadas(budget.getId());

		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}

	}

	public void removerDespesa() {
		super.removerDespesa();
		obterDespesaNoDetalhe(despesaDetalheSelecionada.getTipoDespesa().getId(),despesaDetalheSelecionada.getBudget().getId());
	}

	public void obterDespesaDetalhe()
	{
		obterDespesaNoDetalhe(despesaSelecionada.getTipoDespesaId(), budget.getId());
	}

	@Override
	public String concluir() {
		params = new HashMap<String,Object>();
		params.put("_budgetId",budget.getId().toString());
		return super.concluir();
	}

	public List<DespesasAgrupadasDTO> getDespesasAgrupadas() {
		return despesasAgrupadas;
	}

	public void setDespesasAgrupadas(List<DespesasAgrupadasDTO> despesasAgrupadas) {
		this.despesasAgrupadas = despesasAgrupadas;
	}

	public List<Despesa> getDespesasNoDetalhe() {
		return despesasNoDetalhe;
	}

	public void setDespesasNoDetalhe(List<Despesa> despesasNoDetalhe) {
		this.despesasNoDetalhe = despesasNoDetalhe;
	}

	public DespesasAgrupadasDTO getDespesaSelecionada() {
		return despesaSelecionada;
	}

	public void setDespesaSelecionada(DespesasAgrupadasDTO despesaSelecionada) {
		this.despesaSelecionada = despesaSelecionada;
	}

	public Double getValorTotalAgrupado() {
		return valorTotalAgrupado;
	}

	public void setValorTotalAgrupado(Double valorTotalAgrupado) {
		this.valorTotalAgrupado = valorTotalAgrupado;
	}

}

