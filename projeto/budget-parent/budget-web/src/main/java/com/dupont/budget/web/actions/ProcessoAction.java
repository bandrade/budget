package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.model.MesEnum;
import com.dupont.budget.model.ToleranciaForecast;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.web.util.FacesUtils;

@SessionScoped
@Named
public class ProcessoAction implements Serializable{
	@Inject
    private BPMSProcessService bpms;

	private String ano;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	private String mes;
	
	private Date dataExpiracao;

	private Calendar calendar;
	
	@Inject
	private ForecastService forecastService;
	
	private ToleranciaForecast toleranciaForecast;
	
	@Inject
	private DomainService domainService;

	@PostConstruct
	public void init()
	{
		calendar = Calendar.getInstance();  
		ano = calendar.get(Calendar.YEAR)+"";
		
		List<ToleranciaForecast> lista = domainService.findAll(ToleranciaForecast.class);
		if(lista !=null && lista.size()>0)
		{
			toleranciaForecast = lista.get(0);
		}
		else
		{
			toleranciaForecast = new ToleranciaForecast();
		}
	}

	private boolean validarPrazoBudget()
	{
		boolean isPrazoOk= true;
		if(dataExpiracao.before(new Date()))
		{
			facesUtils.addErrorMessage("O prazo deve ser uma data futura");
			isPrazoOk=false;
		}
		
		if(dataExpiracao.before(new Date()))
		{
			facesUtils.addErrorMessage("O prazo deve ser uma data futura");
			isPrazoOk=false;
		}
		return isPrazoOk;
		
	}
	
	public void limparValorToleranciaPositiva()
	{
		this.toleranciaForecast.setValorToleranciaPositiva(null);
	}
	public void limparValorToleranciaNegativa()
	{
		this.toleranciaForecast.setValorToleranciaNegativa(null);
	}
	private boolean validarPrazoForecast() throws Exception
	{

		if(bpms.existeProcessoAtivo(ano))
		{
			facesUtils.addErrorMessage("O processo de budget deve ser finalizado");
			return false;
		}
		calendar.setTime(dataExpiracao);
		if(!facesUtils.isDateAfterOrEqualToday(dataExpiracao))
		{
			facesUtils.addErrorMessage("O prazo deve ser uma data futura");
			return false;
		}
				
		MesEnum mesPrazo = MesEnum.values()[calendar.get(Calendar.MONTH)];
		MesEnum mesForecast = MesEnum.obterMes(mes);
		if(mesForecast.getId()>mesPrazo.getId())
		{
			facesUtils.addErrorMessage("O prazo não pode ter uma data inferior ao mes do Forecast");
			return false;
		}
		if(!(forecastService.isForecastMensalisado(mesForecast.getId(), ano)))
		{
			facesUtils.addErrorMessage("Ja existe um processo de processo completo/em andamento para o mes de "+mes);
			return false;
		}
		
		return true;
	}

	
	public void iniciarProcessoBudget()
	{
		if(!validarPrazoBudget())
		{
			return;
		}
		try {
			
			if(bpms.existeProcessoAtivo(ano))
			{
				facesUtils.addErrorMessage("Ja existe um processo de budget para o ano vigente");
			}
			else
			{
				bpms.iniciarProcessoBudget(ano,dataExpiracao);
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
			if(!validarPrazoForecast())
			{
				return;
			}	
			if(bpms.existeProcessoForecastAtivo(ano))
			{
				facesUtils.addErrorMessage("Ja existe um processo de forecast ativo");
			}
			else
			{
				if(toleranciaForecast.getId() !=null)
				{
					domainService.update(toleranciaForecast);
				}
				else
				{
					domainService.create(toleranciaForecast);
				}
				long processInstanceId =bpms.iniciarProcessoForecast(ano, mes,dataExpiracao,toleranciaForecast);
				
				MesEnum mesForecast = MesEnum.obterMes(mes);
				forecastService.alterarForecastMensalisado((Long)(mesForecast.getId()), ano,processInstanceId);
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

	public Date getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(Date dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public ToleranciaForecast getToleranciaForecast() {
		return toleranciaForecast;
	}

	public void setToleranciaForecast(ToleranciaForecast toleranciaForecast) {
		this.toleranciaForecast = toleranciaForecast;
	}
	
	public void doWorkAround()
	{
		
	}
	

}
