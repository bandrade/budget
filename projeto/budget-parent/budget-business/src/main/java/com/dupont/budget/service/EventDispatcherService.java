package com.dupont.budget.service;

import com.dupont.budget.service.event.UploadEvent;


/**
 * Serviço de disparo de eventos assíncronos do sistema.
 * 
 * @author joel
 *
 */
public interface EventDispatcherService {

	/**
	 * Dispara um evento de tratamento de arquivos.
	 * 
	 * @param event
	 */
	void publish(UploadEvent event);

}
