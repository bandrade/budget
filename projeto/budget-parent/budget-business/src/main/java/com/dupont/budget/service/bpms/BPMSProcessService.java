package com.dupont.budget.service.bpms;


public interface BPMSProcessService {
	long iniciarProcessoBudget(String ano) throws Exception;
	Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception;
	boolean existeProcessoAtivo(String ano);


}
