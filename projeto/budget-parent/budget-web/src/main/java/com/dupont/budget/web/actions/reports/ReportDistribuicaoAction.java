package com.dupont.budget.web.actions.reports;

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
	
	private Double totalAnoForecast = 0.0;
	
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
	
	public void addTotalAnoForecast(Double valor) {
		totalAnoForecast += valor;
	}
	
	public Double getTotalAnoForecast() {
		return totalAnoForecast;
	}
	
	public Double getTotalPercentagemAnoUtilizado() {
		return (getTotalAnoUtilizado() * 100 ) / totalAnoForecast ; 
	}

}
