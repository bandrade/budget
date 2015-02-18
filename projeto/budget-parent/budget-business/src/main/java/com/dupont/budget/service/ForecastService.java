package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.dto.DetalheValoresComprometidosDTO;
import com.dupont.budget.model.Acao;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.TipoDespesa;

public interface ForecastService {

	void criarPrimeiroForecast(String budgetId) throws Exception;

	void criarForecastSeguinte(String idForecast) throws Exception;

	List<Forecast> findForecastsByBudgetId(Long id);

	List<DespesaForecast> obterDespesasForecast(String mes, String ano,
			Long idCentroCusto) throws Exception;
	
	DespesaForecast obterDespesaForecast(Forecast forecast,
			TipoDespesa tipoDespesa, Acao acao) throws Exception;

	public void incluirDespesaForecast(DespesaForecast despesaForecast,Integer mes) throws Exception;

	void atualizarDespesaForecast(DespesaForecast despesaForecast)
			throws Exception;

	Double obterValoresComprometidosNotas(DespesaForecast despesaForecast,int mes);

	boolean isForecastMensalisado(Long mes, String ano);

	void alterarForecastMensalisado(Long mes, String ano, long processInstanceId) throws Exception;

	Forecast findForecastByCCAndAno(String ano, Long centroCustoId);
	
	public boolean isDespesaExistente(DespesaForecast despesa);
	
	public DespesaForecast obterDespesaPorTipoEAcao(DespesaForecast despesa);
	
	public List<DetalheValoresComprometidosDTO> obterDetalheValoresComprometidosNotas(DespesaForecast despesaForecast, int mes);
	
	public List<DetalheValoresComprometidosDTO> obterDetalheValoresComprometidos(DespesaForecast despesa, int mes); 

}
