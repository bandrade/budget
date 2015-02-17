package com.dupont.budget.web.actions;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.context.RequestContext;

import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;

/**
 * Action do modal de inclusão de despesa de rateio, do caso de uso de lançamento de despesas.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
@ViewAccessScoped
@Named
public class IncluirDespesaRateioAction extends SolicitacaoPagamentoAction {

	private static final long serialVersionUID = -3764720158261496009L;
	
	private DespesaForecast despesaForecast 						= new DespesaForecast();

	public void incluirDespesaForecast(){
		despesaForecastFlag = !despesaForecastFlag;
	}
	@PostConstruct
	public void initDespesa()
	{
		despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();
	}
	
	public void closeCCDialog(){	
		RequestContext.getCurrentInstance().closeDialog(despesaSolicitacaoPagamento);
		despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();
	}

	public DespesaForecast getDespesaForecast() {
		return despesaForecast;
	}

	public void setDespesaForecast(DespesaForecast despesaForecast) {
		this.despesaForecast = despesaForecast;
	}
	
}
