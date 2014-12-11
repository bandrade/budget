package com.dupont.budget.service.bpms;

import javax.inject.Inject;

import com.dupont.budget.bpm.custom.process.BPMProcessManagerApiImpl;

public class BPMSProcessServiceImpl implements BPMSProcessService{
	@Inject
	private BPMProcessManagerApiImpl processApi ;
	
	public long iniciarProcessoBudget() throws Exception {
		
		return processApi.startBudgetProcess();
	}
	

}
