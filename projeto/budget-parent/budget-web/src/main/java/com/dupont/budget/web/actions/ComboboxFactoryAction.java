package com.dupont.budget.web.actions;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.model.Produto;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.Vendedor;
import com.dupont.budget.service.DomainService;

@Model
public class ComboboxFactoryAction {

	@Inject
	private DomainService service; 
	
	@Produces @Named
	public List<Fornecedor> getFornecedores(){
		
		return service.findByNamedQuery("Fornecedor.findAllAtivos", Fornecedor.class);
	}
	
	@Produces @Named
	public List<CentroCusto> getCentroCustos(){
		
		return service.findAll(CentroCusto.class);
	}
	
	@Produces @Named
	public List<Produto> getProdutos(){		
		
		return service.findAll(Produto.class);
	}
	
	@Produces @Named
	public List<TipoDespesa> getTiposDespesa() {
		
		return service.findAll(TipoDespesa.class);
	}
	
	@Produces @Named
	public List<Cultura> getCulturas() {
		
		return service.findAll(Cultura.class);
	}
	
	@Produces @Named
	public List<Distrito> getDistritos() {
		
		return service.findAll(Distrito.class);
	}
	
	@Produces @Named
	public List<Vendedor> getVendedores() {
		
		return service.findAll(Vendedor.class);
	}
	
	@Produces @Named
	public List<Acao> getAcoes() {
		
		return service.findAll(Acao.class);
	}
}
