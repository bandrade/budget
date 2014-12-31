package com.dupont.budget.service;

import com.dupont.budget.model.SolicitacaoPagamento;

/**
 * Servico que controla a solicitação de pagamento.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public interface SolicitacaoPagamentoService {

	/**
	 * Inicia o processo de solicitação de pagamento
	 * @param solicitacaoPagamento solicitaçao de pagamento
	 */
	public void startSolicitacaoPagamento(SolicitacaoPagamento solicitacaoPagamento);
}
