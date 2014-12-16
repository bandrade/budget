package com.dupont.budget.web.actions;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.service.DomainService;
/**
 * Controller das telas de manutenção da entidade tipo despesa
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
public class TipoDespesaAction {

	@Inject
	private DomainService service;
	
	private List<TipoDespesa> tiposDespesa;

	public List<TipoDespesa> getTiposDespesa() {

		// Pré popula a lista de tipos de despesa
		if (tiposDespesa == null)
			tiposDespesa = service.findAll(TipoDespesa.class);

		return tiposDespesa;
	}
}
	