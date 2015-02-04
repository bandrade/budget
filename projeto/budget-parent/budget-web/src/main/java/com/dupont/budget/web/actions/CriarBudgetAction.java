
package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.HashMap;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.model.Budget;

/**
 * @author bandrade
 *
 */
@ConversationScoped
@Named
public class CriarBudgetAction extends BudgetAction implements Serializable {


	@Inject
	private Conversation conversation;
	
	public void obterDadosBudget()
	{
			if(conversation.isTransient())
			{
				conversation.begin();
				try {
						super.obterDadosBudget();
						if(possuiBudgetSalvo)
						{
								obterDespesaNoDetalhe(budget.getId());
						}
						else
						{
							budget = new Budget();
						}
			
				} catch (Exception e) {
					facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
					logger.error("Erro ao obter tarefas do usuario.", e);
				}
			}
	}


	@Override
	public String concluir() {
		
		params = new HashMap<String,Object>();
		params.put("_budgetId",budget.getId().toString());
		return super.concluir();
	}



	
}

