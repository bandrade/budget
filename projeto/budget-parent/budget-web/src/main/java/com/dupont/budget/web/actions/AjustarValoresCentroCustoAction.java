package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.model.BudgetEstipuladoAnoCC;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.TipoDespesa;


/**
 * @author bandrade
 *
 */
@ConversationScoped
@Named
public class AjustarValoresCentroCustoAction extends BudgetAction implements Serializable{

	private BudgetEstipuladoAnoCC budgetEstipuladoAnoCC;


	public void obterDadosBudget() throws Exception{
		if(conversation.isTransient())
			conversation.begin();
		Map<String,Object> dados = bpmsTask.obterConteudoTarefa(idTarefa);
		centroDeCusto = (CentroDeCustoDTO)dados.get("centroCusto");
		ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
		budget = budgetService.findByAnoAndCentroDeCusto(ano, centroDeCusto.getId());
		budgetEstipuladoAnoCC = budgetService.obterValoresAprovadosESubmetidosCC(budget.getCentroCusto().getId(), ano);
		possuiBudgetSalvo = budget !=null;
		if(despesasNoDetalhe == null)
			obterDespesaNoDetalhe(budget.getId());

	}

	@Override
	public void adicionarDespesaPlanilha(TipoDespesa tipoDespesa) {
		Despesa _despesa = new Despesa();
		_despesa.setAprovado(true);
		_despesa.initLists();
		_despesa.setTipoDespesa(tipoDespesa);
		despesasNoDetalhe.add(_despesa);

	}

	public Double calcularTotalValorProposto()
	{
		Double valor =0d;
		if(despesasNoDetalhe!=null)
		{
			for(Despesa despesa: despesasNoDetalhe)
			{
				if( despesa!=null &&  !(despesa.isFirstLine()) && despesa.getAprovado())
				{
					Double valorProposto = despesa.getValorProposto()==null ? despesa.getValor(): despesa.getValorProposto();
					despesa.setValorProposto(valorProposto);
					if(valorProposto!=null)
						valor+=valorProposto;
				}
			}
		}
		return valor;
	}

	public Double calcularTotalValorSubmetido()
	{
		Double valor =0d;
		if(despesasNoDetalhe!=null)
		{
			for(Despesa despesa: despesasNoDetalhe)
			{
				if(!despesa.isFirstLine() && despesa.getAprovado() && despesa.getValor()!=null)
				{
					valor+=despesa.getValor();
				}
			}
		}
		return valor;
	}


	public boolean adicionarDespesas()
	{
		possuiErro=false;
		for(Despesa despesa : despesasNoDetalhe)
		{
			despesa.setAprovado(true);
			if(despesa.isFirstLine())
				continue;
			if(despesa.getId() !=null)
			{
				if(!(alterarDespesa(despesa,true)))
					possuiErro=true;
			}
			else
			{
				if(!(adicionarDespesa(despesa,true)))
					possuiErro=true;
			}
		}
		return possuiErro;
	}

	public String concluir()
	{
		try {
			adicionarDespesas();

			if(!validarPreenchimentoDespesas())
			{
				return null;
			}
			if(!calcularTotalValorSubmetido().equals(budgetEstipuladoAnoCC.getValorAprovado()))
			{
				facesUtils.addErrorMessage("O valor do budget do Centro de Custo deve ser igual ao valor aprovado");
				return null;

			}
			params = new HashMap<>();
			params.put("papel_cc", centroDeCusto.getPapelResponsavel());
			bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,params);
			facesUtils.addInfoMessage("Tarefa concluída com sucesso");

		} catch (Exception e) {
			facesUtils.addInfoMessage("Erro o concluir a tarefa");
			logger.error("Erro o concluir a tarefa: ", e);
		}
		conversation.end();
		return "minhasTarefas";
	}

	private boolean validarPreenchimentoDespesas() {
		boolean bool= true;
		for(Despesa despesa : despesasNoDetalhe)
		{
			if(!despesa.isFirstLine() && !despesa.isPreeenchimentoCompleto())
			{
				facesUtils.addErrorMessage("Linha " +(despesa.getIndice())+ " - Todas as despesas devem ser preenchidas por completo(Produto,Cultura,Acao,Distrito e Valor)");
				bool=false;
			}
		}
		return bool;

	}
	public BudgetEstipuladoAnoCC getBudgetEstipuladoAnoCC() {
		return budgetEstipuladoAnoCC;
	}


	public void setBudgetEstipuladoAnoCC(BudgetEstipuladoAnoCC budgetEstipuladoAnoCC) {
		this.budgetEstipuladoAnoCC = budgetEstipuladoAnoCC;
	}



}
