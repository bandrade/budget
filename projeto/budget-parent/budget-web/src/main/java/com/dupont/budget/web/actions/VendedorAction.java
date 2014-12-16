package com.dupont.budget.web.actions;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.model.Vendedor;
import com.dupont.budget.service.DomainService;
/**
 * Controller das telas de manutenção da entidade vendedor
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
public class VendedorAction {

	@Inject
	private DomainService service;
	
	private List<Vendedor> vendedores;

	public List<Vendedor> getVendedores() {

		// Pré popula a lista de vendedores
		if (vendedores == null)
			vendedores = service.findAll(Vendedor.class);

		return vendedores;
	}
}  