package com.dupont.budget.web.actions;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.DespesaForecast;

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
	
	private DespesaForecast despesaForecast 						= new DespesaForecast();

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
	

	public DespesaForecast getDespesaForecast() {
		return despesaForecast;
	}

	public void setDespesaForecast(DespesaForecast despesaForecast) {
		this.despesaForecast = despesaForecast;
	}
	
}
