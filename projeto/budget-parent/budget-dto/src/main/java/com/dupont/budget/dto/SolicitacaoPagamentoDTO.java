package com.dupont.budget.dto;

public class SolicitacaoPagamentoDTO {
	private Long idSolicitacao;
	private Long idDespesa;
	private AreaDTO area;
	private String numeroNota;
	public Long getIdSolicitacao() {
		return idSolicitacao;
	}
	public void setIdSolicitacao(Long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}
	public Long getIdDespesa() {
		return idDespesa;
	}
	public void setIdDespesa(Long idDespesa) {
		this.idDespesa = idDespesa;
	}
	public AreaDTO getArea() {
		return area;
	}
	public void setArea(AreaDTO area) {
		this.area = area;
	}
	public String getNumeroNota() {
		return numeroNota;
	}
	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}


}
