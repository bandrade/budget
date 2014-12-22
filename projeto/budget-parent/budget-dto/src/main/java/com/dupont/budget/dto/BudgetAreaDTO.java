package com.dupont.budget.dto;

/**
 * @author bandrade
 *
 */
public class BudgetAreaDTO {

	private Long idArea;
	private String nomeArea;
	private Double valorTotalBudget;
	private String status;

	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public String getNomeArea() {
		return nomeArea;
	}
	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}
	public Double getValorTotalBudget() {
		return valorTotalBudget;
	}
	public void setValorTotalBudget(Double valorTotalBudget) {
		this.valorTotalBudget = valorTotalBudget;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}



}
