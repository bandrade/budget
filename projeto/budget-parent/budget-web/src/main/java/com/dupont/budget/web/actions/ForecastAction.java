package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;
@Named
@ConversationScoped
public class ForecastAction implements Serializable {

	@Inject
	protected Conversation conversation;

	@Inject
    protected BPMSProcessService bpmsProcesso;

	@Inject
	protected BPMSTaskService bpmsTask;

	@Inject
	protected ForecastService forecastService;

	protected Long idInstanciaProcesso;
	protected Long idTarefa;

	protected DespesaForecast despesa;

	protected DespesaForecast despesaDetalheSelecionada;

	protected String mes;

	protected String ano;

	@Inject
	protected Logger logger;
	@Inject
	protected FacesUtils facesUtils;

	protected List<DespesaForecast> despesasNoDetalhe;

	protected CentroDeCustoDTO centroDeCusto;

	protected static final String ACAO_EXISTENTE="S";
	protected static final String ACAO_NAO_EXISTENTE="S";

	protected Double valorTotalDetalhe;

	protected ForecastHelper helper;

	@PostConstruct
	private void init()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
			helper = new ForecastHelper();
		}

	}
	public void obterDadosForecast()
	{
		try
		{
		  centroDeCusto = (CentroDeCustoDTO)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "centroDeCusto");
		  mes = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "mes");
		  ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
		  despesasNoDetalhe =  forecastService.obterDespesasForecast(mes, ano, centroDeCusto.getId());

		}
	    catch(Exception e )
		{
			facesUtils.addErrorMessage("Erro ao obter informações da tarefa");
			logger.error("Erro ao obter as informacoes da tarefa",e);

		}

	}



	public Double calcularValorColuna(String mounth)
	{
		return helper.calcularValorColuna(mounth,despesasNoDetalhe);
	}

	public Boolean exibirColuna(String mounth)
	{

		return helper.exibirColuna(mounth, mes);
	}

	public Boolean exibirDespesa(String mounth)
	{

		return helper.exibirDespesa(mounth, mes);
	}

	public void calcularTotalForecast()
	{
		valorTotalDetalhe=0d;
		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			valorTotalDetalhe +=despesa.getValor();
		}

	}



	public Long getIdInstanciaProcesso() {
		return idInstanciaProcesso;
	}

	public void setIdInstanciaProcesso(Long idInstanciaProcesso) {
		this.idInstanciaProcesso = idInstanciaProcesso;
	}

	public Long getIdTarefa() {
		return idTarefa;
	}

	public void setIdTarefa(Long idTarefa) {
		this.idTarefa = idTarefa;
	}

	public DespesaForecast getDespesa() {
		return despesa;
	}

	public void setDespesa(DespesaForecast despesa) {
		this.despesa = despesa;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public List<DespesaForecast> getDespesasNoDetalhe() {
		return despesasNoDetalhe;
	}
	public void setDespesasNoDetalhe(List<DespesaForecast> despesasNoDetalhe) {
		this.despesasNoDetalhe = despesasNoDetalhe;
	}
	public CentroDeCustoDTO getCentroDeCusto() {
		return centroDeCusto;
	}
	public void setCentroDeCusto(CentroDeCustoDTO centroDeCusto) {
		this.centroDeCusto = centroDeCusto;
	}
	public DespesaForecast getDespesaDetalheSelecionada() {
		return despesaDetalheSelecionada;
	}
	public void setDespesaDetalheSelecionada(
			DespesaForecast despesaDetalheSelecionada) {
		this.despesaDetalheSelecionada = despesaDetalheSelecionada;
	}

	public Double getValorTotalDetalhe() {
		return valorTotalDetalhe;
	}
	public void setValorTotalDetalhe(Double valorTotalDetalhe) {
		this.valorTotalDetalhe = valorTotalDetalhe;
	}
	public ForecastHelper getHelper() {
		return helper;
	}
	public void setHelper(ForecastHelper helper) {
		this.helper = helper;
	}


}
