package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import com.dupont.budget.model.Despesa;

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

	public void adicionarDespesa()
	{
		try {
			despesa.setAprovado(true);
			super.adicionarDespesa();
			budgetService.atualizarDespesas(despesasNoDetalhe);
		} catch (Exception e) {
			logger.error("Erro ao incluir a despesa",e);
			facesUtils.addErrorMessage("Erro ao concluir a tarefa de Aprovacao de Budget");
		}
		obterDespesaNoDetalhe(budget.getId());
		validarAprovacao();
	}

	public void validarAprovacao()
	{
		if(tipoAprovacao.equals("S"))
		{
			aprovarReprovarLista(true);
		}
		calcularTotalBudget();

	}
	@Override
	public void calcularTotalBudget() {
		valorTotalDetalhe = 0d;
		for(Despesa despesa : despesasNoDetalhe)
		{
			valorTotalDetalhe+=despesa.getValor();

		}
	}

	public Double getValorTotalAprovado() {
		Double _valor = 0d;
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(despesa.getAprovado() !=null &&  despesa.getAprovado())
				_valor+=despesa.getValor();

		}
		return _valor;
	}


	private boolean possuiRessalva()
	{
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(!despesa.getAprovado())
				return true;
		}
		return false;
	}
	public void aprovarReprovarLista(Boolean b)
	{
		for(Despesa despesa : despesasNoDetalhe)
		{
			despesa.setAprovado(b);
		}
	}
	public void alterarDespesa(){
		try
		{
			super.alterarDespesa();
			budgetService.atualizarDespesas(despesasNoDetalhe);
			obterDespesaNoDetalhe(budget.getId());
			validarAprovacao();
		}	
		catch (Exception e) {
			logger.error("Erro ao incluir a despesa",e);
			facesUtils.addErrorMessage("Erro ao concluir a tarefa de Aprovacao de Budget");
		}

	}
	public String concluir()
	{
		if(valorTotalDetalhe==null || valorTotalDetalhe == 0 )
		{
			facesUtils.addErrorMessage("Nao deve-se concluir a tarefa de Aprovar Budget sem aprovar nenhuma despesa");
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
