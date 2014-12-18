
package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * @author bandrade
 *
 */
@ConversationScoped
@Named
public class CriarBudgetAction implements Serializable {
	private Budget budget;
	private Despesa despesa;
	private List<Despesa> despesasAgrupadas;
	private Despesa despesaSelecionada;
	private Long idInstanciaProcesso;
	private Long idTarefa;
	@Inject
    private BPMSProcessService bpmsProcesso;
	@Inject
	private BudgetService budgetService;
	@Inject
	private DomainService domainService;
	@Inject
	private Conversation conversation;
	@Inject
	private Logger logger;
	@Inject
	private FacesUtils facesUtils;

	private CentroDeCustoDTO centroDeCusto;
	
	private String ano;
	
	private boolean possuiBudgetSalvo;
	
		
	@PostConstruct
	private void init(){
		conversation.begin();
		despesa = new Despesa();
		despesa.init();
		
	
		
	}
	
	public void obterDadosBudget()
	{
			try {
			   centroDeCusto = (CentroDeCustoDTO)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "centroDeCusto");
			   ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
			   budget = budgetService.findByAnoAndCentroDeCusto(ano, centroDeCusto.getId());
			   possuiBudgetSalvo = budget !=null;
			   if(possuiBudgetSalvo)
			   {
				   despesasAgrupadas = budgetService.obterDespesaAgrupadas(budget.getId());
			   }
			   else
			   {
				   budget = new Budget();
			   }
			   
			} catch (Exception e) {
				facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
				logger.error("Erro ao obter tarefas do usuario.", e);
			}
	}
	
	

	public void adicionarDespesa()
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
		despesa.setTipoDespesa(domainService.findById(despesa.getTipoDespesa()));
		domainService.create(despesa.getAcao());
		budgetService.insertItemDespesa(despesa);
		budget.getDespesas().add(despesa);
		
		despesasAgrupadas = budgetService.obterDespesaAgrupadas(budget.getId());
			
	}
	
	public void criarBudget()
	{
		budget.setAno(ano);
		CentroCusto centroCusto = new CentroCusto();
		centroCusto.setId(centroDeCusto.getId());
		centroCusto= domainService.findById(centroCusto);
		//centroDeCusto= domainService.fi(centroDeCusto);
		budget.setCentroCusto(centroCusto);
		budget.setUsuarioCriador(domainService.getUsuarioByLogin(facesUtils.getUserLogin()));
		budget.setUltimaAtualizacao(new Date());
		budget.setCricao(new Date());
		budget.setProcessInstanceId(idInstanciaProcesso);
		budget = budgetService.insertBudget(budget);
	}
	
	
	public Budget getBudget() {
		return budget;
	}
	public void setBudget(Budget budget) {
		this.budget = budget;
	}
	public Despesa getDespesa() {
		return despesa;
	}
	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public Despesa getDespesaSelecionada() {
		return despesaSelecionada;
	}

	public void setDespesaSelecionada(Despesa despesaSelecionada) {
		this.despesaSelecionada = despesaSelecionada;
	}

	public List<Despesa> getDespesasAgrupadas() {
		return despesasAgrupadas;
	}

	public void setDespesasAgrupadas(List<Despesa> despesasAgrupadas) {
		this.despesasAgrupadas = despesasAgrupadas;
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

	public CentroDeCustoDTO getCentroDeCusto() {
		return centroDeCusto;
	}

	public void setCentroDeCusto(CentroDeCustoDTO centroDeCusto) {
		this.centroDeCusto = centroDeCusto;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}
	
	
	
	
}

