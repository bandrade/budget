package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Cliente;
import com.dupont.budget.model.Cultura;
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
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@ConversationScoped @Named
public class SolicitacaoPagamentoAction implements Serializable {

	private static final long serialVersionUID = -5396229570158854069L;

	@Inject
	protected BudgetService budgetService;
	
	@Inject
	private ForecastService forecastService;
	
	@Inject 
	private FacesUtils facesUtils;
	
	@Inject
	private SolicitacaoPagamentoService solicitacaoPagamentoService;
	
	@Inject
	protected Conversation conversation;
	
	@Inject
	protected DomainService domainService;
	
	@Inject
	private BPMSTaskService taskService;
	

	@Inject
    protected BPMSProcessService bpmsProcesso;
	
	@Inject
	private LoggedUserAction loggedUserAction;

	private SolicitacaoPagamento solicitacaoPagamento               = new SolicitacaoPagamento();		
	protected DespesaSolicitacaoPagamento despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();	
	private DespesaForecast despesaForecast 						= new DespesaForecast();
	private CentroCusto centroCustoDespesaForecast   				= new CentroCusto();
	
	private List<SolicitacaoPagamento> list;
	private Long tarefa;
	private Long processInstanceId;
	
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
	protected boolean despesaForecastFlag = false; // Propriedade que informa se o usuario incluiu a despesa de forecast
	
	
	private List<DespesaForecast> despesasForecast = new ArrayList<DespesaForecast>();
	
	private Forecast forecast;
	
	private String ano = Calendar.getInstance().get(Calendar.YEAR) + "";
	
	private boolean desabilitarDespesa;
	
	@PostConstruct
	private void init() {
		solicitacaoPagamento = new SolicitacaoPagamento();
		solicitacaoPagamento.setFornecedor(new Fornecedor());
		despesaSolicitacaoPagamento = new DespesaSolicitacaoPagamento();
		despesaForecast = new DespesaForecast();
		despesaForecast.init();
	}
	
	public void load() {
		try {
			DespesaSolicitacaoPagamento despesa = new DespesaSolicitacaoPagamento();
			SolicitacaoPagamentoDTO dto = (SolicitacaoPagamentoDTO) taskService.obterConteudoTarefa(tarefa).get("solicitacaoAtual");
			despesa.setId(dto.getIdDespesa());
			despesa = domainService.findById(despesa);
			this.solicitacaoPagamento = despesa.getSolicitacaoPagamento();
			this.despesaSolicitacaoPagamento = despesa;
		} catch (Exception e) {
			facesUtils.addErrorMessage("Não foi possível recuperar a solicitação");
		}
	}
	
	public String approve() {
		try {
			solicitacaoPagamento = domainService.findById(solicitacaoPagamento);
			solicitacaoPagamento.setStatus(StatusPagamento.ENVIADO_SAP);
			domainService.update(solicitacaoPagamento);
			taskService.aprovarTarefa(facesUtils.getUserLogin(), tarefa, new HashMap<String,Object>());
			facesUtils.addInfoMessage("Tarefa concluida com sucesso");
		} catch (Exception e) {
			facesUtils.addErrorMessage("Não foi possível aprovar a tarefa");
			return null;
		}
		return "minhasTarefas";
		
	}
	public void testeEnvioRelatorio()
	{
		solicitacaoPagamentoService.enviarRelatorioDespesasExternas();
	}
	
	@Produces @Named
	public SolicitacaoPagamento getSolicitacaoPagamento(){
		return solicitacaoPagamento;
	}
	
	@Produces @Named @RequestScoped
	public DespesaSolicitacaoPagamento getDespesaSolicitacaoPagamento(){
		return despesaSolicitacaoPagamento;
	}
	
	@Produces @Named @RequestScoped
	public DespesaForecast getDespesaForecast(){
		return despesaForecast;
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
		list = domainService.listSolicitacaoByFiltro(solicitacaoPagamento.getNumeroNotaFiscal(), solicitacaoPagamento.getTipoSolicitacao(),null, solicitacaoPagamento.getFornecedor().getNome());
	}
	
	/* Inicia o escopo de conversação */
	public void initConversation(){
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
		}

	}
	public void limparCombos()
	{
		produtos = new ArrayList<>();
		distritos = new ArrayList<>();
		culturas= new ArrayList<>();
		vendedores = new ArrayList<>();
		clientes = new ArrayList<>();
	}
	public void doSelectTipoDespesa(){
		
		
		TipoDespesa tipoDespesa = getDespesaSolicitacaoPagamento().getTipoDespesa();
		if(tipoDespesa !=null)
		{
			acoes =domainService.findAcaoDespesaForecastByTipo(forecast.getId(),tipoDespesa.getId());
			limparCombos();
		}
	}
	
	public void doSelectAcao(){
		
		Acao acao = getDespesaSolicitacaoPagamento().getAcao();	
		if(acao!=null)
		{
			try {
				DespesaForecast despesa =forecastService.obterDespesaForecast(forecast, getDespesaSolicitacaoPagamento().getTipoDespesa(),acao);
				limparCombos();
				
				produtos.add(despesa.getProduto());
				distritos.add(despesa.getDistrito());
				if(despesa.getCliente()!=null)
					clientes.add(despesa.getCliente());
				if(despesa.getVendedor()!=null)
					vendedores.add(despesa.getVendedor());
				
				culturas.add(despesa.getCultura());
				
				getDespesaSolicitacaoPagamento().setCultura(despesa.getCultura());
				getDespesaSolicitacaoPagamento().setDistrito(despesa.getDistrito());
				getDespesaSolicitacaoPagamento().setVendedor(despesa.getVendedor());
				getDespesaSolicitacaoPagamento().setCliente(despesa.getCliente());
				getDespesaSolicitacaoPagamento().setProduto(despesa.getProduto());
				
			} catch (Exception e) {
				facesUtils.addErrorMessage("Erro ao obter a despesa");
			}
		}
	}
	
	/* Carrega os combos a aprtir do centro de custo */
	public void doSelectCentroCusto(){	
		
		forecast = forecastService.findForecastByCCAndAno(ano,despesaSolicitacaoPagamento.getCentroCusto().getId());
		limparCombos();
		if(forecast != null)
		{
			tiposDespesas =	domainService.findTiposDespesaForecast(forecast.getId());
			acoes         = new ArrayList<Acao>();
			
		}
		else
		{
			facesUtils.addErrorMessage("O centro de custo nao possui forecast cadastrado" );
		}
		

	}
	
	protected Budget getBudgetDespesaForecast(String ano) {
		return budgetService.findByAnoAndCentroDeCusto(ano, centroCustoDespesaForecast.getId());
	}
	
	protected Budget getBudget(String ano) {
		return budgetService.findByAnoAndCentroDeCusto(ano, despesaSolicitacaoPagamento.getCentroCusto().getId());
	}
	
	/* Salva a solicitacao  */
	public String save(){
		
		
		if(!facesUtils.isDateAfterOrEqualToday(solicitacaoPagamento.getDataPagamento()))
		{
			facesUtils.addErrorMessage("A data de pagamento deve ser uma data futura");
			return null;
		}
		
		solicitacaoPagamento.setCriacao(new Date());
		solicitacaoPagamento.setStatus(StatusPagamento.COMPROMETIDO);
		solicitacaoPagamento.setOrigem(OrigemSolicitacao.COVERSHEET);
		solicitacaoPagamento.setUsuarioCriador(loggedUserAction.getLoggedUser());
		
		if( solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC ) {
			despesaSolicitacaoPagamento.setValor(solicitacaoPagamento.getValor());
			solicitacaoPagamento.addDespesaSolicitacaoPagamento(despesaSolicitacaoPagamento);
		}
		
		
		try {
			solicitacaoPagamentoService.startSolicitacaoPagamento(solicitacaoPagamento);
		} catch (DuplicateEntityException e) {
			facesUtils.addErrorMessage("Já existe um registro com o mesmo número de nota fiscal e fornecedor cadastrado.");
			return null;
		}		
		
		// Caso se optou por cadastrar despesa do forecast
		if( despesaForecastFlag ) {
			try {
				for (DespesaForecast df : despesasForecast) {
					df.setValor(0d);
					forecastService.incluirDespesaForecast(df,(Calendar.getInstance().get(Calendar.MONTH)+1));					
				}
			} catch (Exception e) {
				facesUtils.addErrorMessage(e.getMessage());
				return null;
			}			
		}
		
		conversation.end();
		
		facesUtils.addInfoMessage("Solicitação de pagamento enviada para processamento.");
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
		return "edit.xhtml?faces-redirect=true";
	}
	
	public String update(){
		
		if(solicitacaoPagamento.getStatus().equals(StatusPagamento.ENVIADO_SAP))
				solicitacaoPagamento.setStatus(StatusPagamento.COMPROMETIDO);
	
		if(solicitacaoPagamento.getStatus().equals(StatusPagamento.PENDENTE_VALIDACAO))
		{
			solicitacaoPagamento.setStatus(StatusPagamento.PAGO);
			solicitacaoPagamento.setDataPagamentoRealizado(new Date());
		}
	
		
		if( solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC )
			despesaSolicitacaoPagamento.setValor(solicitacaoPagamento.getValor());
		
		solicitacaoPagamentoService.updateSolicitacaoPagamento(solicitacaoPagamento);
		
		conversation.end();
		
		facesUtils.addInfoMessage("Solicitação de pagamento atualizada e reenviada para processamento.");
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
		return "edit.xhtml?faces-redirect=true";
	}
	
	public void delete(SolicitacaoPagamento _solicitacaoPagamento) {

		try
		{
			boolean processoEmExecucao = bpmsProcesso.isProcessoEmExecucao(_solicitacaoPagamento.getProcessInstanceId());
			if(processoEmExecucao)
				bpmsProcesso.abortarProcesso(_solicitacaoPagamento.getProcessInstanceId());
			domainService.delete(_solicitacaoPagamento);
			getList().remove(_solicitacaoPagamento);
		
			facesUtils.addInfoMessage(String.format("%s removido(a) com sucesso.", "Solicitação de Pagamento"));
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao excluir solictação de pagamento");
		}
		
	}
	
	
	public String edit(SolicitacaoPagamento _solicitacaoPagamento) {	
		
		conversation.begin();
		
		solicitacaoPagamento = solicitacaoPagamentoService.findSolicitacaoPagamento(_solicitacaoPagamento.getId());
		
		// Verifica se pode editar
		if( _solicitacaoPagamento.getStatus() == StatusPagamento.INATIVO || _solicitacaoPagamento.getStatus() == StatusPagamento.PAGO ) {
			
			facesUtils.addErrorMessage("Não é possível editar Solicitações de pagamento inativas ou pagas");
			
			return null;
		}
		if( _solicitacaoPagamento.getStatus().equals(StatusPagamento.PENDENTE_VALIDACAO))
		{
		
			_solicitacaoPagamento.setStatus(StatusPagamento.PAGO);
		}
		if (solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC && !solicitacaoPagamento.getDespesas().isEmpty()) {
			this.despesaSolicitacaoPagamento = solicitacaoPagamento.getDespesas().iterator().next();
			
			Calendar criacao = Calendar.getInstance();
			criacao.setTime(solicitacaoPagamento.getCriacao());
			
			if(!StatusPagamento.PENDENTE_VALIDACAO.equals(solicitacaoPagamento.getStatus()))
					populateAllCombos();
			else
				doSelectCentroCusto();
		} 
		
		
		
		return "/pages/process/pagamento/edit.xhtml";
	}
	
	private void populateAllCombos() {
		doSelectCentroCusto();
		doSelectTipoDespesa();
		doSelectAcao();
		//doSelectProduto();
		//doSelectCultura();
		//doSelectDistrito();
		//doSelectVendedor();
		
	}
	
	public void closeDespesaForecastDialog(){
		Forecast fcast = forecastService.findForecastByCCAndAno(ano,centroCustoDespesaForecast.getId());
		if(fcast ==null)
		{
			facesUtils.addErrorMessage("Centro de custo "+centroCustoDespesaForecast.getCodigo() + " nao possui forecast");
			return ;
		}
		Long budgetId = fcast.getBudget() !=null ?fcast.getBudget().getId():null;
		despesaForecast.setForecast(fcast);
		Acao acao = domainService.findAcaoByForecastOrBudget(budgetId,fcast.getId(),despesaForecast.getAcao().getNome());
		if(acao !=null)
			despesaForecast.setAcao(acao);
		else
		{
			despesaForecast.getAcao().setId(null);
			despesaForecast.getAcao().setForecast(fcast);
			domainService.insertAcao(despesaForecast.getAcao());
		}
		if(despesaForecast.getAcao() !=null && forecastService.isDespesaExistente(despesaForecast))
		{
			facesUtils.addErrorMessage("Não é possível adicionar uma despesa com o mesmo tipo de despesa e ação.");
			return ;
		}
		
		Map<String, Object> objects = new HashMap<String, Object>();
		objects.put("despesaForecast", despesaForecast);
		objects.put("centroCustoDespesaForecast", centroCustoDespesaForecast);
		objects.put("forecast", fcast);
		RequestContext.getCurrentInstance().closeDialog(objects);
		
	}
	
	public void openDespesaForecastDialog() {
		despesaForecast = new DespesaForecast();
		despesaForecast.init();
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		//options.put("height", 265);
		options.put("contentWidth", 600);
		
		RequestContext.getCurrentInstance().openDialog("despesa-forecast-dialog", options, null);
	}

	public void openCCDialog() {
		
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		//options.put("height", 265);
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
	
	/* Ao incluir a DESPESA de forecast, a despesa de solicitação de pagamento fica identica a ela. */
	public void incluirDespesaForecast(SelectEvent event){
		Map<String, Object> objects = (Map<String, Object>) event.getObject();

		centroCustoDespesaForecast = (CentroCusto)     objects.get("centroCustoDespesaForecast");
		despesaForecast 		   = (DespesaForecast) objects.get("despesaForecast");
		Forecast fcast 		   = (Forecast) objects.get("forecast");
		despesaForecast.setForecast(fcast);
		despesasForecast.add(despesaForecast);
		
		if( solicitacaoPagamento.getValor() == null	||  solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC) {
			solicitacaoPagamento.setValor(despesaForecast.getValor());
		}
		else
		{
			solicitacaoPagamento.setValor(solicitacaoPagamento.getValor() + despesaForecast.getValor());
		}
		
		// Seta os mesmo valores 
		despesaSolicitacaoPagamento.setCentroCusto(centroCustoDespesaForecast);
		despesaSolicitacaoPagamento.setTipoDespesa(despesaForecast.getTipoDespesa());
		despesaSolicitacaoPagamento.setAcao(despesaForecast.getAcao());
		despesaSolicitacaoPagamento.setProduto(despesaForecast.getProduto());
		despesaSolicitacaoPagamento.setCultura(despesaForecast.getCultura());
		despesaSolicitacaoPagamento.setDistrito(despesaForecast.getDistrito());
		despesaSolicitacaoPagamento.setVendedor(despesaForecast.getVendedor());
		despesaSolicitacaoPagamento.setCliente(despesaForecast.getCliente());
		despesaSolicitacaoPagamento.setValor(despesaForecast.getValor());
		// troca o flag afirmando que incluiu despesa
		despesaForecastFlag = true;
		
		if(solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.CC) {
			facesUtils.addInfoMessage("Despesa incluida com sucesso. A partir de agora, não é possível editar os dados de Centro de Custo.");
		} else if (solicitacaoPagamento.getTipoSolicitacao() == TipoSolicitacao.RATEIO) {
			solicitacaoPagamento.addDespesaSolicitacaoPagamento(despesaSolicitacaoPagamento);
		}
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

	public CentroCusto getCentroCustoDespesaForecast() {
		return centroCustoDespesaForecast;
	}

	public void setCentroCustoDespesaForecast(CentroCusto centroCustoDespesaForecast) {
		this.centroCustoDespesaForecast = centroCustoDespesaForecast;
	}
	
	public boolean isDespesaForecastFlag() {
		return despesaForecastFlag;
	}

	public boolean isDesabilitarDespesa() {
		return desabilitarDespesa;
	}

	public void setDesabilitarDespesa(boolean desabilitarDespesa) {
		this.desabilitarDespesa = desabilitarDespesa;
	}
	
	
}
