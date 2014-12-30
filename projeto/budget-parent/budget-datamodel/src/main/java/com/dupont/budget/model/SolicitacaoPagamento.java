package com.dupont.budget.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidade de solicitação de pagamentos de despesas.
 *
 * @author @author bandrade
 * @since 2014
 *
 */
@Entity
@Table(name="solicitacao_pagamento")
@NamedQueries({
	@NamedQuery(name = SolicitacaoPagamento.FIND_BY_FILTRO, query = "select o from SolicitacaoPagamento o join o.despesas d where o.numeroNotaFiscal = :numeroNotaFiscal and lower(o.fornecedor.nome) = :fornecedor and lower(d.centroCusto.nome) = :codigoCentroCusto and o.mes = :mes")
})
public class SolicitacaoPagamento extends AbstractEntity<Long> {

	private static final long serialVersionUID = 4098491678812403894L;

	public static final String FIND_BY_FILTRO = "SolicitacaoPagamento.findByFiltro";

	private Double valor;

	@Column(name="num_nota_fiscal")
	private String numeroNotaFiscal;

	@Column(name="data_pagamento")
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;

	@Column(name="data_pagamento_realizado")
	@Temporal(TemporalType.DATE)
	private Date dataPagamentoRealizado;


	@Column(name = "data_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date criacao;

	@Column(name="tipo_solicitacao")
	@Enumerated(EnumType.STRING)
	private TipoSolicitacao tipoSolicitacao;

	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private StatusPagamento status;

	@OneToMany(mappedBy = "solicitacaoPagamento")
	private List<DespesaSolicitacaoPagamento> despesas;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioCriador;

	@Column(name="process_instance_id")
	private Integer processInstanceId;


	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public TipoSolicitacao getTipoSolicitacao() {
		return tipoSolicitacao;
	}

	public void setTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
		this.tipoSolicitacao = tipoSolicitacao;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public StatusPagamento getStatus() {
		return status;
	}

	public void setStatus(StatusPagamento status) {
		this.status = status;
	}

	public List<DespesaSolicitacaoPagamento> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<DespesaSolicitacaoPagamento> despesas) {
		this.despesas = despesas;
	}

	public Date getDataPagamentoRealizado() {
		return dataPagamentoRealizado;
	}

	public void setDataPagamentoRealizado(Date dataPagamentoRealizado) {
		this.dataPagamentoRealizado = dataPagamentoRealizado;
	}

	public Date getCriacao() {
		return criacao;
	}

	public void setCriacao(Date criacao) {
		this.criacao = criacao;
	}

	public Usuario getUsuarioCriador() {
		return usuarioCriador;
	}

	public void setUsuarioCriador(Usuario usuarioCriador) {
		this.usuarioCriador = usuarioCriador;
	}

	public Integer getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Integer processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	

}
