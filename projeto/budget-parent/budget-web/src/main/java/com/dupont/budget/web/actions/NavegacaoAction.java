package com.dupont.budget.web.actions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
@ApplicationScoped
@Named
public class NavegacaoAction {
	private Map<String,String> navegationMap;
	@PostConstruct
	public void init()
	{
		navegationMap = new HashMap<String, String>();
		navegationMap.put("Criar Budget", "criarBudget");
		navegationMap.put("Aprovar Budget", "aprovarBudget");
		navegationMap.put("Auditar Budget", "auditarBudget");
		navegationMap.put("Inserir Budget Aprovado", "inserirBudgetAprovado");
		navegationMap.put("Ajustar o Budget aprovado por CC", "ajustarValoresCC");
		navegationMap.put("Ajustar o Budget aprovado por Area", "ajustarValoresArea");
		navegationMap.put("Dividir budget por mes", "divisaoBudgetMes");
		navegationMap.put("Efetuar Entrada da Nota", "detalharPagamentoDespesa");
		navegationMap.put("Atualizar Forecast", "atualizarForecast");
		navegationMap.put("Aprovacao Lider", "aprovarForecast");
		navegationMap.put("Ajustar Forecast", "ajustarForecast");
	}

	public String obterOutcome(String taskname)
	{
		return navegationMap.get(taskname);
	}
}
