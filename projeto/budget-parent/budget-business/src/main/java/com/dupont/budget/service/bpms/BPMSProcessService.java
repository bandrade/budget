package com.dupont.budget.service.bpms;

import com.dupont.budget.dto.SolicitacaoPagamentoDTO;


public interface BPMSProcessService {
	long iniciarProcessoBudget(String ano) throws Exception;
	long iniciarProcessoSolicitacaoPagamento(SolicitacaoPagamentoDTO [] solicitacoes) throws Exception;
    long iniciarProcessoForecast(String ano, String mes) throws Exception;
	Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception;
	boolean existeProcessoAtivo(String ano) throws Exception;
	boolean existeProcessoForecastAtivo(String mes,String ano) throws Exception;

}
