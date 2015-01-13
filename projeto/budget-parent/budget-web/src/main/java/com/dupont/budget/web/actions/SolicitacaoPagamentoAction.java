package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.dupont.budget.dto.SolicitacaoPagamentoDTO;
import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.Acao;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.Cliente;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.model.OrigemSolicitacao;
import com.dupont.budget.model.Produto;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.TipoSolicitacao;
import com.dupont.budget.model.Vendedor;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.service.SolicitacaoPagamentoService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
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
	private ForecastService forecastService;
	
	@Inject
	private FacesUtils facesUtils;
	
	@Inject
	private SolicitacaoPagamentoService solicitacaoPagamentoService;
	
	@Inject
	protected Conversation conversation;
	
	@Inject
	private DomainService domainService;
	
	@Inject
	private BPMSTaskService taskService;
	
	@Inject
	private BPMSProcessService processService;
	
	private SolicitacaoPagamento solicitacaoPagamento               = new SolicitacaoPagamento();	
	
	private DespesaSolicitacaoPagamento despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();
	
	private List<SolicitacaoPagamento> list;
	
	private Long tarefa;
	
	private Long processInstanceId;
	
	@PostConstruct
	private void init() {
		solicitacaoPagamento = new SolicitacaoPagamento();
		solicitacaoPagamento.setFornecedor(new Fornecedor());
		despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();
	}
	
	public void load() {
		try {
			DespesaSolicitacaoPagamento despesa = new DespesaSolicitacaoPagamento();
			SolicitacaoPagamentoDTO dto = (SolicitacaoPagamentoDTO) processService.obterVariavelProcesso(processInstanceId, "solicitacaoAtual");
			despesa.setId(dto.getIdDespesa());
			despesa = domainService.findById(despesa);
			this.solicitacaoPagamento = despesa.getSolicitacaoPagamento();
			this.despesaSolicitacaoPagamento = despesa;
		} catch (Exception e) {
			facesUtils.addErrorMessage("Não foi possível recuperar a solicitação");
		}
	}
	
	public void approve() {
		try {
			solicitacaoPagamento = domainService.findById(solicitacaoPagamento);
			solicitacaoPagamento.setStatus(StatusPagamento.ENVIADO_SAP);
			domainService.update(solicitacaoPagamento);
			taskService.aprovarTarefa(facesUtils.getUserLogin(), tarefa, new HashMap<String,Object>());
		} catch (Exception e) {
			facesUtils.addErrorMessage("Não foi possível aprovar a tarefa");
		}
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
	private List<Cliente> clientes			= new ArrayList<Cliente>();
	
	// Propriedades da tela
	private String checkAcao = "Existente";	
	private String novaAcao;
	
	// Cache
	private Budget budget;
	private List<Forecast> forecasts;
	
	
	/* Inicia o escopo de conversação */
	public void initConversation(){
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
		}

	}
	
	public void doSelectVendedor(){
		
		Vendedor vendedor = getDespesaSolicitacaoPagamento().getVendedor();
		
		clientes      = new ArrayList<Cliente>();
		
		Set<Despesa> despesas = budget.getDespesas();
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
		
			if( !despesa.getVendedor().equals(vendedor) )
				continue;
			
			if( clientes.contains(despesa.getCliente()) )
				continue;
			
			clientes.add(despesa.getCliente());
		}
		
		if(forecasts != null ) {
			for (Forecast forecast : forecasts) {
				
				
				Set<DespesaForecast> _despesas = forecast.getDespesas();
				
				if( _despesas == null)
					continue;
				
				for (DespesaForecast _despesa : _despesas) {
					
					if( _despesa.getAtivo() == false )
						continue;
					
					if( !_despesa.getVendedor().equals(vendedor) )
						continue;
					
					if( clientes.contains(_despesa.getCliente()) )
						continue;
					
					clientes.add(_despesa.getCliente());
				}
			}
		}
		
	}
	
	public void doSelectDistrito(){
		
		Distrito distrito = getDespesaSolicitacaoPagamento().getDistrito();
		
		vendedores    = new ArrayList<Vendedor>();
		clientes      = new ArrayList<Cliente>();
		
		Set<Despesa> despesas = budget.getDespesas();
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
		
			if( !despesa.getDistrito().equals(distrito) )
				continue;
			
			if( vendedores.contains(despesa.getVendedor()) )
				continue;
			
			vendedores.add(despesa.getVendedor());
		}
		
		if(forecasts != null ) {
			for (Forecast forecast : forecasts) {
				
				
				Set<DespesaForecast> _despesas = forecast.getDespesas();
				
				if( _despesas == null)
					continue;
				
				for (DespesaForecast _despesa : _despesas) {
					
					if( _despesa.getAtivo() == false )
						continue;
					
					if( !_despesa.getDistrito().equals(distrito) )
						continue;
					
					if( vendedores.contains(_despesa.getVendedor()) )
						continue;
					
					vendedores.add(_despesa.getVendedor());
				}
			}
		}
	}
	
	public void doSelectCultura(){
		
		Cultura cultura = getDespesaSolicitacaoPagamento().getCultura();
		
		distritos     = new ArrayList<Distrito>();
		vendedores    = new ArrayList<Vendedor>();
		clientes      = new ArrayList<Cliente>();
		
		Set<Despesa> despesas = budget.getDespesas();
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
		
			if( !despesa.getCultura().equals(cultura) )
				continue;
			
			if( distritos.contains(despesa.getDistrito()) )
				continue;
			
			distritos.add(despesa.getDistrito());
		}
		
		if(forecasts != null ) {
			for (Forecast forecast : forecasts) {
				
				
				Set<DespesaForecast> _despesas = forecast.getDespesas();
				
				if( _despesas == null)
					continue;
				
				for (DespesaForecast _despesa : _despesas) {
					
					if( _despesa.getAtivo() == false )
						continue;
					
					if( !_despesa.getCultura().equals(cultura) )
						continue;
					
					if( distritos.contains(_despesa.getDistrito()) )
						continue;
					
					distritos.add(_despesa.getDistrito());
				}
			}
		}
	}
	
	public void doSelectProduto(){
		
		Produto produto = getDespesaSolicitacaoPagamento().getProduto();
		
		culturas      = new ArrayList<Cultura>();
		distritos     = new ArrayList<Distrito>();
		vendedores    = new ArrayList<Vendedor>();
		clientes      = new ArrayList<Cliente>();
		
		Set<Despesa> despesas = budget.getDespesas();
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
		
			if( !despesa.getProduto().equals(produto) )
				continue;
			
			if( culturas.contains(despesa.getCultura()) )
				continue;
			
			culturas.add(despesa.getCultura());
		}
		
		if(forecasts != null ) {
			for (Forecast forecast : forecasts) {
				
				
				Set<DespesaForecast> _despesas = forecast.getDespesas();
				
				if( _despesas == null)
					continue;
				
				for (DespesaForecast _despesa : _despesas) {
					
					if( _despesa.getAtivo() == false )
						continue;
					
					if( !_despesa.getProduto().equals(produto) )
						continue;
					
					if( culturas.contains(_despesa.getCultura()) )
						continue;
					
					culturas.add(_despesa.getCultura());
				}
			}
		}
	}
	
	public void doSelectTipoDespesa(){
		
		TipoDespesa tipoDespesa = getDespesaSolicitacaoPagamento().getTipoDespesa();
		
		acoes         = new ArrayList<Acao>();
		produtos      = new ArrayList<Produto>();
		culturas      = new ArrayList<Cultura>();
		distritos     = new ArrayList<Distrito>();
		vendedores    = new ArrayList<Vendedor>();
		clientes      = new ArrayList<Cliente>();
		
		Set<Despesa> despesas = budget.getDespesas();
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
		
			if( !despesa.getTipoDespesa().equals(tipoDespesa) )
				continue;
			
			if( acoes.contains(despesa.getAcao()) )
				continue;
			
			acoes.add(despesa.getAcao());
		}
		
		if(forecasts != null ) {
			for (Forecast forecast : forecasts) {
				
				
				Set<DespesaForecast> _despesas = forecast.getDespesas();
				
				if( _despesas == null)
					continue;
				
				for (DespesaForecast _despesa : _despesas) {
					
					if( _despesa.getAtivo() == false )
						continue;
					
					if( !_despesa.getTipoDespesa().equals(tipoDespesa) )
						continue;
					
					if( acoes.contains(_despesa.getAcao()) )
						continue;
					
					acoes.add(_despesa.getAcao());
				}
			}
		}
	}
	
	public void doSelectAcao(){
		
		Acao acao = getDespesaSolicitacaoPagamento().getAcao();		
		
		produtos      = new ArrayList<Produto>();
		culturas      = new ArrayList<Cultura>();
		distritos     = new ArrayList<Distrito>();
		vendedores    = new ArrayList<Vendedor>();
		clientes      = new ArrayList<Cliente>();
		
		Set<Despesa> despesas = budget.getDespesas();
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
		
			if( !despesa.getAcao().equals(acao) )
				continue;
			
			if( produtos.contains(despesa.getProduto()) )
				continue;
			
			produtos.add(despesa.getProduto());
		}
		
		if(forecasts != null ) {
			for (Forecast forecast : forecasts) {
				
				
				Set<DespesaForecast> _despesas = forecast.getDespesas();
				
				if( _despesas == null)
					continue;
				
				for (DespesaForecast _despesa : _despesas) {
					
					if( _despesa.getAtivo() == false )
						continue;
					
					if( !_despesa.getAcao().equals(acao) )
						continue;
					
					if( produtos.contains(_despesa.getProduto()) )
						continue;
					
					produtos.add(_despesa.getProduto());
				}
			}
		}
		
	}
	
	/* Carrega os combos a aprtir do centro de custo */
	public void doSelectCentroCusto(){	
		
		
		String ano = Calendar.getInstance().get(Calendar.YEAR) + "";
		
		// Popula os combos a partir do CENTRO DE CUSTO selecionado
		
		budget = getBudget(ano);
		
		if( budget == null ) {
			facesUtils.addErrorMessage("Não exite BUDGET cadastro para o centro de custo informado!");
			return;
		}
		
		Set<Despesa> despesas = budget.getDespesas();
		if( despesas == null ) {
			facesUtils.addErrorMessage("Não exite DESPESAS cadastras para o centro de custo informado!");
			return;
		}
		
		// Limpar combos abaixo da hierarquia
		tiposDespesas = new ArrayList<TipoDespesa>();
		acoes         = new ArrayList<Acao>();
		produtos      = new ArrayList<Produto>();
		culturas      = new ArrayList<Cultura>();
		distritos     = new ArrayList<Distrito>();
		vendedores    = new ArrayList<Vendedor>();
		clientes      = new ArrayList<Cliente>();
		
		
		// Popula os combos da tela a partir do budget
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
			
			if(!tiposDespesas.contains(despesa.getTipoDespesa()))
				tiposDespesas.add(despesa.getTipoDespesa());
			
		}
		
		// Popula combos a partir dos forecasts do budget
		forecasts = forecastService.findForecastsByBudgetId(budget.getId());
		
		if(forecasts != null ) {
			for (Forecast forecast : forecasts) {
				
				
				Set<DespesaForecast> _despesas = forecast.getDespesas();
				
				if( _despesas == null)
					continue;
				
				for (DespesaForecast _despesa : _despesas) {
					
					if( _despesa.getAtivo() == false )
						continue;
					
					if(!tiposDespesas.contains(_despesa.getTipoDespesa()))
						tiposDespesas.add(_despesa.getTipoDespesa());
					
					
//					if(!distritos.contains(_despesa.getDistrito()))
//						distritos.add(_despesa.getDistrito());
//					
//					if(!vendedores.contains(_despesa.getVendedor()))
//						vendedores.add(_despesa.getVendedor());
				}
			}
		}

	}
	
	protected Budget getBudget(String ano) {
		return budgetService.findByAnoAndCentroDeCusto(ano, despesaSolicitacaoPagamento.getCentroCusto().getId());
	}
	
	/* Salva a solicitacao  */
	public String save(){
		
		// Salvar a nova ação caso tenha sido pedido
//		if( getCheckAcao().equals("Criar Nova")){
//			Acao acao = new Acao(novaAcao);
//			
//			acao = domainService.create(acao);
//			
//			despesaSolicitacaoPagamento.setAcao(acao);
//		}
		
		solicitacaoPagamento.setCriacao(new Date());
		solicitacaoPagamento.setStatus(StatusPagamento.COMPROMETIDO);
		solicitacaoPagamento.setOrigem(OrigemSolicitacao.COVERSHEET);
		
		if( solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC ) {
			despesaSolicitacaoPagamento.setValor(solicitacaoPagamento.getValor());
			solicitacaoPagamento.addDespesaSolicitacaoPagamento(despesaSolicitacaoPagamento);
		}
		
		
		try {
			solicitacaoPagamentoService.startSolicitacaoPagamento(solicitacaoPagamento);
		} catch (DuplicateEntityException e) {
			facesUtils.addErrorMessage("Já existe um registro com o mesmo número de nota fiscal cadastrado.");
			return null;
		}		
		
		conversation.end();
		
		facesUtils.addInfoMessage("Solicitação de pagamento enviada para processamento.");
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
		return "edit.xhtml?faces-redirect=true";
	}
	
	public String update(){
		// Salvar a nova ação caso tenha sido pedido
		if( getCheckAcao().equals("Criar Nova")){
			Acao acao = new Acao(novaAcao);
			
			acao = domainService.create(acao);
			
			despesaSolicitacaoPagamento.setAcao(acao);
		}
		
		solicitacaoPagamento.setStatus(StatusPagamento.COMPROMETIDO);
		
		if( solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC )
			despesaSolicitacaoPagamento.setValor(solicitacaoPagamento.getValor());
		
		solicitacaoPagamentoService.updateSolicitacaoPagamento(solicitacaoPagamento);
		
		conversation.end();
		
		facesUtils.addInfoMessage("Solicitação de pagamento atualizada e reenviada para processamento.");
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
		return "edit.xhtml?faces-redirect=true";
	}
	
	public void delete(SolicitacaoPagamento _solicitacaoPagamento) {
		
		domainService.delete(solicitacaoPagamento);
		
		getList().remove(solicitacaoPagamento);
		
		facesUtils.addInfoMessage(String.format("%s removido(a) com sucesso.", "Solicitação de Pagamento"));
		
		
	}
	
	
	public String edit(SolicitacaoPagamento _solicitacaoPagamento) {	
		
		conversation.begin();
		
		solicitacaoPagamento = solicitacaoPagamentoService.findSolicitacaoPagamento(_solicitacaoPagamento.getId());
		
		// Verifica se pode editar
		if( _solicitacaoPagamento.getStatus() == StatusPagamento.INATIVO || _solicitacaoPagamento.getStatus() == StatusPagamento.PAGO ) {
			
			facesUtils.addErrorMessage("Não é possível editar Solicitações de pagamento inativas ou pagas");
			
			return null;
		}
		
		if (solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC && !solicitacaoPagamento.getDespesas().isEmpty()) {
			this.despesaSolicitacaoPagamento = solicitacaoPagamento.getDespesas().get(0);
			
			Calendar criacao = Calendar.getInstance();
			criacao.setTime(solicitacaoPagamento.getCriacao());
			

			budget = getBudget(criacao.get(Calendar.YEAR) + "");
			
			populateAllCombos();
		} 
		
		
		
		return "/pages/process/pagamento/edit.xhtml";
	}
	
	private void populateAllCombos() {
		doSelectCentroCusto();
		doSelectTipoDespesa();
		doSelectAcao();
		doSelectProduto();
		doSelectCultura();
		doSelectDistrito();
		doSelectVendedor();
		
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
	
	public void deleteDespesaSolicitacaoPagamento(DespesaSolicitacaoPagamento despesa) {
		solicitacaoPagamento.removeDespesaSolicitacaoPagamento(despesa);
		
		Double valor = solicitacaoPagamento.getValor() - despesa.getValor() ;
		
		if( valor < 0 )
			valor = 0.0;
		
		solicitacaoPagamento.setValor(valor);
	}
		
	public void incluirDespesa(SelectEvent event){
		DespesaSolicitacaoPagamento despesa = (DespesaSolicitacaoPagamento) event.getObject();
		
		solicitacaoPagamento.addDespesaSolicitacaoPagamento(despesa);
		
		Double valor = despesa.getValor() + (solicitacaoPagamento.getValor()==null?0.0:solicitacaoPagamento.getValor());
		
		solicitacaoPagamento.setValor(valor);
	}
	
	public List<SolicitacaoPagamento> getList() {
		if (list == null) {
			list = (List<SolicitacaoPagamento>) domainService.findAll(SolicitacaoPagamento.class);
		}
		return list;
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

	public Long getTarefa() {
		return tarefa;
	}

	public void setTarefa(Long tarefa) {
		this.tarefa = tarefa;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}
}
