package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.DetalheValoresComprometidosDTO;
import com.dupont.budget.model.Acao;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.MesEnum;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;
/**
 * @author bandrade
 *
 */
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
	
	@Inject
	protected DomainService domainService;
	
	protected Long idInstanciaProcesso;
	protected Long idTarefa;

	protected DespesaForecast despesa;

	protected DespesaForecast despesaDetalheSelecionada;

	protected String mes;

	protected boolean incAltComSucesso=false;

	protected String ano;

	protected String tipoAcao;

	protected boolean inclusao;

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

	protected Map<String, Object> params ;
	
	protected Forecast forecast;
	
	protected DespesaForecast despesaSelecionada;
	
	protected List<DetalheValoresComprometidosDTO> detalheValorComprometido;


	@PostConstruct
	private void init()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
			helper = new ForecastHelper();
			params = new HashMap<>();
		}

	}
	public void obterDadosForecast()
	{
		try
		{
		  centroDeCusto = (CentroDeCustoDTO)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "centroDeCusto");
		  mes = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "mes");
		  ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
		  if(despesasNoDetalhe==null)
			  despesasNoDetalhe =  forecastService.obterDespesasForecast(mes, ano, centroDeCusto.getId());
		  if(forecast == null)
			  setForecast(forecastService.findForecastByCCAndAno(ano, centroDeCusto.getId()));
		  

		}
	    catch(Exception e )
		{
			facesUtils.addErrorMessage("Erro ao obter informações da tarefa");
			logger.error("Erro ao obter as informacoes da tarefa",e);

		}

	}
	
	public void obterDetalheValorComprometido(String mesDetalhe)
	{
		MesEnum mesEnum = MesEnum.valueOf(mesDetalhe.toUpperCase());
		setDetalheValorComprometido(forecastService.obterDetalheValoresComprometidos(despesaDetalheSelecionada, (int)mesEnum.getId()));
	}

	public void atualizarDetalhe()
	{
		try {
			despesasNoDetalhe =  forecastService.obterDespesasForecast(mes, ano, centroDeCusto.getId());
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter detalhes da despesa");
			logger.error("Erro ao obter detalhes da despesa",e);
		}
		calcularTotalForecast();
	}

	public void trataInclusao()
	{
		incAltComSucesso=false;
		inclusao=true;
		inicializarDespesa();
	}
	public void inicializarDespesa(){
		despesa = new DespesaForecast();
		despesa.init();
		tipoAcao = ACAO_EXISTENTE;

	}


	public boolean adicionarDespesa()
	{
		MesEnum mesEnum = MesEnum.valueOf(mes.toUpperCase());
		if(despesasNoDetalhe !=null && despesasNoDetalhe.size()>0)
		{
			despesa.setForecast(despesasNoDetalhe.get(0).getForecast());
		}
		despesa.setValor(helper.calcularValorMensalisado(mes,despesa));
		if(despesa.getValor() ==null || despesa.getValor()<=0d)
		{
			facesUtils.addErrorMessage("O valor da despesa deve ser maior que zero.");
			return false;
		
		}
		validarAcao();
		if(despesa.getAcao() !=null && forecastService.isDespesaExistente(despesa))
		{
			facesUtils.addErrorMessage("Não é possível adicionar uma despesa com o mesmo tipo de despesa e ação.");
			return false;
		}
		try
		{
			forecastService.incluirDespesaForecast(despesa,(int)mesEnum.getId());
			incAltComSucesso=true;
			inicializarDespesa();
			facesUtils.addInfoMessage("Despesa adicionada com sucesso");
			despesasNoDetalhe =  forecastService.obterDespesasForecast(mes, ano, centroDeCusto.getId());
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao incluir a despesa");
			logger.error("Erro ao incluir a despesa",e);
		}
		return true;
	}
	
	protected void validarAcao()
	{
		if(tipoAcao.equals(ACAO_EXISTENTE))
		{
			despesa.setAcao(facesUtils.validarCamposDespesa(despesa.getAcao()));
		}
		else
		{
			Long budgetId = despesa.getForecast().getBudget() !=null ?despesa.getForecast().getBudget().getId():null;
			Acao acao = domainService.findAcaoByForecastOrBudget(budgetId,despesa.getForecast().getId(),despesa.getAcao().getNome());
			if(acao !=null)
				despesa.setAcao(acao);
			else
			{
				despesa.getAcao().setId(null);
				despesa.getAcao().setForecast(despesa.getForecast());
				domainService.insertAcao(despesa.getAcao());
			}
		}
	}

	public String concluir()
	{
		try {
			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,params);
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
			conversation.end();
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao concluir tarefa");
			logger.error("Erro ao concluir tarefa",e);		}
		return "minhasTarefas";
	}

	public void despesaAlterada(DespesaForecast despesa, String mounth)
	{
		despesa.setAlterada(true);
		despesa.setValor(helper.calcularValorMensalisado(mes,despesa));
		calcularValorColuna(mounth);
		calcularTotalBudget();
	}

	public Double calcularValorColuna(String mounth)
	{
		return helper.calcularValorColuna(mounth,despesasNoDetalhe);
	}

	public Boolean exibirColuna(String mounth)
	{

		return helper.exibirColuna(mounth, mes);
	}


	public Double calcularValorMensalisado()
	{
		return helper.calcularValorMensalisado(mes,despesa);
	}
	public Double calcularTotalBudget()
	{
		Double valor = 0d;
		if (despesasNoDetalhe != null)
		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			valor += despesa.getDespesaBudget() !=null ? despesa.getDespesaBudget().getValor() : 0d;
		}
		return valor;
	}


	public Double calcularTotalAno()
	{
		Double valor = 0d;
		if (despesasNoDetalhe != null)
		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			valor += despesa.getValor();
		}
		return valor;
	}

	public Double calcularTotalYTD()
	{
		Double valor = 0d;
		if (despesasNoDetalhe != null)
		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			valor += helper.getDouble(despesa.getYtd());
		}
		return valor;
	}

	public Double calcularTotalPLM()
	{
		Double valor = 0d;
		if (despesasNoDetalhe != null)
		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			valor += helper.getDouble(despesa.getPlm());
		}
		return valor;
	}


	public void calcularTotalForecast()
	{
		valorTotalDetalhe=0d;
		if (despesasNoDetalhe != null)
		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			valorTotalDetalhe +=despesa.getValor();
		}
	}
	
	
	public List<Acao> obterAcoesPorBudget()
	{
		Long budgetId = forecast !=null && forecast.getBudget() !=null ? forecast.getBudget().getId() : null;
		Long forecastId = forecast !=null ? forecast.getId() : null;
		
		return domainService.findAcaoByForecastOrBudget(budgetId,forecastId); 
	}

	public List<Acao> autocompleteAcao(String input)
	{

		return facesUtils.autoComplete(obterAcoesPorBudget(),input);
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
	public boolean isIncAltComSucesso() {
		return incAltComSucesso;
	}
	public void setIncAltComSucesso(boolean incAltComSucesso) {
		this.incAltComSucesso = incAltComSucesso;
	}
	public String getTipoAcao() {
		return tipoAcao;
	}
	public void setTipoAcao(String tipoAcao) {
		this.tipoAcao = tipoAcao;
	}
	public boolean isInclusao() {
		return inclusao;
	}
	public void setInclusao(boolean inclusao) {
		this.inclusao = inclusao;
	}
	public Forecast getForecast() {
		return forecast;
	}
	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}
	public DespesaForecast getDespesaSelecionada() {
		return despesaSelecionada;
	}
	public void setDespesaSelecionada(DespesaForecast despesaSelecionada) {
		this.despesaSelecionada = despesaSelecionada;
	}
	public List<DetalheValoresComprometidosDTO> getDetalheValorComprometido() {
		return detalheValorComprometido;
	}
	public void setDetalheValorComprometido(
			List<DetalheValoresComprometidosDTO> detalheValorComprometido) {
		this.detalheValorComprometido = detalheValorComprometido;
	}

	
}
