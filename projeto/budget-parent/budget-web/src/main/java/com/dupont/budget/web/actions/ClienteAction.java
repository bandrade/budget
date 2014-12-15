package com.dupont.budget.web.actions;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.model.Cliente;
import com.dupont.budget.service.DomainService;
/**
 * Controller das telas de manutenção da entidade distrito
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
public class ClienteAction {

	@Inject
	private DomainService service;
	
	private List<Cliente> clientes;

	public List<Cliente> getClientes() {

		// Pré popula a lista de clientes
		if (clientes == null)
			clientes = service.findAllClientes();

		return clientes;
	}
}
