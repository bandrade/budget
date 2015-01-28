package com.dupont.budget.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
	@NamedQuery(name = SolicitacaoPagamento.FIND_BY_FILTRO, query = "select o from SolicitacaoPagamento o where o.numeroNotaFiscal = :numeroNotaFiscal and lower(o.fornecedor.nome) like :fornecedor and o.tipoSolicitacao = :tipoSolicitacao and o.status = :status"),
	@NamedQuery(name = SolicitacaoPagamento.FIND_BY_NUMERO_NOTA, query = "select o from SolicitacaoPagamento o where lower(o.numeroNotaFiscal) = :numeroNotaFiscal"),
	@NamedQuery(name = "SolicitacaoPagamento.findAll", query = "select p from SolicitacaoPagamento p" )
})
public class SolicitacaoPagamento extends AbstractEntity<Long> {

	private static final StringBuilder BODY_QUERY_VALOR_YTD=new StringBuilder()
			.append(" from solicitacao_pagamento solicitacao")
			.append(" inner join")
			.append("	despesa_solicitacao_pagamento despesa_solicitacao")
			.append(" on ")
			.append("	solicitacao.id = despesa_solicitacao.solicitacao_pagamento_id")
			.append(" inner join ")
			.append("	despesa_forecast")
		    .append(" on")
			.append("		despesa_solicitacao.acao_id = despesa_forecast.acao_id")
			.append("	and")
			.append("		despesa_solicitacao.tipo_despesa_id = despesa_forecast.tipo_despesa_id")
			.append("	and")
			.append("		despesa_forecast.mes = :mes_forecast")
			.append(" WHERE")
			.append("	MONTH (solicitacao.data_pagamento) in(:meses) and YEAR(solicitacao.data_pagamento)=:ano")
			.append(" and ")
			.append("	solicitacao.status='PAGO'")
			.append(" and")
			.append("	despesa_solicitacao.centro_custo_id=:centro_custo_id")
		    .append(" and")
		    .append("		despesa_solicitacao.acao_id = :acao_id")
		    .append(" and")
		    .append("		despesa_solicitacao.tipo_despesa_id = :tipo_despesa_id");

	public static final StringBuilder QUERY_SOMA_YTD =
			new StringBuilder().append(
					"select sum(despesa_solicitacao.valor) ")
				.append(BODY_QUERY_VALOR_YTD.toString());

	public static final StringBuilder QUERY_DETALHE_VALOR_YTD =
			new StringBuilder().append(
					"select solicitacao.num_nota_fiscal , solicitacao.valor, solicitacao.status , solicitacao.data_pagamento_realizado ")
				.append(BODY_QUERY_VALOR_YTD.toString());


	private static final StringBuilder BODY_QUERY_VALOR_COMPROMETIDO =new StringBuilder()
		.append(" from solicitacao_pagamento solicitacao")
		.append(" inner join")
		.append("	despesa_solicitacao_pagamento despesa_solicitacao")
		.append(" on ")
		.append("	solicitacao.id = despesa_solicitacao.solicitacao_pagamento_id")
		.append(" inner join ")
		.append("	despesa_forecast")
	    .append(" on")
		.append("		despesa_solicitacao.acao_id = despesa_forecast.acao_id")
		.append("	and")
		.append("		despesa_solicitacao.tipo_despesa_id = despesa_forecast.tipo_despesa_id")
		.append("	and")
		.append("		despesa_forecast.mes = :mes_forecast")
		.append("	inner join 	tipo_despesa")
		.append("	on	tipo_despesa.id = despesa_solicitacao.tipo_despesa_id")
		.append("   inner join 	acao")
		.append("		on acao.id = despesa_solicitacao.acao_id")
		.append(" WHERE")
		.append("	MONTH (solicitacao.data_pagamento)=:mes_anterior and YEAR(solicitacao.data_pagamento)=:ano")
		.append(" and ")
		.append("	(solicitacao.status='COMPROMETIDO'  or solicitacao.status='ENVIADO_SAP' or solicitacao.status='PENDENTE_VALIDACAO')")
		.append(" and")
		.append("	despesa_solicitacao.centro_custo_id=:centro_custo_id")
	    .append(" and")
	    .append("		despesa_solicitacao.acao_id = :acao_id")
	    .append(" and")
	    .append("		despesa_solicitacao.tipo_despesa_id = :tipo_despesa_id");



	public static final StringBuilder QUERY_SOMA_VALOR_COMPROMETIDO =
			new StringBuilder().append(
					"select sum(despesa_solicitacao.valor) ")
				.append(BODY_QUERY_VALOR_COMPROMETIDO.toString());


	public static final StringBuilder QUERY_DETALHE_VALOR_COMPROMETIDO =
			new StringBuilder()
				.append(" select distinct solicitacao.valor, tipo_despesa.nome as 'tipoDespesa', acao.nome as 'acao',solicitacao.num_nota_fiscal  ")
				.append(BODY_QUERY_VALOR_COMPROMETIDO.toString());




	private static final long serialVersionUID = 4098491678812403894L;

	public static final String FIND_BY_FILTRO = "SolicitacaoPagamento.findByFiltro";

	public static final String FIND_BY_NUMERO_NOTA = "SolicitacaoPagamento.findByNumeroNota";

	private Double valor;

	@Column(name="num_nota_fiscal")
	private String numeroNotaFiscal;

	@Column(name="process_instance_id")
	private Long processInstanceId;

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
	private TipoSolicitacao tipoSolicitacao = TipoSolicitacao.CC;

	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private StatusPagamento status;

	@OneToMany(mappedBy = "solicitacaoPagamento", cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval= true)
	private Set<DespesaSolicitacaoPagamento> despesas;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioCriador;

	@Column(name="origem")
	@Enumerated(EnumType.STRING)
	private OrigemSolicitacao origem;

	public void addDespesaSolicitacaoPagamento(DespesaSolicitacaoPagamento despesaSolicitacaoPagamento) {
		if(this.despesas == null)
			this.despesas = new HashSet<DespesaSolicitacaoPagamento>();

		this.despesas.add(despesaSolicitacaoPagamento);
		despesaSolicitacaoPagamento.setSolicitacaoPagamento(this);
	}

	public void removeDespesaSolicitacaoPagamento(DespesaSolicitacaoPagamento despesaSolicitacaoPagamento) {
		if( this.despesas == null )
			return;

		this.despesas.remove(despesaSolicitacaoPagamento);
		//despesaSolicitacaoPagamento.setSolicitacaoPagamento(null);
	}

	public SolicitacaoPagamento() {
		this(null);
	}

	public SolicitacaoPagamento(StatusPagamento status) {
		this.status = status;
	}

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

	public Set<DespesaSolicitacaoPagamento> getDespesas() {
		if (despesas == null) {
			despesas = new HashSet<>();
		}
		return despesas;
	}

	public void setDespesas(Set<DespesaSolicitacaoPagamento> despesas) {
		this.despesas = despesas;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
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

	public OrigemSolicitacao getOrigem() {
		return origem;
	}

	public void setOrigem(OrigemSolicitacao origem) {
		this.origem = origem;
	}

}
