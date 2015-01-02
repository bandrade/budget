package com.dupont.budget.service.bpms;

import com.dupont.budget.dto.SolicitacaoPagamentoDTO;
import com.dupont.budget.model.Area;


public interface BPMSProcessService {
	long iniciarProcessoBudget(String ano) throws Exception;
	long iniciarProcessoSolicitacaoPagamento(SolicitacaoPagamentoDTO [] solicitacoes) throws Exception;
	Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception;
	boolean existeProcessoAtivo(String ano) throws Exception;

}
