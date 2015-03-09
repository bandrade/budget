package com.dupont.budget.dto;

import java.math.BigDecimal;

/**
 * @author bandrade
 *
 */
public class BudgetAreaDTO {

	private Long idArea;
	private String nomeArea;
	private BigDecimal valorTotalBudget;
	private BigDecimal valorTotalAprovadoBudget;
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
	public BigDecimal getValorTotalBudget() {
		return valorTotalBudget;
	}
	public void setValorTotalBudget(BigDecimal valorTotalBudget) {
		this.valorTotalBudget = valorTotalBudget;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getValorTotalAprovadoBudget() {
		return valorTotalAprovadoBudget;
	}
	public void setValorTotalAprovadoBudget(BigDecimal valorTotalAprovadoBudget) {
		this.valorTotalAprovadoBudget = valorTotalAprovadoBudget;
	}




}
