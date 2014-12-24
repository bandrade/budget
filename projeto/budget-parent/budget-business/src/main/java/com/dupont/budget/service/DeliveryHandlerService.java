package com.dupont.budget.service;

import com.dupont.budget.service.event.FileUploadEvent;

/**
 * Serviço de recebimento e execução de evento assíncronos da aplicação.
 * 
 * @author joel
 *
 */
public interface DeliveryHandlerService {

	/**
	 * Trata eventos de upload da planilha de fornecedores do sistema.
	 * @param event evento
	 */
	void onFornecedorUpload(FileUploadEvent event);

	/**
	 * Trata eventos de upload da planilha de valores comprometidos do sistema.
	 * 
	 * @param event evento
	 */
	void onValorComprometidoUpload(FileUploadEvent event);

}