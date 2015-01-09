package com.dupont.budget.service;

import com.dupont.budget.exception.DuplicateEntityException;
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
	 * @throws DuplicateEntityException 
	 */
	public void startSolicitacaoPagamento(SolicitacaoPagamento solicitacaoPagamento) throws DuplicateEntityException;
	
	/**
	 * Atualiza o procesos de solicitacao de pagamento
	 * @param solicitacaoPagamento
	 */
	public void updateSolicitacaoPagamento(SolicitacaoPagamento solicitacaoPagamento);
	
	
	/**
	 * Retorna a solicitacao de pagamento por id
	 * @param id id da solicitacao do pagamento
	 * @return
	 */
	public SolicitacaoPagamento findSolicitacaoPagamento(Long id);
}
