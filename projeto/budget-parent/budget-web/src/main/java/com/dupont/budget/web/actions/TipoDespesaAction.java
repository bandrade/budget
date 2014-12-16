package com.dupont.budget.web.actions;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller das telas de manutenção da entidade tipo despesa
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
@RolesAllowed(value = "ADMINISTRADOR")
public class TipoDespesaAction extends GenericAction<TipoDespesa> {

	private static final long serialVersionUID = 2346840808937801813L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@Named
	@Produces
	public TipoDespesa getTipoDespesa() {
		return getEntidade();
	}

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
