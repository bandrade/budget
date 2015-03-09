package com.dupont.budget.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class DespesaSolicitacaoPagamento extends AbstractEntity<Long> {

	private static final long serialVersionUID = -1567944689224148141L;

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

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name = "solicitacao_pagamento_id")
	private SolicitacaoPagamento solicitacaoPagamento;

	private BigDecimal valor;

	public boolean isPreenchimentoCompleto()
	{
		if(centroCusto !=null && centroCusto.getId() !=null && acao !=null && acao.getId() !=null && valor !=null && valor.doubleValue() !=0d)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipoSolicitacaoAsString()
	{
		if(solicitacaoPagamento !=null && solicitacaoPagamento.getStatus() !=null)
			return this.solicitacaoPagamento.getStatus().toString();
		else
			return "";
	}





}
