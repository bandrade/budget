package com.dupont.budget.web.actions;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.slf4j.Logger;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.ValorComprometido;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.EventDispatcherService;
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
	
	@Named
	@Produces
	public List<Acao> getAcaoList() {
		return service.findAll(Acao.class);
	}
	
	@Named
	@Produces
	public List<TipoDespesa> getTipoDespesaList() {
		return service.findAll(TipoDespesa.class);
	}
	
	@Override
	public void clearInstance() {
		super.clearInstance();
		entidade.setCentroCusto(new CentroCusto());
		entidade.setAcao(new Acao());
		entidade.setTipoDespesa(new TipoDespesa());
	}

	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void find() {
		list = service.findByExample(entidade);
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
}
