package com.dupont.budget.web.actions;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.slf4j.Logger;

import com.dupont.budget.model.Cliente;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller das telas de manutenção da entidade distrito
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@ViewAccessScoped @Named
@RolesAllowed(value = "ADMINISTRADOR")
public class ClienteAction extends GenericAction<Cliente> {

	private static final long serialVersionUID = -6158334629049467790L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@Named
	@Produces
	public Cliente getCliente() {
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
