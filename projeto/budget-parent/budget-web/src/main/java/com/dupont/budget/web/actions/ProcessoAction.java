package com.dupont.budget.web.actions;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.dupont.budget.model.MesEnum;
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

	private String mes;


	@PostConstruct
	public void init()
	{
		ano = Calendar.getInstance().get(Calendar.YEAR)+"";
	}

	public void iniciarProcessoBudget()
	{
		try {
			if(bpms.existeProcessoAtivo(ano))
			{
				facesUtils.addErrorMessage("Ja existe um processo de budget para o ano vigente");
			}
			else
			{
				bpms.iniciarProcessoBudget(ano);
				facesUtils.addInfoMessage("Processo de budget iniciado com sucesso");
			}
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao iniciar processo de Budget.");
			logger.error("Erro ao iniciar processo de Budget", e);
		}
	}
	public void iniciarProcessoForecast()
	{
		try {

			if(bpms.existeProcessoForecastAtivo(mes, ano))
			{
				facesUtils.addErrorMessage("Ja existe um processo de forecast para o ano "+ano +" e mes de "+mes);
			}
			else
			{
				bpms.iniciarProcessoForecast(ano, mes);
				facesUtils.addInfoMessage("Processo de Forecast iniciado com sucesso");
			}
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao iniciar processo de Forecast.");
			logger.error("Erro ao iniciar processo de Forecast", e);
		}
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public MesEnum [] getMeses()
	{
		return MesEnum.values();
	}



}
