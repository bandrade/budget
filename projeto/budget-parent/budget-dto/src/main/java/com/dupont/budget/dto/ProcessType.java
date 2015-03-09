package com.dupont.budget.dto;

import java.util.ArrayList;
import java.util.List;

public enum ProcessType {
	BUDGET("Criar Budget","AjustarBudget","Criar Budget Centro de Custo"),
	FORECAST("Atualizar Forecast","Atualizar Forecast Centro de Custo "),
	SOLICITACAO_PAGAMENTO("Solicitar Pagamento");
	private List<String> processNames;

	ProcessType(String ... listaProcessos)
	{
		processNames = new ArrayList<String>();
		for(String nome : listaProcessos)
		{
			processNames.add(nome);
		}
	}

	public List<String> getProcessNames() {
		return processNames;
	}

	public void setProcessNames(List<String> processNames) {
		this.processNames = processNames;
	}

}
