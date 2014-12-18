package com.dupont.budget.dto;

public class DespesasAgrupadasDTO {
	private Long tipoDespesaId;
	private String nomeDespesa;
	private Double valorAgrupado;
	
	public Long getTipoDespesaId() {
		return tipoDespesaId;
	}
	public void setTipoDespesaId(Long tipoDespesaId) {
		this.tipoDespesaId = tipoDespesaId;
	}
	public String getNomeDespesa() {
		return nomeDespesa;
	}
	public void setNomeDespesa(String nomeDespesa) {
		this.nomeDespesa = nomeDespesa;
	}
	public Double getValorAgrupado() {
		return valorAgrupado;
	}
	public void setValorAgrupado(Double valorAgrupado) {
		this.valorAgrupado = valorAgrupado;
	}
	
	
	
}
