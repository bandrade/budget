package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.NamedAbstractEntity;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;

@ConversationScoped
@Named
public class BudgetAction implements Serializable{
	protected CentroDeCustoDTO centroDeCusto;
	protected Budget budget;
	protected Long idInstanciaProcesso;
	protected Long idTarefa;
	protected String ano;
	protected Despesa despesa;

	@Inject
	private Conversation conversation;

	@Inject
    protected BPMSProcessService bpmsProcesso;

	@Inject
	protected BPMSTaskService bpmsTask;

	@Inject
	protected BudgetService budgetService;
	@Inject
	protected DomainService domainService;

	protected boolean inclusao;

	protected String tipoAcao;

	protected Despesa despesaDetalheSelecionada;

	protected static final String ACAO_EXISTENTE="S";
	protected static final String ACAO_NAO_EXISTENTE="S";

	@Inject
	protected Logger logger;
	@Inject
	protected FacesUtils facesUtils;

	protected Double valorTotalDetalhe;

	protected boolean incAltComSucesso=false;

	protected Map<String, Object> params ;

	protected List<Despesa> despesasNoDetalhe;

	protected boolean possuiBudgetSalvo;

	public void obterDadosBudget() throws Exception{
			if(conversation.isTransient())
				conversation.begin();
		   centroDeCusto = (CentroDeCustoDTO)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "centroDeCusto");
		   ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
		   budget = budgetService.findByAnoAndCentroDeCusto(ano, centroDeCusto.getId());
		   possuiBudgetSalvo = budget !=null;
	}

	@PostConstruct
	private void init(){
		inclusao = true;
		inicializarDespesa();
		tipoAcao = ACAO_EXISTENTE;

	}

	public void trataInclusao()
	{
		incAltComSucesso=false;
		inclusao=true;
		inicializarDespesa();
	}
	public void inicializarDespesa(){
		despesa = new Despesa();
		despesa.init();
		tipoAcao = ACAO_EXISTENTE;

	}


	public void criarBudget()
	{
		try
		{

			budget.setAno(ano);
			CentroCusto centroCusto = new CentroCusto();
			centroCusto.setId(centroDeCusto.getId());
			centroCusto= domainService.findById(centroCusto);
			budget.setCentroCusto(centroCusto);
			budget.setUsuarioCriador(domainService.getUsuarioByLogin(facesUtils.getUserLogin()));
			budget.setUltimaAtualizacao(new Date());
			budget.setCricao(new Date());
			budget.setProcessInstanceId(idInstanciaProcesso);
			budget = budgetService.insertBudget(budget);
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao efetuar a criação do budget");
			logger.error("Erro ao efetuar a criação do budget", e);
		}

	}


	public void tratarDadosAlteracao()
	{
		incAltComSucesso=false;
		despesa.initLists();
		if(despesa.getAcao().getId()==null || despesa.getAcao().getId()==0)
		{
			tipoAcao=ACAO_NAO_EXISTENTE;
		}
		else
		{
			tipoAcao = ACAO_EXISTENTE;
		}
		inclusao=false;
	}

	public void adicionarDespesa()
	{
		try
		{
			if(!possuiBudgetSalvo)
			{
				criarBudget();
			}
			if(budget.getDespesas() ==null)
			{
				budget.setDespesas(new HashSet<Despesa>());
			}
			validarDadosDespesa();
			budgetService.insertItemDespesa(despesa);
			incAltComSucesso=true;
			inicializarDespesa();
			facesUtils.addInfoMessage("Despesa adicionada com sucesso");
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao adicionar a despesa");
			logger.error("Erro ao adicionar a despesa", e);
		}
	}


	public void removerDespesa()
	{
		domainService.delete(despesaDetalheSelecionada);
		calcularTotalBudget();
		facesUtils.addInfoMessage("Despesa removida com sucesso");
	}


	public String concluir()
	{
		try {

			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,params);
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
			conversation.end();
			return "minhasTarefas";
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao adicionar a despesa");
			logger.error("Erro ao adicionar a despesa", e);
			return null;
		}

	}

	protected void validarDadosDespesa()
	{
		despesa.setProduto(validarCamposDespesaId(despesa.getProduto()));
		despesa.setCultura(validarCamposDespesaId(despesa.getCultura()));
		despesa.setDistrito(validarCamposDespesaId(despesa.getDistrito()));
		despesa.setVendedor(validarCamposDespesaId(despesa.getVendedor()));
		despesa.setCliente(validarCamposDespesaId(despesa.getCliente()));
		validarAcao();
		despesa.setBudget(budget);
		despesa.setTipoDespesa(domainService.findById(despesa.getTipoDespesa()));
	}
	public void obterDespesaNoDetalhe(Long tipoDeDespesaId,Long budgetId)
	{
		try
		{
			setDespesasNoDetalhe(budgetService.obterDespesaNoDetalhe(tipoDeDespesaId, budgetId));
			calcularTotalBudget();
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao obter despesa no detalhe");
			logger.error("Erro ao obter despesa no detalhe", e);
		}

	}

	public void obterDespesaNoDetalhe(Long budgetId)
	{
		try
		{
			setDespesasNoDetalhe(budgetService.obterDespesaNoDetalheBudget(budgetId));
			calcularTotalBudget();
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao obter despesa no detalhe");
			logger.error("Erro ao obter despesa no detalhe", e);
		}

	}

	public void calcularTotalBudget()
	{
		valorTotalDetalhe=0d;
		for(Despesa despesa : despesasNoDetalhe)
		{
			valorTotalDetalhe +=despesa.getValor();
		}

	}
	protected void validarAcao()
	{
		if(tipoAcao.equals(ACAO_EXISTENTE))
		{
			despesa.setAcao(validarCamposDespesa(despesa.getAcao()));
		}
		else
		{
			despesa.getAcao().setId(null);
			domainService.create(despesa.getAcao());
		}
	}

	protected void alterarDespesa()
	{
		try
		{
			validarDadosDespesa();
			budgetService.updateItemDespesa(despesa);
			incAltComSucesso= true;
			inicializarDespesa();
			calcularTotalBudget();
			facesUtils.addInfoMessage("Despesa alterada com sucesso");
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao alterar despesa");
			logger.error("Erro ao alterar despesa", e);
		}
	}

	protected <T extends NamedAbstractEntity<Long>> T validarCamposDespesa(T entidade)
	{

		if(entidade !=null && entidade.getNome() !=null && !entidade.getNome().trim().equals("") )
		{
			return entidade;
		}
		return null;
	}
	protected <T extends NamedAbstractEntity<Long>> T validarCamposDespesaId(T entidade)
	{

		if(entidade !=null && entidade.getId() !=null && entidade.getId() !=0 )
		{
			return entidade;
		}
		return null;
	}
	public CentroDeCustoDTO getCentroDeCusto() {
		return centroDeCusto;
	}

	public void setCentroDeCusto(CentroDeCustoDTO centroDeCusto) {
		this.centroDeCusto = centroDeCusto;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public boolean isInclusao() {
		return inclusao;
	}

	public void setInclusao(boolean inclusao) {
		this.inclusao = inclusao;
	}

	public String getTipoAcao() {
		return tipoAcao;
	}

	public void setTipoAcao(String tipoAcao) {
		this.tipoAcao = tipoAcao;
	}

	public boolean isIncAltComSucesso() {
		return incAltComSucesso;
	}

	public void setIncAltComSucesso(boolean incAltComSucesso) {
		this.incAltComSucesso = incAltComSucesso;
	}

	public Double getValorTotalDetalhe() {
		return valorTotalDetalhe;
	}

	public void setValorTotalDetalhe(Double valorTotalDetalhe) {
		this.valorTotalDetalhe = valorTotalDetalhe;
	}

	public List<Despesa> getDespesasNoDetalhe() {
		return despesasNoDetalhe;
	}

	public void setDespesasNoDetalhe(List<Despesa> despesasNoDetalhe) {
		this.despesasNoDetalhe = despesasNoDetalhe;
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

	public Despesa getDespesaDetalheSelecionada() {
		return despesaDetalheSelecionada;
	}

	public void setDespesaDetalheSelecionada(Despesa despesaDetalheSelecionada) {
		this.despesaDetalheSelecionada = despesaDetalheSelecionada;
	}


}
