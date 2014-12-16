package com.dupont.budget.web.actions;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.model.Distrito;
import com.dupont.budget.service.DomainService;
/**
 * Controller das telas de manutenção da entidade distrito
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
public class DistritoAction {

	@Inject
	private DomainService service;
	
	private List<Distrito> distritos;

	public List<Distrito> getDistritos() {

		// Pré popula a lista de distritos
		if (distritos == null)
			distritos = service.findAllDistritos();

		return distritos;
	}
}
