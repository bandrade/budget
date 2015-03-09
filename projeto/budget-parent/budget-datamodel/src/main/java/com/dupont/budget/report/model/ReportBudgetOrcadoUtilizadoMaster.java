package com.dupont.budget.report.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * MASTER do relatorio de budget orçado x utilizado.
 *
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public class ReportBudgetOrcadoUtilizadoMaster implements Serializable {

	private static final long serialVersionUID = 928997715561629397L;

	private String master;

	private BigDecimal totalOrcado;

	private BigDecimal totalUtilizado;

	private List<ReportBudgetOrcadoUtilizadoDetail> details;

	public ReportBudgetOrcadoUtilizadoMaster(String master, BigDecimal totalOrcado, BigDecimal totalUtilizado) {
		this.master         = master;
		this.totalOrcado    = totalOrcado;
		this.totalUtilizado = totalUtilizado;
	}

	public void addDetail(ReportBudgetOrcadoUtilizadoDetail detail) {
		if( details == null )
			details = new ArrayList<ReportBudgetOrcadoUtilizadoDetail>();

		details.add(detail);

		this.totalOrcado = totalOrcado.add(detail.getOrcado());
	}

	public ReportBudgetOrcadoUtilizadoDetail getDetail(String detailName){
		if( details == null )
			return null;

		ReportBudgetOrcadoUtilizadoDetail result = null;

		for (ReportBudgetOrcadoUtilizadoDetail detail : details) {

			if( detail.getDetail().equals(detailName) ) {
				result = detail;
				break;
			}
		}

		return result;
	}

	public BigDecimal getTotalOrcado() {
		return totalOrcado;
	}

	public void setTotalOrcado(BigDecimal totalOrcado) {
		this.totalOrcado = totalOrcado;
	}

	public BigDecimal getTotalUtilizado() {
		return totalUtilizado.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public void setTotalUtilizado(BigDecimal totalUtilizado) {
		this.totalUtilizado = totalUtilizado;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public List<ReportBudgetOrcadoUtilizadoDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ReportBudgetOrcadoUtilizadoDetail> details) {
		this.details = details;
	}


}
