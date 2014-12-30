package com.dupont.budget.model;

import java.util.Date;
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
		})
public class Forecast {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "mes")
	private Integer mes;

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



}