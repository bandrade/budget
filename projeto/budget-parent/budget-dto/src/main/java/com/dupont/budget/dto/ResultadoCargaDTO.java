package com.dupont.budget.dto;

import java.io.Serializable;

/**
 * DTO para retorno do resultado da carga de notas na solicitação de pagamento.
 * 
 * @author joel
 *
 */
public class ResultadoCargaDTO implements Serializable {
	
	private static final long serialVersionUID = -2637347083728205456L;

	private String numeroNota;
	
	private String centroCusto;
	
	private String fornecedor;
	
	private Double valor;
	
	private String status;

	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
