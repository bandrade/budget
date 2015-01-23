package com.dupont.budget.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
	@NamedQuery(name="Forecast.findByBudgetId", query="select f from Forecast f where f.budget.id = :budgetId"),
	@NamedQuery(name="Forecast.findByAno", query="select f from Forecast f where f.ano=:ano"),
	@NamedQuery(name="Forecast.findByAnoAndCC", query="select f from Forecast f where f.ano=:ano and f.centroCusto.id=:centroCustoId"),
	@NamedQuery(name="Forecast.findAll", query="select f from Forecast f")
})
public class Forecast implements Serializable {

	private static final long serialVersionUID = 6795074865859720715L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "budget_id")
	private Budget budget;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioCriador;

	@Column(name = "data_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cricao;

	@Column(name = "data_ultima_atualizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaAtualizacao;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "forecast_id")
	private Set<DespesaForecast> despesas;

	//bi-directional many-to-one association to Acao
	@OneToMany(mappedBy="forecast")
	private List<Acao> acoes;
	
	private String ano;
	
	//bi-directional many-to-one association to StatusForecast
	@OneToMany(mappedBy="forecast",fetch = FetchType.EAGER)
	private Set<ForecastMensalisado> statusForecasts;
	
	@ManyToOne
	@JoinColumn(name = "centro_custo_id")
	private CentroCusto centroCusto;

	public Forecast() {}

	public Forecast(Long id) {
		super();
		this.id = id;
	}

	public Forecast(Usuario usuarioCriador, Date cricao, Budget budget,CentroCusto centroCusto,String ano) {
		super();
		this.usuarioCriador = usuarioCriador;
		this.cricao = cricao;
		this.budget =budget;
		this.centroCusto= centroCusto;
		this.ano= ano;
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

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Set<ForecastMensalisado> getStatusForecasts() {
		if(statusForecasts == null)
			statusForecasts = new HashSet<>();
			
		return statusForecasts;
	}

	public void setStatusForecasts(Set<ForecastMensalisado> statusForecasts) {
		this.statusForecasts = statusForecasts;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

}
