package com.dupont.budget.report.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Detalhe dos relatorio do tipo BUDGET ORÇADO x UTILIZADO (DISTRIBUICAO).
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public class ReportBudgetOrcadoUtilizadoDistribuicaoMaster extends ReportBudgetOrcadoUtilizadoMaster {
	
	private static final long serialVersionUID = -1906926859438799505L;
	
	private Double totalForecast;

	public ReportBudgetOrcadoUtilizadoDistribuicaoMaster(String master, Double totalOrcado, Double totalUtilizado, Double totalForecast) {
		super(master, totalOrcado, totalUtilizado);
		
		this.totalForecast = totalForecast;
	}
	
	/* Hack pq o JSF EL não entende herança	 */
	public List<ReportBudgetOrcadoUtilizadoDistribuicaoDetail> getDistribuicaoDetails(){
		
		List<ReportBudgetOrcadoUtilizadoDistribuicaoDetail> result = new ArrayList<ReportBudgetOrcadoUtilizadoDistribuicaoDetail>();
		
		for (ReportBudgetOrcadoUtilizadoDetail item : getDetails()) {
			result.add((ReportBudgetOrcadoUtilizadoDistribuicaoDetail) item);
		}
		
		return result;
	}

	public Double getTotalForecast() {
		return totalForecast;
	}
	
	public void setTotalForecast(Double totalForecast) {
		this.totalForecast = totalForecast;
	}
	
	public Double getPorcentagemTotalUtilizado() {
		
		return (getTotalUtilizado() * 100 ) / totalForecast ; 
	}
}
