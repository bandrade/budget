package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.PSource;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.model.Acao;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.TipoDespesa;
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


	protected Despesa despesaDetalheSelecionada;


	@Inject
	protected Logger logger;
	@Inject
	protected FacesUtils facesUtils;

	protected Double valorTotalDetalhe;

	protected Map<String, Object> params ;

	protected List<Despesa> despesasNoDetalhe;

	protected boolean possuiBudgetSalvo;
	
	protected TipoDespesa tipoDespesa;
	
	protected List<TipoDespesa> tiposDespesasSelecionadas;
	
	protected boolean possuiErro;

	public void obterDadosBudget() throws Exception{
			if(conversation.isTransient())
				conversation.begin();
		   centroDeCusto = (CentroDeCustoDTO)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "centroDeCusto");
		   ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
		   budget = budgetService.findByAnoAndCentroDeCusto(ano, centroDeCusto.getId());
		   possuiBudgetSalvo = budget !=null;
		   
		   
	}
	
	public List<TipoDespesa> autocompleteTipoDespesa(String input)
	{
		List<TipoDespesa> tiposDespesa = domainService.findAll(TipoDespesa.class);
		if(tiposDespesasSelecionadas !=null )
		{
			for(TipoDespesa tipoDespesa : tiposDespesasSelecionadas)
			{
				tiposDespesa.remove(tipoDespesa);
			}
		}
		
		return facesUtils.autoComplete(tiposDespesa, input) ;
	}
	public void incluirTipoDespesa()
	{
		if(tipoDespesa !=null)
		{
			Despesa d = new Despesa();
			d.setTipoDespesa(tipoDespesa);
			d.setFirstLine(true);
			despesasNoDetalhe.add(d);
			tiposDespesasSelecionadas.add(tipoDespesa);
			setTipoDespesa(null);
		}
	}

	public void adicionarDespesaPlanilha(TipoDespesa tipoDespesa)
	{
		Despesa _despesa = new Despesa();
		_despesa.setTipoDespesa(tipoDespesa);
		despesasNoDetalhe.add(_despesa);
		
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


	public boolean adicionarDespesa(Despesa despesa)
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
			despesa.setBudget(budget);
			validarDadosDespesa(despesa);
			if(despesa.getAcao() !=null && budgetService.isDespesaExistente(despesa))
			{
				facesUtils.addErrorMessage("Nao é possível adicionar uma despesa com o mesmo tipo de despesa e ação.");
				return false;
			}
			budgetService.insertItemDespesa(despesa);
			return true;
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao adicionar a despesa");
			logger.error("Erro ao adicionar a despesa", e);
			return false;
		}
		
	}


	public void removerDespesa()
	{
		if(despesaDetalheSelecionada.getId() !=null)
			domainService.delete(despesaDetalheSelecionada);
		despesasNoDetalhe.remove(despesaDetalheSelecionada);
		calcularTotalBudget();
		facesUtils.addInfoMessage("Despesa removida com sucesso");
	}


	public String concluir()
	{	
		calcularTotalBudget();
		if(valorTotalDetalhe==null || valorTotalDetalhe<=0d)
		{
			facesUtils.addErrorMessage("Nao deve-se concluir a tarefa de Criar Budget sem adicionar nenhuma despesa com valor");
			return null;
		}
		adicionarDespesas();
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
	public void adicionarDespesas()
	{
		possuiErro=false;
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(despesa.getId() !=null)
			{
				alterarDespesa(despesa);
			}
			else
			{
				adicionarDespesa(despesa);
			}
		}
	}

	protected void validarDadosDespesa(Despesa despesa)
	{
		despesa.setProduto(facesUtils.validarCamposDespesaId(despesa.getProduto()));
		despesa.setCultura(facesUtils.validarCamposDespesaId(despesa.getCultura()));
		despesa.setDistrito(facesUtils.validarCamposDespesaId(despesa.getDistrito()));
		despesa.setVendedor(facesUtils.validarCamposDespesaId(despesa.getVendedor()));
		despesa.setCliente(facesUtils.validarCamposDespesaId(despesa.getCliente()));
		validarAcao(despesa);
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
			trataPlanilha();
			calcularTotalBudget();
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao obter despesa no detalhe");
			logger.error("Erro ao obter despesa no detalhe", e);
		}

	}
	public void trataPlanilha()
	{
		List<Despesa> listaTratada = new ArrayList<>();
		TipoDespesa tipoDespesa = null;
		tiposDespesasSelecionadas = new ArrayList<>();
		for(int c = 0 ; c<despesasNoDetalhe.size();c++)
		{
			Despesa desp = despesasNoDetalhe.get(c);
			if(!desp.getTipoDespesa().equals(tipoDespesa) )
			{
				tipoDespesa = desp.getTipoDespesa();
				Despesa d = new Despesa();
				d.setTipoDespesa(tipoDespesa);
				d.setFirstLine(true);
				listaTratada.add(d);
				listaTratada.add(desp);
				tiposDespesasSelecionadas.add(tipoDespesa);
			}
			else
			{
				listaTratada.add(desp);
			}
		}
		
		setDespesasNoDetalhe(listaTratada);
	}
	public void calcularTotalBudget()
	{
		valorTotalDetalhe=0d;
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(despesa.getValor()!=null)
				valorTotalDetalhe +=despesa.getValor();
		}

	}
	protected void validarAcao(Despesa despesa)
	{
		Acao acao = domainService.findAcaoByForecastOrBudget(budget.getId(),null,despesa.getAcao().getNome());
		if(acao !=null)
			despesa.setAcao(acao);
		else
		{
			despesa.getAcao().setId(null);
			despesa.getAcao().setBudget(budget);;
			domainService.insertAcao(despesa.getAcao());
		}
	}

	protected boolean alterarDespesa(Despesa despesa)
	{
		try
		{
			
			validarDadosDespesa(despesa);
			if(despesa.getAcao() !=null && budgetService.isDespesaExistente(despesa))
			{
				Despesa despesaRetorno = budgetService.obterDespesaPorTipoEAcao(despesa);
				if(!(despesaRetorno.getId().equals(despesa.getId())))
				{
					
					facesUtils.addErrorMessage("Nao é possível adicionar uma despesa com o mesmo tipo de despesa e ação.");	
					return false;
				}
			}
			budgetService.updateItemDespesa(despesa);
			calcularTotalBudget();
			facesUtils.addInfoMessage("Despesa alterada com sucesso");
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao alterar despesa");
			logger.error("Erro ao alterar despesa", e);
			return false;
		}
		return true;
	}
	
	
	public List<Acao> obterAcoesPorBudget()
	{
		return domainService.findAcaoByBudget(budget.getId());
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

	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public List<TipoDespesa> getTiposDespesasSelecionadas() {
		return tiposDespesasSelecionadas;
	}

	public void setTiposDespesasSelecionadas(
			List<TipoDespesa> tiposDespesasSelecionadas) {
		this.tiposDespesasSelecionadas = tiposDespesasSelecionadas;
	}

	public boolean isPossuiErro() {
		return possuiErro;
	}

	public void setPossuiErro(boolean possuiErro) {
		this.possuiErro = possuiErro;
	}
	
	
}
