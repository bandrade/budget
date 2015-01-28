package com.dupont.budget.model;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((acao == null) ? 0 : acao.hashCode());
		result = prime * result
				+ ((centroCusto == null) ? 0 : centroCusto.hashCode());
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((cultura == null) ? 0 : cultura.hashCode());
		result = prime * result
				+ ((distrito == null) ? 0 : distrito.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime
				* result
				+ ((solicitacaoPagamento == null) ? 0 : solicitacaoPagamento
						.hashCode());
		result = prime * result
				+ ((tipoDespesa == null) ? 0 : tipoDespesa.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result
				+ ((vendedor == null) ? 0 : vendedor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DespesaSolicitacaoPagamento other = (DespesaSolicitacaoPagamento) obj;
		if (acao == null) {
			if (other.acao != null)
				return false;
		} else if (!acao.equals(other.acao))
			return false;
		if (centroCusto == null) {
			if (other.centroCusto != null)
				return false;
		} else if (!centroCusto.equals(other.centroCusto))
			return false;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (cultura == null) {
			if (other.cultura != null)
				return false;
		} else if (!cultura.equals(other.cultura))
			return false;
		if (distrito == null) {
			if (other.distrito != null)
				return false;
		} else if (!distrito.equals(other.distrito))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (solicitacaoPagamento == null) {
			if (other.solicitacaoPagamento != null)
				return false;
		} else if (!solicitacaoPagamento.equals(other.solicitacaoPagamento))
			return false;
		if (tipoDespesa == null) {
			if (other.tipoDespesa != null)
				return false;
		} else if (!tipoDespesa.equals(other.tipoDespesa))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		if (vendedor == null) {
			if (other.vendedor != null)
				return false;
		} else if (!vendedor.equals(other.vendedor))
			return false;
		return true;
	}
	
	



}
