package com.dupont.budget.web.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.slf4j.Logger;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Cliente;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.Produto;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.ValorComprometido;
import com.dupont.budget.model.Vendedor;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.EventDispatcherService;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller das telas de manutenção da entidade produto
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@ViewAccessScoped @Named
@RolesAllowed(value = "ADMINISTRADOR")
public class ValorComprometidoAction extends AsyncFileUploadAction<ValorComprometido> {

	private static final long serialVersionUID = -9064126463852854590L;

	@Inject
	private DomainService service;
	
	@Inject
	private BudgetService budgetService;
	
	@Inject
	private ForecastService forecastService;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@Inject
	private EventDispatcherService eventDispatcher;

	@Named
	@Produces
	public ValorComprometido getValorComprometido() {
		return getEntidade();
	}
	
	@Named
	@Produces
	public List<CentroCusto> getCentroCustoList() {
		return service.findAll(CentroCusto.class);
	}
	
	private List<TipoDespesa> tiposDespesas = new ArrayList<TipoDespesa>();
	private List<Acao> acoes				= new ArrayList<Acao>();
		
	@Override
	public void clearInstance() {
		super.clearInstance();
		entidade.setCentroCusto(new CentroCusto());
		entidade.setAcao(new Acao());
		entidade.setTipoDespesa(new TipoDespesa());
	}
	
	@Override
	public String edit(ValorComprometido t) {
		
		String result = super.edit(t);
		doSelectCentroCusto();
		doSelectTipoDespesa();
		return result;
	}

	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void find() {
		list = service.findByExample(entidade);
	}
	
	public void doSelectCentroCusto(){	
		
		
		String ano = Calendar.getInstance().get(Calendar.YEAR) + "";
		
		// Popula os combos a partir do CENTRO DE CUSTO selecionado
		
		Budget budget = budgetService.findByAnoAndCentroDeCusto(ano, getValorComprometido().getCentroCusto().getId());
		
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
		
		
		// Popula os combos da tela a partir do budget
		for (Despesa despesa : despesas) {
			
			if( despesa.getAprovado() == false )
				continue;
			
			if(!tiposDespesas.contains(despesa.getTipoDespesa()))
				tiposDespesas.add(despesa.getTipoDespesa());
			
		}
		
		// Popula combos a partir dos forecasts do budget
		List<Forecast> forecasts = forecastService.findForecastsByBudgetId(budget.getId());
		
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
					
				}
			}
		}
	}
	
	public void doSelectTipoDespesa(){
		
		TipoDespesa tipoDespesa = getValorComprometido().getTipoDespesa();
		
		acoes = new ArrayList<Acao>();
		
		String ano = Calendar.getInstance().get(Calendar.YEAR) + "";

		// Popula os combos a partir do CENTRO DE CUSTO selecionado		
		Budget budget = budgetService.findByAnoAndCentroDeCusto(ano, getValorComprometido().getCentroCusto().getId());
		
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
		
		// Popula combos a partir dos forecasts do budget
		List<Forecast> forecasts = forecastService.findForecastsByBudgetId(budget.getId());
				
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

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected DomainService getService() {
		return service;
	}

	@Override
	protected FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public EventDispatcherService getEventDispatcher() {
		return eventDispatcher;
	}

	public void setEventDispatcher(EventDispatcherService eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	public List<TipoDespesa> getTiposDespesas() {
		return tiposDespesas;
	}

	public List<Acao> getAcoes() {
		return acoes;
	}
}
