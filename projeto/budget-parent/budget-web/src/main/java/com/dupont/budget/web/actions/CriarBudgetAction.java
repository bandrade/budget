package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.model.Acao;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Cliente;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.model.Produto;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.Vendedor;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.bpms.BPMSProcessService;

/**
 * @author bandrade
 *
 */
@ConversationScoped
@Named
public class CriarBudgetAction implements Serializable {
	private TarefaDTO tarefa;
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
	
	
	@PostConstruct
	private void init(){
		conversation.begin();
		despesa = new Despesa();
		despesa.setAcao(new Acao());
		despesa.setProduto(new Produto());
		despesa.setCultura(new Cultura());
		despesa.setCliente(new Cliente());
		despesa.setDistrito(new Distrito());
		despesa.setVendedor(new Vendedor());
		despesa.setTipoDespesa(new TipoDespesa());
		tarefa = new TarefaDTO();
		budget = new Budget();
	
		carregarDespesasBudget();
	}
	
	public void obterDadosBudget()
	{

			try {
				CentroDeCustoDTO centroDeCustoDTO = (CentroDeCustoDTO)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "centroDeCusto");
				System.out.println(centroDeCustoDTO.getNome());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	private void carregarDespesasBudget() {
		if(budget !=null && budget.getId() !=null)
		{
			budgetService.obterDespesaAgrupadas(budget.getId());
		}
	}

	public void adicionarDespesa()
	{
		
		if(budget.getDespesas() ==null)
		{
			budget.setDespesas(new HashSet<Despesa>());
		}
		domainService.insertAcao(despesa.getAcao());
		budgetService.insertItemDespesa(despesa);
		budget.getDespesas().add(despesa);
		if(budget.getId() ==null)
		{
			budgetService.insertBudget(budget);
		}
		despesasAgrupadas = budgetService.obterDespesaAgrupadas(budget.getId());
			
	}
	
	public String criarBudget()
	{
		
		return null;
	}
	
	
	public Budget getBudget() {
		return budget;
	}
	public void setBudget(Budget budget) {
		this.budget = budget;
	}
	public TarefaDTO getTarefa() {
		return tarefa;
	}
	public void setTarefa(TarefaDTO tarefa) {
		this.tarefa = tarefa;
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
	
	
	
	
}
