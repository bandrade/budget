package com.dupont.budget.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tolerancia_forecast database table.
 * 
 */
@Entity
@Table(name="tolerancia_forecast")
@NamedQuery(name="ToleranciaForecast.findAll", query="SELECT t FROM ToleranciaForecast t")
public class ToleranciaForecast extends AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="tipo_tolerancia_negativa")
	private String tipoToleranciaNegativa;

	@Column(name="tipo_tolerancia_positiva")
	private String tipoToleranciaPositiva;

	@Column(name="valor_tolerancia_negativa")
	private Double valorToleranciaNegativa;

	@Column(name="valor_tolerancia_positiva")
	private Double valorToleranciaPositiva;
	
	
	public ToleranciaForecast() {
	}


	public String getTipoToleranciaNegativa() {
		return this.tipoToleranciaNegativa;
	}

	public void setTipoToleranciaNegativa(String tipoToleranciaNegativa) {
		this.tipoToleranciaNegativa = tipoToleranciaNegativa;
	}

	public String getTipoToleranciaPositiva() {
		return this.tipoToleranciaPositiva;
	}

	public void setTipoToleranciaPositiva(String tipoToleranciaPositiva) {
		this.tipoToleranciaPositiva = tipoToleranciaPositiva;
	}

	public Double getValorToleranciaNegativa() {
		return valorToleranciaNegativa;
	}

	public void setValorToleranciaNegativa(Double valorToleranciaNegativa) {
		this.valorToleranciaNegativa = valorToleranciaNegativa;
	}

	public Double getValorToleranciaPositiva() {
		return valorToleranciaPositiva;
	}

	public void setValorToleranciaPositiva(Double valorToleranciaPositiva) {
		this.valorToleranciaPositiva = valorToleranciaPositiva;
	}

	

}