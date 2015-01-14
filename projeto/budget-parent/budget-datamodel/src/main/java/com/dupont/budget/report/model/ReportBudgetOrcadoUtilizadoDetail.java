package com.dupont.budget.report.model;

import java.io.Serializable;

/**
 * Detalhe dos relatorio do tipo BUDGET ORÇADO x UTILIZADO.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public class ReportBudgetOrcadoUtilizadoDetail implements Serializable {
	
	private static final long serialVersionUID = -6529967707879672477L;
	
	private Double orcado;
	private Double utilizado;
	private String detail;
	
	public ReportBudgetOrcadoUtilizadoDetail(String detail, Double orcado, Double utilizado) {
		this.detail = detail;
		this.orcado = orcado;
		this.utilizado = utilizado;
	}

	public Double getOrcado() {
		return orcado;
	}

	public void setOrcado(Double orcado) {
		this.orcado = orcado;
	}

	public Double getUtilizado() {
		return utilizado;
	}

	public void setUtilizado(Double utilizado) {
		this.utilizado = utilizado;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
