package com.dupont.budget.service.bpms;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.bpm.custom.process.BPMProcessManagerApiImpl;
@Model
public class BPMSProcessServiceImpl implements BPMSProcessService{
	@Inject
	private BPMProcessManagerApiImpl processApi ;
	
	public long iniciarProcessoBudget() throws Exception {
		
		return processApi.startBudgetProcess();
	}

	@Override
	public Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception {
		return processApi.getProcessVariable(idProcesso, variavel);
	}
	

}
