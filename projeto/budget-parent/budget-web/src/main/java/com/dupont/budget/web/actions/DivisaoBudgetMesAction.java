package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@ConversationScoped
@Named
public class DivisaoBudgetMesAction extends AprovarBudgetAction implements Serializable{
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
}
