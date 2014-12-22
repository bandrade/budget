package com.dupont.budget.web.actions;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.web.util.FacesUtils;

@Model
public class ProcessoAction {
	@Inject
    private BPMSProcessService bpms;

	private String ano;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;


	@PostConstruct
	public void init()
	{
		ano = Calendar.getInstance().get(Calendar.YEAR)+"";
	}

	public void iniciarProcessoBudget()
	{
		try {
			bpms.iniciarProcessoBudget(ano);
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao iniciar processo de Budget.");
			logger.error("Erro ao iniciar processo de Budget", e);
		}
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}



}
