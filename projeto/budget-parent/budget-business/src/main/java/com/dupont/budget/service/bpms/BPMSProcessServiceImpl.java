package com.dupont.budget.service.bpms;

import javax.inject.Inject;

import com.dupont.budget.bpm.custom.ProcessBean;

public class BPMSProcessServiceImpl implements BPMSProcessService{
	@Inject
	private ProcessBean processBean ;
	
	public long iniciarProcessoBudget() throws Exception {
		
		return processBean.startBudgetProcess();
	}
	

}
