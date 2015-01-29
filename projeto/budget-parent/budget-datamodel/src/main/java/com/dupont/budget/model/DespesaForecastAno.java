package com.dupont.budget.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the despesa_forecast_ano database table.
 * 
 */
@Entity
@Table(name="despesa_forecast_ano")
@NamedQueries({
@NamedQuery(name="DespesaForecastAno.findAll", query="SELECT d FROM DespesaForecastAno d"),
@NamedQuery(name="DespesaForecastAno.obterDespesasMensalizada", query="select c.despesaForecastMes from DespesaForecastAno c where c.despesaForecast.forecast.id = :forecastId and c.mes=:mes"),
})
public class DespesaForecastAno implements Serializable {
	private static final long serialVersionUID = 1L;

	 @EmbeddedId
	 @AttributeOverrides({
	        @AttributeOverride(
	            name = "id_despesa_forecast",
	            column = @Column(name = "id_despesa_forecast")),
	        @AttributeOverride(
		            name = "id_despesa_forecast_mes",
		            column = @Column(name = "id_despesa_forecast_mes"))
		    })
	private DespesaForecastAnoPK id;
	private int mes;

	//bi-directional many-to-one association to DespesaForecast
	@ManyToOne
	@JoinColumn(name="id_despesa_forecast")
	private DespesaForecast despesaForecast;

	//bi-directional many-to-one association to DespesaForecastMe
	@ManyToOne
	@JoinColumn(name="id_despesa_forecast_mes")
	private DespesaForecastMes despesaForecastMes;

	public DespesaForecastAno() {
	}

	
	public DespesaForecastAno(DespesaForecastAnoPK id, int mes,
			DespesaForecast despesaForecast,
			DespesaForecastMes despesaForecastMes) {
		super();
		this.id = id;
		this.mes = mes;
		this.despesaForecast = despesaForecast;
		this.despesaForecastMes = despesaForecastMes;
	}


	public DespesaForecastAnoPK getId() {
		return this.id;
	}

	public void setId(DespesaForecastAnoPK id) {
		this.id = id;
	}

	public int getMes() {
		return this.mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public DespesaForecast getDespesaForecast() {
		return this.despesaForecast;
	}

	public void setDespesaForecast(DespesaForecast despesaForecast) {
		this.despesaForecast = despesaForecast;
	}

	public DespesaForecastMes getDespesaForecastMes() {
		return despesaForecastMes;
	}

	public void setDespesaForecastMes(DespesaForecastMes despesaForecastMes) {
		this.despesaForecastMes = despesaForecastMes;
	}

	

}