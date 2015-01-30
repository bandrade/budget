package com.dupont.budget.dto;

import java.io.Serializable;

public class EmailSolicitacaoPagamentoDTO implements Serializable{
	private String notaFiscal;
	private String dataCriacao;
	private String fornecedor;
	private String usuario;
	private String tipoDespesa;
	private String acao ;
	private String valor;
	
	
	public EmailSolicitacaoPagamentoDTO(String notaFiscal, String dataCriacao,
			String fornecedor, String usuario, String tipoDespesa, String acao,
			String valor) {
		super();
		this.notaFiscal = notaFiscal;
		this.dataCriacao = dataCriacao;
		this.fornecedor = fornecedor;
		this.usuario = usuario;
		this.tipoDespesa = tipoDespesa;
		this.acao = acao;
		this.valor = valor;
	}
	public String getNotaFiscal() {
		return notaFiscal;
	}
	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	public String getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getTipoDespesa() {
		return tipoDespesa;
	}
	public void setTipoDespesa(String tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}
	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	@Override
	public String toString() {
		return "EmailSolicitacaoPagamentoDTO [notaFiscal=" + notaFiscal
				+ ", dataCriacao=" + dataCriacao + ", fornecedor=" + fornecedor
				+ ", usuario=" + usuario + ", tipoDespesa=" + tipoDespesa
				+ ", acao=" + acao + ", valor=" + valor + "]";
	}
	
	
}
