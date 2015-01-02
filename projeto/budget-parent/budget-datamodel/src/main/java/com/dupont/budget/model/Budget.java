package com.dupont.budget.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Entidade que representa o dudget do ano.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "budget")
@NamedQueries({
		@NamedQuery(name = "Budget.findByAnoAndCentroDeCusto", query = "select b from Budget b where b.centroCusto.id=:centroDeCustoId and b.ano like :ano"),
		@NamedQuery(name = "Budget.findByAnoAndArea", query = "select b from Budget b where b.centroCusto.area.id=:area_id and b.ano like :ano")
		})
public class Budget {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "centro_custo_id")
	private CentroCusto centroCusto;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioCriador;

	@Column(name = "process_instance_id")
	private Long processInstanceId;

	private String ano;
	@Transient
	private Double valorTotalBudget;

	@Column(name = "data_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cricao;

	@Column(name = "data_ultima_atualizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaAtualizacao;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "budget_id")
	private Set<Despesa> despesas;

	@Enumerated(EnumType.STRING)
	private StatusBudget status;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Usuario getUsuarioCriador() {
		return usuarioCriador;
	}

	public void setUsuarioCriador(Usuario usuarioCriador) {
		this.usuarioCriador = usuarioCriador;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Date getCricao() {
		return cricao;
	}

	public void setCricao(Date cricao) {
		this.cricao = cricao;
	}

	public Date getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(Date ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}

	public Set<Despesa> getDespesas() {
		return despesas;
	}

	public void setDespesas(Set<Despesa> despesas) {
		this.despesas = despesas;
	}

	public StatusBudget getStatus() {
		return status;
	}

	public void setStatus(StatusBudget status) {
		this.status = status;
	}

	public Double getValorTotalBudget() {
		return valorTotalBudget;
	}

	public void setValorTotalBudget(Double valorTotalBudget) {
		this.valorTotalBudget = valorTotalBudget;
	}


}
