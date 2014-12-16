package com.dupont.budget.web.actions;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.model.Produto;
import com.dupont.budget.service.DomainService;
/**
 * Controller das telas de manutenção da entidade produto
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
public class ProdutoAction {

	@Inject
	private DomainService service;
	
	private List<Produto> produtos;

	public List<Produto> getProdutos() {

		// Pré popula a lista de produtos
		if (produtos == null)
			produtos = service.findAllProdutos();

		return produtos;
	}
}
	