package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Despesa da solicitação de pagamento.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */

@Entity
@Table(name = "despesa_solicitacao_pagamento")
//@NamedQueries({
//	@NamedQuery(name = DespesaSolicitacaoPagamento.FIND_BY_FILTRO, query = "select o from DespesaSolicitacaoPagamento o join o.solicitacaoPagamento s where s.fornecedor.id = :fornecedor and s.centroCusto.id")
//})
public class DespesaSolicitacaoPagamento extends AbstractEntity<Long> {

	private static final long serialVersionUID = -1567944689224148141L;
	
	public static final String FIND_BY_FILTRO = "DespesaSolicitacaoPagamento.findByFiltro";

	@ManyToOne
	@JoinColumn(name = "centro_custo_id")
	private CentroCusto centroCusto;

	@ManyToOne
	@JoinColumn(name = "produto_id")
	private Produto produto;

	@ManyToOne
	@JoinColumn(name = "cultura_id")
	private Cultura cultura;

	@ManyToOne
	@JoinColumn(name = "distrito_id")
	private Distrito distrito;

	@ManyToOne
	@JoinColumn(name = "tipo_despesa_id")
	private TipoDespesa tipoDespesa;

	@ManyToOne
	@JoinColumn(name = "acao_id")
	private Acao acao;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "vendedor_id")
	private Vendedor vendedor;

	@ManyToOne
	@JoinColumn(name = "solicitacao_id")
	private SolicitacaoPagamento solicitacaoPagamento;

	private Double valor;

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Cultura getCultura() {
		return cultura;
	}

	public void setCultura(Cultura cultura) {
		this.cultura = cultura;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public SolicitacaoPagamento getSolicitacaoPagamento() {
		return solicitacaoPagamento;
	}

	public void setSolicitacaoPagamento(SolicitacaoPagamento solicitacaoPagamento) {
		this.solicitacaoPagamento = solicitacaoPagamento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}


}
