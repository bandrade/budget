package com.dupont.budget.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the despesa_forecast_ano database table.
 * 
 */
@Embeddable
public class DespesaForecastAnoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	@JoinColumn(name="id_despesa_forecast")
	private Long idDespesaForecast;
	
	@JoinColumn(name="id_despesa_forecast_mes")
	private Long idDespesaForecastMes;

	public DespesaForecastAnoPK(Long idDespesaForecast, Long idDespesaForecastMes) {
		super();
		this.idDespesaForecast = idDespesaForecast;
		this.idDespesaForecastMes = idDespesaForecastMes;
	}
	public DespesaForecastAnoPK() {
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DespesaForecastAnoPK)) {
			return false;
		}
		DespesaForecastAnoPK castOther = (DespesaForecastAnoPK)other;
		return 
			(this.idDespesaForecast == castOther.idDespesaForecast)
			&& (this.idDespesaForecastMes == castOther.idDespesaForecastMes);
	}

	public int hashCode() {
		final int prime = 31;
		long hash = 17;
		hash = hash * prime + this.idDespesaForecast;
		hash = hash * prime + this.idDespesaForecastMes;
		
		return Integer.valueOf(hash+"");
	}
	public Long getIdDespesaForecast() {
		return idDespesaForecast;
	}
	public void setIdDespesaForecast(Long idDespesaForecast) {
		this.idDespesaForecast = idDespesaForecast;
	}
	public Long getIdDespesaForecastMes() {
		return idDespesaForecastMes;
	}
	public void setIdDespesaForecastMes(Long idDespesaForecastMes) {
		this.idDespesaForecastMes = idDespesaForecastMes;
	}
	
}