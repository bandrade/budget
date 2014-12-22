package com.dupont.budget.service;

import com.dupont.budget.service.event.FornecedorEvent;

/**
 * Serviço de disparo de eventos assíncronos do sistema.
 * 
 * @author joel
 *
 */
public interface EventDispatcherService {

	/**
	 * Dispara um evento de cadastro de fornecedores.
	 * 
	 * @param event
	 */
	void publish(FornecedorEvent event);

}
