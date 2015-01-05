package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.model.Produto;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.TipoSolicitacao;
import com.dupont.budget.model.Vendedor;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.SolicitacaoPagamentoService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller da tela de Solicitação de Pagamento.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@ConversationScoped @Named
public class SolicitacaoPagamentoAction implements Serializable {

	private static final long serialVersionUID = -5396229570158854069L;

	@Inject
	private BudgetService budgetService;
	
	@Inject
	private FacesUtils facesUtils;
	
	@Inject
	private SolicitacaoPagamentoService solicitacaoPagamentoService;
	
	@Inject
	protected Conversation conversation;
	
	@Inject
	private DomainService domainService;
	
	private SolicitacaoPagamento solicitacaoPagamento               = new SolicitacaoPagamento();	
	
	private DespesaSolicitacaoPagamento despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();
	
	private List<SolicitacaoPagamento> list;
	
	@PostConstruct
	private void init() {
		solicitacaoPagamento = new SolicitacaoPagamento();
		solicitacaoPagamento.setFornecedor(new Fornecedor());
		despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();
	}
	
	@Produces @Named
	public SolicitacaoPagamento getSolicitacaoPagamento(){
		return solicitacaoPagamento;
	}
	
	@Produces @Named @RequestScoped
	public DespesaSolicitacaoPagamento getDespesaSolicitacaoPagamento(){
		return despesaSolicitacaoPagamento;
	}
	
	@Produces 
	@Named 
	public TipoSolicitacao[] getTipoSolicitacaoPagamentoList() {
		return TipoSolicitacao.values();
	}
	
	@Produces 
	@Named 
	public StatusPagamento[] getStatusSolicitacaoPagamentoList() {
		return StatusPagamento.values();
	}
	
	public void find() {
		list = domainService.listSolicitacaoByFiltro(solicitacaoPagamento.getNumeroNotaFiscal(), solicitacaoPagamento.getTipoSolicitacao(), solicitacaoPagamento.getStatus(), solicitacaoPagamento.getFornecedor().getNome());
	}
	
	// Listas que populam os combos da tela
	private List<Produto> produtos          = new ArrayList<Produto>();	
	private List<TipoDespesa> tiposDespesas = new ArrayList<TipoDespesa>();
	private List<Cultura> culturas          = new ArrayList<Cultura>();
	private List<Distrito> distritos        = new ArrayList<Distrito>();
	private List<Vendedor> vendedores		= new ArrayList<Vendedor>();
	private List<Acao> acoes				= new ArrayList<Acao>();
	
	// Propriedades da tela
	private String checkAcao = "Existente";	
	private String novaAcao;
	
	/* Inicia o scopo de conversação */
	public void initConversation(){
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
		}

	}
	
	/* Carrega os combos a aprtir do centro de custo */
	public void doSelectCentroCusto(){	
		
		
		String ano = Calendar.getInstance().get(Calendar.YEAR) + "";
		
		// Popula os combos a partir do CENTRO DE CUSTO selecionado
		
		Budget budget = getBudget(ano);

		if( budget == null ) {
			facesUtils.addErrorMessage("Não exite BUDGET cadastro para o centro de custo informado!");
			return;
		}
		
		Set<Despesa> despesas = budget.getDespesas();
		if( despesas == null ) {
			facesUtils.addErrorMessage("Não exite DESPESAS cadastras para o centro de custo informado!");
			return;
		}
		
		
		// Popula os combos da tela 
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
			
			produtos.add(despesa.getProduto());
			
			tiposDespesas.add(despesa.getTipoDespesa());
			
			culturas.add(despesa.getCultura());
			
			distritos.add(despesa.getDistrito());
			
			vendedores.add(despesa.getVendedor());
			
			acoes.add(despesa.getAcao());
		}
		
	}

	protected Budget getBudget(String ano) {
		return budgetService.findByAnoAndCentroDeCusto(ano, despesaSolicitacaoPagamento.getCentroCusto().getId());
	}
	
	/* Salva a solicitacao  */
	public String save(){
		
		// Salvar a nova ação caso tenha sido pedido
		if( getCheckAcao().equals("Criar Nova")){
			Acao acao = new Acao(novaAcao);
			
			acao = domainService.create(acao);
			
			despesaSolicitacaoPagamento.setAcao(acao);
		}
		
		solicitacaoPagamento.addDespesaSolicitacaoPagamento(despesaSolicitacaoPagamento);
		solicitacaoPagamentoService.startSolicitacaoPagamento(solicitacaoPagamento);		
		
		conversation.end();
		
		facesUtils.addInfoMessage("Solicitação de pagamento enviada para processamento.");
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
		return "edit.xhtml?faces-redirect=true";
	}
	
	public void openCCDialog() {
		
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		// options.put("contentHeight", 320);
		options.put("contentWidth", 800);
		
		RequestContext.getCurrentInstance().openDialog("rateio-dialog", options, null);
	}
		
	public void incluirDespesa(SelectEvent event){
		DespesaSolicitacaoPagamento despesa = (DespesaSolicitacaoPagamento) event.getObject();
		
		solicitacaoPagamento.addDespesaSolicitacaoPagamento(despesa);
		
		Double valor = despesa.getValor() + (solicitacaoPagamento.getValor()==null?0.0:solicitacaoPagamento.getValor());
		
		solicitacaoPagamento.setValor(valor);
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}
	
	public List<TipoDespesa> getTiposDespesas() {
		return tiposDespesas;
	}
	
	public List<Cultura> getCulturas() {
		return culturas;
	}
	
	public List<Distrito> getDistritos() {
		return distritos;
	}
	
	public List<Vendedor> getVendedores() {
		return vendedores;
	}
	
	public List<Acao> getAcoes() {
		return acoes;
	}
	
	public String getCheckAcao() {
		return checkAcao;
	}
	
	public void setCheckAcao(String checkAcao) {
		this.checkAcao = checkAcao;
	}

	public String getNovaAcao() {
		return novaAcao;
	}

	public void setNovaAcao(String novaAcao) {
		this.novaAcao = novaAcao;
	}
	
	public List<SolicitacaoPagamento> getList() {
		if (list == null) {
			list = (List<SolicitacaoPagamento>) domainService.findAll(SolicitacaoPagamento.class);
		}
		return list;
	}
}
