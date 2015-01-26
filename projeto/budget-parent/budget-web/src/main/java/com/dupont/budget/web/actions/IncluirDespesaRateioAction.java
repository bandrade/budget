package com.dupont.budget.web.actions;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;

/**
 * Action do modal de inclusão de despesa de rateio, do caso de uso de lançamento de despesas.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
@ConversationScoped @Named
public class IncluirDespesaRateioAction extends SolicitacaoPagamentoAction {

	private static final long serialVersionUID = -3764720158261496009L;
	
	@Inject
	private BudgetService budgetService;
	
	@Inject
	private DomainService domainService;
	
	private DespesaSolicitacaoPagamento despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();	
	private DespesaForecast despesaForecast 						= new DespesaForecast();

	protected Budget getBudget(String ano) {
		return budgetService.findByAnoAndCentroDeCusto(ano, despesaSolicitacaoPagamento.getCentroCusto().getId());
	}
	
	public void incluirDespesaForecast(){
		despesaForecastFlag = !despesaForecastFlag;
	}
	
	public void closeCCDialog(){	
		
		if( getCheckAcao().equals("Criar Nova")){
			Acao acao = new Acao(getNovaAcao());
			
			acao = domainService.create(acao);
			
			despesaSolicitacaoPagamento.setAcao(acao);
		}
		
		RequestContext.getCurrentInstance().closeDialog(despesaSolicitacaoPagamento);
		
		conversation.end();
	}
	
	public DespesaSolicitacaoPagamento getDespesaSolicitacaoPagamento() {
		return despesaSolicitacaoPagamento;
	}

	public void setDespesaSolicitacaoPagamento(DespesaSolicitacaoPagamento despesaSolicitacaoPagamento) {
		this.despesaSolicitacaoPagamento = despesaSolicitacaoPagamento;
	}

	public DespesaForecast getDespesaForecast() {
		return despesaForecast;
	}

	public void setDespesaForecast(DespesaForecast despesaForecast) {
		this.despesaForecast = despesaForecast;
	}
	
}
