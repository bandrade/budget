package com.dupont.budget.web.actions;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.slf4j.Logger;

import com.dupont.budget.model.Vendedor;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;
/**
 * Controller das telas de manutenção da entidade vendedor
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@ViewAccessScoped @Named
@RolesAllowed(value = "ADMINISTRADOR")
public class VendedorAction extends GenericAction<Vendedor> {

	private static final long serialVersionUID = -9064126463852854590L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@Named
	@Produces
	public Vendedor getVendedor() {
		return getEntidade();
	}

	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void find() {
		list = service.findByName(entidade);
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
}  