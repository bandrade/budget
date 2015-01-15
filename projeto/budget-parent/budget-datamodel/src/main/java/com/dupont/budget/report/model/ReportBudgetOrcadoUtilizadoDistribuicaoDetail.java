package com.dupont.budget.report.model;

/**
 * Detalhe dos relatorio do tipo BUDGET ORÇADO x UTILIZADO (DISTRIBUICAO)
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public class ReportBudgetOrcadoUtilizadoDistribuicaoDetail extends ReportBudgetOrcadoUtilizadoDetail {

	private static final long serialVersionUID = 1952519372479444685L;
	
	private Double forecast;

	public ReportBudgetOrcadoUtilizadoDistribuicaoDetail(String detail, Double orcado, Double utilizado, Double forecast) {
		super(detail, orcado, utilizado);
		
		this.forecast = forecast;
	}
	
	public Double getPorcentagemUtilizado() {
		
		return (getUtilizado() * 100 ) / forecast ; 
	}

	public Double getForecast() {
		return forecast;
	}

	public void setForecast(Double forecast) {
		this.forecast = forecast;
	}

}
