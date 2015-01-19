package com.dupont.budget.web.actions;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Cliente;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.model.Produto;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.Vendedor;
import com.dupont.budget.web.util.FacesUtils;

@Model
public class AutocompleteAction {

	@Inject
	private FacesUtils facesUtils;

	@Inject
	private DistritoAction distritoAction;

	@Inject
	private ClienteAction clienteAction;

	@Inject
	private VendedorAction vendedorAction;

	@Inject
	private CulturaAction culturaAction;

	@Inject
	private ProdutoAction produtoAction;

	@Inject
	private TipoDespesaAction tipoDespesaAction;
	
	@Inject
	private CentroCustoAction centroCustoAction;


	@Inject
	private AcaoAction acaoAction;


	public List<Distrito> autocompleteDistrito(String input)
	{

		return facesUtils.autoComplete(distritoAction.getDistritos(),input);
	}

	public List<TipoDespesa> autocompleteTipoDespesa(String input)
	{

		return facesUtils.autoComplete(tipoDespesaAction.getList(),input);
	}


	public List<Cultura> autocompleteCultura(String input)
	{

		return facesUtils.autoComplete(culturaAction.getList(),input);
	}


	public List<Produto> autocompleteProduto(String input)
	{

		return facesUtils.autoComplete(produtoAction.getList(),input);
	}

	public List<Vendedor> autocompleteVendedor(String input)
	{

		return facesUtils.autoComplete(vendedorAction.getList(),input);
	}
	public List<Cliente> autocompleteCliente(String input)
	{

		return facesUtils.autoComplete(clienteAction.getList(),input);
	}

	public List<Acao> autocompleteAcao(String input)
	{

		return facesUtils.autoComplete(acaoAction.getList(),input);
	}

	public List<CentroCusto> autoCompleteCentroCusto(String input)
	{
		return facesUtils.autoComplete(centroCustoAction.getList(),input);
	}

}
