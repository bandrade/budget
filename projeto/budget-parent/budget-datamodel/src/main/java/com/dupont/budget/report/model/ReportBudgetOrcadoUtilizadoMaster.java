package com.dupont.budget.report.model;

import java.io.Serializable;
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
	
	private Double totalOrcado;
	
	private Double totalUtilizado;
	
	private List<ReportBudgetOrcadoUtilizadoDetail> details;
	
	public ReportBudgetOrcadoUtilizadoMaster(String master, Double totalOrcado, Double totalUtilizado) {
		this.master         = master;
		this.totalOrcado    = totalOrcado;
		this.totalUtilizado = totalUtilizado;
	}

	public void addDetail(ReportBudgetOrcadoUtilizadoDetail detail) {
		if( details == null )
			details = new ArrayList<ReportBudgetOrcadoUtilizadoDetail>();
		
		details.add(detail);
		
		this.totalOrcado += detail.getOrcado();
	}
	
	public ReportBudgetOrcadoUtilizadoDetail getAcao(String detailName){
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
	
	public Double getTotalOrcado() {
		return totalOrcado;
	}

	public void setTotalOrcado(Double totalOrcado) {
		this.totalOrcado = totalOrcado;
	}

	public Double getTotalUtilizado() {
		return totalUtilizado;
	}

	public void setTotalUtilizado(Double totalUtilizado) {
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
