package com.dupont.budget.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidade que representa o forecast.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "forecast")
@NamedQueries({
	@NamedQuery(name="Forecast.findByBudgetId", query="select f from Forecast f where f.budget.id = :budgetId")
})
public class Forecast implements Serializable {
	
	private static final long serialVersionUID = 6795074865859720715L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(name = "mes")
	private Integer mes;


	@ManyToOne
	@JoinColumn(name = "budget_id")
	private Budget budget;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioCriador;

	@Column(name = "process_instance_id")
	private Long processInstanceId;


	@Column(name = "data_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cricao;

	@Column(name = "data_ultima_atualizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaAtualizacao;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "forecast_id")
	private Set<DespesaForecast> despesas;
	

	public Forecast() {}

	public Forecast(Integer mes, Usuario usuarioCriador, Date cricao, Budget budget) {
		super();
		this.mes = mes;
		this.usuarioCriador = usuarioCriador;
		this.cricao = cricao;
		this.budget =budget;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public Set<DespesaForecast> getDespesas() {
		return despesas;
	}

	public void setDespesas(Set<DespesaForecast> despesas) {
		this.despesas = despesas;
	}

	public Usuario getUsuarioCriador() {
		return usuarioCriador;
	}


}
