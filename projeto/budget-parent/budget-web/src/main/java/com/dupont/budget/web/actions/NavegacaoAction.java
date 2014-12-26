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
		navegationMap.put("Ajustar o Budget aprovado por Centro de Custo", "ajustarValoresCC");
		navegationMap.put("Dividir budget por mes", "divisaoBudgetMes");

	}

	public String obterOutcome(String taskname)
	{
		return navegationMap.get(taskname);
	}
}
