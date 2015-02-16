package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.TipoDespesa;

@ConversationScoped
@Named
public class AprovarBudgetAction extends BudgetAction  implements Serializable{
	private String tipoAprovacao;
	@PostConstruct
	private void init(){
		tipoAprovacao="N";
	}
	public void obterDadosBudget() {
		try {
			super.obterDadosBudget();
			if(despesasNoDetalhe == null)
				obterDespesaNoDetalhe(budget.getId());

		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
	}
	
	
	@Override
	public void adicionarDespesaPlanilha(TipoDespesa tipoDespesa) {
		Despesa _despesa = new Despesa();
		_despesa.setAprovado(true);
		_despesa.initLists();
		_despesa.setTipoDespesa(tipoDespesa);
		despesasNoDetalhe.add(_despesa);
		
	}
	public void validarAprovacao()
	{
		if(tipoAprovacao.equals("S"))
		{
			aprovarReprovarLista(true);
		}

	}

	public Double getValorTotalAprovado() {
		Double _valor = 0d;
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(!despesa.isFirstLine() && despesa.getAprovado() !=null &&  despesa.getAprovado() && despesa.getValor()!=null)
				_valor+=despesa.getValor();

		}
		return _valor;
	}


	private boolean possuiRessalva()
	{
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(!despesa.isFirstLine() && !despesa.getAprovado())
				return true;
		}
		return false;
	}
	public void aprovarReprovarLista(Boolean b)
	{
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(!despesa.isFirstLine())
				despesa.setAprovado(b);
		}
	}

	public String concluir()
	{
		if(valorTotalDetalhe==null || valorTotalDetalhe == 0 )
		{
			facesUtils.addErrorMessage("Nao deve-se concluir a tarefa de Aprovar Budget sem aprovar nenhuma despesa");
			return null;
		}
		if(adicionarDespesas())
		{
			facesUtils.addErrorMessage("Existem despesas que não foram preenchidas corretamente. Favor regularizar.");
			return null;
		}
		try {
			budgetService.atualizarDespesas(despesasNoDetalhe);
			params = new HashMap<String,Object>();
			params.put("contemRejeicao", possuiRessalva());
			return super.concluir();
		} catch (Exception e) {
			logger.error("Erro ao concluir a tarefa de Aprovacao de Budget",e);
			facesUtils.addErrorMessage("Erro ao concluir a tarefa de Aprovacao de Budget");
			return null;
		}
	}
	
	public String getTipoAprovacao() {
		return tipoAprovacao;
	}

	public void setTipoAprovacao(String tipoAprovacao) {
		this.tipoAprovacao = tipoAprovacao;
	}


}
