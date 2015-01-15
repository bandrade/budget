package com.dupont.budget.web.actions.reports;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.report.model.ReportBudgetOrcadoUtilizadoMaster;
import com.dupont.budget.service.BudgetService;

/**
 * Relatório de BUDGET Orçado x Utilizado: TIPO DE DESPSA / ACAO
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
@Model
public class ReportBudgetOrcadoUtilizadoTipoDespesaAcaoAction extends ReportBudgetOrcadoUtilizadoAction {

	@Inject
	private BudgetService budgetService;
	
	@Override
	protected List<ReportBudgetOrcadoUtilizadoMaster> getReportResult() {
		return budgetService.getBudgetOrcadoUtilizadoTipoDespesaAcaoReport(ano, centroCusto.getId());
	}	
}
