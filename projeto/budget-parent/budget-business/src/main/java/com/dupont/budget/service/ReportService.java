package com.dupont.budget.service;

/**
 * Serviço que fornece os dados de relatorio.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public interface ReportService {

	/**
	 * Exporta uma arquivo EXCEL de todos os dados do banco de dados das tabelas: 
	 * Solicitacao de pagamentos, Budget e Forecast
	 * @return arquivo EXCEL
	 */
	public byte[] exportDatabase();
}
