package com.dupont.budget.web;

import javax.enterprise.inject.Model;

import javax.inject.Inject;

import com.dupont.budget.service.bpms.BPMSProcessService;

@Model
public class ProcessoAction {
	@Inject
    private BPMSProcessService bpms;
	
	public void iniciarProcessoBudget()
	{
		try {
			bpms.iniciarProcessoBudget();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
