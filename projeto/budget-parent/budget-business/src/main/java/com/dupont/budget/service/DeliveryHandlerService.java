package com.dupont.budget.service;

import com.dupont.budget.service.event.FileUploadEvent;

/**
 * Serviço de recebimento e execução de evento assíncronos da aplicação.
 * 
 * @author joel
 *
 */
public interface DeliveryHandlerService {
	
	static final String RELATORIO_SAP_TEMP_DIR = "dupont_carga_sap";

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

	/**
	 * Trata eventos de upload da planilha de notas para a solicitação de pagamento
	 * 
	 * @param event evento
	 */
	void onSolicitacaoPagamentoUpload(FileUploadEvent event);

}
