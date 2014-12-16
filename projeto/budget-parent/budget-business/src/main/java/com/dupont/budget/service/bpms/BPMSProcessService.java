package com.dupont.budget.service.bpms;


public interface BPMSProcessService {
	long iniciarProcessoBudget() throws Exception;
	Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception;
	
	
}
