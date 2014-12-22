package com.dupont.budget.service;

import com.dupont.budget.service.event.FornecedorEvent;

/**
 * Serviço de recebimento e execução de evento assíncronos da aplicação.
 * 
 * @author joel
 *
 */
public interface DeliveryHandlerService {

	/**
	 * Recebe eventos de cadastro de fornecedor.
	 * @param event
	 */
	void onNewCadastroFornecedor(FornecedorEvent event);

}
