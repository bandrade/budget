package com.dupont.budget.web.actions.reports;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.report.model.ReportBudgetOrcadoUtilizadoDistribuicaoMaster;
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
public class ReportDistribuicaoAction extends ReportBudgetOrcadoUtilizadoAction {

	@Inject
	private BudgetService budgetService;

	private List<ReportBudgetOrcadoUtilizadoDistribuicaoMaster> report;

	private BigDecimal totalAnoForecast = new BigDecimal(0d);

	@Override
	public void doReport(){
		report = budgetService.getBudgetOrcadoUtilizadoTipoDespesaAcaoDistribuicaoReport(ano, centroCusto.getId());

		if( report == null || report.isEmpty() ) {
			facesUtils.addInfoMessage("Não existe relatório disponível para o ANO e CENTRO DE CUSTO informados.");
			return;
		}

		for (ReportBudgetOrcadoUtilizadoDistribuicaoMaster reportBudgetOrcadoUtilizadoMaster : report) {
			addTotalAnoOrcado(reportBudgetOrcadoUtilizadoMaster.getTotalOrcado());
			addTotalAnoUtilizado(reportBudgetOrcadoUtilizadoMaster.getTotalUtilizado());
			addTotalAnoForecast(reportBudgetOrcadoUtilizadoMaster.getTotalForecast());
		}

	}

	@Override
	protected List<ReportBudgetOrcadoUtilizadoMaster> getReportResult() {
		return null;
	}

	public List<ReportBudgetOrcadoUtilizadoDistribuicaoMaster> getReportDistribuicao() {
		return report;
	}

	public void addTotalAnoForecast(BigDecimal valor) {
		totalAnoForecast = totalAnoForecast.add(valor);
	}

	public BigDecimal getTotalAnoForecast() {
		return totalAnoForecast;
	}

	public BigDecimal getTotalPercentagemAnoUtilizado() {
		return (getTotalAnoUtilizado().multiply(new BigDecimal(100) )).divide(totalAnoForecast) ;
	}

}
