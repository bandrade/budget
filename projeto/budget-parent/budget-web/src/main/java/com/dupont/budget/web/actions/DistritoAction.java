package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

import com.dupont.budget.model.Distrito;
import com.dupont.budget.service.DomainService;
/**
 * Controller das telas de manutenção da entidade distrito
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@ViewAccessScoped
@Named
public class DistritoAction implements Serializable{

	@Inject
	private DomainService service;
	
	private List<Distrito> distritos;

	public List<Distrito> getDistritos() {

		// Pré popula a lista de distritos
		if (distritos == null)
			distritos = service.findAll(Distrito.class);

		return distritos;
	}
}
