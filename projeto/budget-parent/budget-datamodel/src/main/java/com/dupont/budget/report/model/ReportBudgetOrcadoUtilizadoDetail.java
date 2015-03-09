package com.dupont.budget.report.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Detalhe dos relatorio do tipo BUDGET ORÇADO x UTILIZADO.
 *
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public class ReportBudgetOrcadoUtilizadoDetail implements Serializable {

	private static final long serialVersionUID = -6529967707879672477L;

	private BigDecimal orcado;
	private BigDecimal utilizado;
	private String detail;

	public ReportBudgetOrcadoUtilizadoDetail(String detail, BigDecimal orcado, BigDecimal utilizado) {
		this.detail = detail;
		this.orcado = orcado;
		this.utilizado = utilizado;
	}

	public BigDecimal getOrcado() {
		return orcado;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

	public BigDecimal getUtilizado() {
		return utilizado;
	}

	public void setUtilizado(BigDecimal utilizado) {
		this.utilizado = utilizado;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
