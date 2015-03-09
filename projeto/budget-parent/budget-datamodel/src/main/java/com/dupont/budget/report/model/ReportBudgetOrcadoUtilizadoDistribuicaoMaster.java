package com.dupont.budget.report.model;

import java.math.BigDecimal;
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

	private BigDecimal totalForecast;

	public ReportBudgetOrcadoUtilizadoDistribuicaoMaster(String master, BigDecimal totalOrcado, BigDecimal totalUtilizado, BigDecimal totalForecast) {
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

	public BigDecimal getTotalForecast() {
		return totalForecast;
	}

	public void setTotalForecast(BigDecimal totalForecast) {
		this.totalForecast = totalForecast;
	}

	public BigDecimal getPorcentagemTotalUtilizado() {

		return (getTotalUtilizado().divide(totalForecast) ).multiply(new BigDecimal(100)) ;
	}
}
