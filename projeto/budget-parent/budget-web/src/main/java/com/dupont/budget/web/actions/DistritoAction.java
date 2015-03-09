package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.slf4j.Logger;

import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;
/**
 * Controller das telas de manutenção da entidade distrito
 *
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@ViewAccessScoped
@Named
public class DistritoAction extends GenericAction<Distrito>  implements Serializable{

	@Inject
	private DomainService service;

	private List<Distrito> distritos;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	public List<Distrito> getDistritos() {

		// Pré popula a lista de distritos
		if (distritos == null)
			distritos = service.findAll(Distrito.class);

		return distritos;
	}

	@Named
	@Produces
	public Distrito getDistrito() {
		return getEntidade();
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

	@Override
	public void find() {
		list = service.findByName(getDistrito());
	}
}
