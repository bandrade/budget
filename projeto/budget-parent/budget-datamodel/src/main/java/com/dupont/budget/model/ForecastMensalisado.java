package com.dupont.budget.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the status_forecast database table.
 * 
 */
@Entity
@Table(name="forecast_mensalisado")
@NamedQueries({
	@NamedQuery(name="ForecastMensalisado.findByForecastMes", query="select f from ForecastMensalisado f where f.pk.mes=:mes and f.forecast.id=:forecast_id")
})
//TODO CORRIGIR MAPEAMENTO
public class ForecastMensalisado implements Serializable {
	private static final long serialVersionUID = 1L;
	 @EmbeddedId
	 @AttributeOverrides({
	        @AttributeOverride(
	            name = "id",
	            column = @Column(name = "id")),
	        @AttributeOverride(
		            name = "mes",
		            column = @Column(name = "mes"))
		    })
	private ForecastMensalisadoPK pk;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private StatusForecastEnum statusForecast;
	
	//bi-directional many-to-one association to Forecast
	@ManyToOne(fetch = FetchType.EAGER)
	  @JoinColumn(name = "id", insertable = false, updatable = false)
	private Forecast forecast;
	
	@Column(name = "process_instance_id")
	private Long processInstanceId;
	

	public ForecastMensalisado()
	{
		
	}
	
	public ForecastMensalisado(ForecastMensalisadoPK pk,
			StatusForecastEnum statusForecast, Forecast forecast
			) {
		super();
		this.pk = pk;
		this.statusForecast = statusForecast;
		this.forecast = forecast;
	}



	public Forecast getForecast() {
		return this.forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}

	public StatusForecastEnum getStatusForecast() {
		return statusForecast;
	}

	public void setStatusForecast(StatusForecastEnum statusForecast) {
		this.statusForecast = statusForecast;
	}



	public Long getProcessInstanceId() {
		return processInstanceId;
	}


	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public ForecastMensalisadoPK getPk() {
		return pk;
	}

	public void setPk(ForecastMensalisadoPK pk) {
		this.pk = pk;
	}
	
	

}