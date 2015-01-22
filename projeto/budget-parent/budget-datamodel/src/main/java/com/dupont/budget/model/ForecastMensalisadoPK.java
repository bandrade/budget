package com.dupont.budget.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author bandrade
 *
 */
/**
 * @author bandrade
 *
 */
@Embeddable
public class ForecastMensalisadoPK implements Serializable {
	private long id;
	private long mes;
	
	public ForecastMensalisadoPK()
	{
		
	}
	public ForecastMensalisadoPK(long id, long mes) {
		super();
		this.id = id;
		this.mes = mes;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMes() {
		return mes;
	}
	public void setMes(long mes) {
		this.mes = mes;
	}
	
	
}
