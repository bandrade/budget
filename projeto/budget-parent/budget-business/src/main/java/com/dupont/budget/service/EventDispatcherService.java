package com.dupont.budget.service;

import com.dupont.budget.service.event.FileUploadEvent;


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
	void publish(FileUploadEvent event);

}
