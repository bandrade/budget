package com.dupont.budget.service.bpms;

import java.util.Date;

import com.dupont.budget.dto.SolicitacaoPagamentoDTO;


public interface BPMSProcessService {
	long iniciarProcessoBudget(String ano,Date prazo) throws Exception;
	long iniciarProcessoSolicitacaoPagamento(SolicitacaoPagamentoDTO [] solicitacoes) throws Exception;
    long iniciarProcessoForecast(String ano, String mes,Date prazo) throws Exception;
	Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception;
	boolean existeProcessoAtivo(String ano) throws Exception;
	boolean existeProcessoForecastAtivo(String ano) throws Exception;

}
