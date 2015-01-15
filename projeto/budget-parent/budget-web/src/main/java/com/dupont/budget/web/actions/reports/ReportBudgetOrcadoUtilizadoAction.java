package com.dupont.budget.web.actions.reports;

import java.util.List;

import javax.inject.Inject;

import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.report.model.ReportBudgetOrcadoUtilizadoMaster;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Superclasse para as Actions dos relatorios do tipo BUDGET ORCADO X UTILIZADO 
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2015
 *
 */
public abstract class ReportBudgetOrcadoUtilizadoAction {

	@Inject
	protected FacesUtils facesUtils;

	// FILTROS DA TELA
	protected String ano;
	protected CentroCusto centroCusto;
	
	// DADOS DO RELATORIO
	protected List<ReportBudgetOrcadoUtilizadoMaster> report;
	private Double totalAnoOrcado = 0.0;
	private Double totalAnoUtilizado = 0.0;

	public void doReport(){
		report = getReportResult();
		
		if( report == null || report.isEmpty() ) {
			facesUtils.addInfoMessage("Não existe relatório disponível para o ANO e CENTRO DE CUSTO informados.");
			return;
		}
		
		for (ReportBudgetOrcadoUtilizadoMaster reportBudgetOrcadoUtilizadoMaster : report) {				
			addTotalAnoOrcado(reportBudgetOrcadoUtilizadoMaster.getTotalOrcado());
			addTotalAnoUtilizado(reportBudgetOrcadoUtilizadoMaster.getTotalUtilizado());		
		}
		
	}

	protected abstract List<ReportBudgetOrcadoUtilizadoMaster> getReportResult();

	public void addTotalAnoOrcado(Double valor) {
		totalAnoOrcado += valor;
	}

	public void addTotalAnoUtilizado(Double valor) {
		totalAnoUtilizado += valor;
	}

	public List<ReportBudgetOrcadoUtilizadoMaster> getReport() {
		return report;
	}

	public Double getTotalAnoOrcado() {
		return totalAnoOrcado;
	}

	public void setTotalAnoOrcado(Double totalAnoOrcado) {
		this.totalAnoOrcado = totalAnoOrcado;
	}

	public Double getTotalAnoUtilizado() {
		return totalAnoUtilizado;
	}

	public void setTotalAnoUtilizado(Double totalAnoUtilizado) {
		this.totalAnoUtilizado = totalAnoUtilizado;
	}
	
	public String getAno() {
		return ano;
	}
	
	public void setAno(String ano) {
		this.ano = ano;
	}
	
	public CentroCusto getCentroCusto() {
		return centroCusto;
	}
	
	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

}