package com.dupont.budget.report.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Detalhe dos relatorio do tipo BUDGET ORÇADO x UTILIZADO (DISTRIBUICAO)
 *
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public class ReportBudgetOrcadoUtilizadoDistribuicaoDetail extends ReportBudgetOrcadoUtilizadoDetail {

	private static final long serialVersionUID = 1952519372479444685L;

	private BigDecimal forecast;

	public ReportBudgetOrcadoUtilizadoDistribuicaoDetail(String detail, BigDecimal orcado, BigDecimal utilizado, BigDecimal forecast) {
		super(detail, orcado, utilizado);

		this.forecast = forecast;
	}

	public BigDecimal getPorcentagemUtilizado() {
		if(forecast.setScale(2,BigDecimal.ROUND_HALF_UP).equals(new BigDecimal(0.0d).setScale(2, BigDecimal.ROUND_HALF_UP)))
			return new BigDecimal(0.0d).setScale(2,BigDecimal.ROUND_HALF_UP);
		return (getUtilizado().divide(forecast) ).multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP) ;
	}

	public BigDecimal getForecast() {
		return forecast;
	}

	public void setForecast(BigDecimal forecast) {
		this.forecast = forecast;
	}

}
