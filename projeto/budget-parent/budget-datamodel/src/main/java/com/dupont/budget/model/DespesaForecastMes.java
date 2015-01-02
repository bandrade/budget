package com.dupont.budget.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Divisao do valor da Despesa do Forecast por mes.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "despesa_forecast_mes")
public class DespesaForecastMes implements Serializable {

	@Id
	private Long id;

	private Double janeiro;

	private Double fevereiro;

	private Double marco;

	private Double abril;

	private Double maio;

	private Double junho;

	private Double julho;

	private Double agosto;

	private Double setembro;

	private Double outubro;

	private Double novembro;

	private Double dezembro;

	public DespesaForecastMes() {
	}


	public DespesaForecastMes( Double janeiro, Double fevereiro,
			Double marco, Double abril, Double maio, Double junho,
			Double julho, Double agosto, Double setembro, Double outubro,
			Double novembro, Double dezembro) {
		this.janeiro = janeiro;
		this.fevereiro = fevereiro;
		this.marco = marco;
		this.abril = abril;
		this.maio = maio;
		this.junho = junho;
		this.julho = julho;
		this.agosto = agosto;
		this.setembro = setembro;
		this.outubro = outubro;
		this.novembro = novembro;
		this.dezembro = dezembro;
	}

	public static DespesaForecastMes createFromBudgetMes(BudgetMes mes)
	{
		DespesaForecastMes forecastMes = new DespesaForecastMes(mes.getJaneiro(), mes.getFevereiro(),
				mes.getMarco(), mes.getAbril(), mes.getMaio(), mes.getJunho(), mes.getJulho(),
				mes.getAgosto(), mes.getSetembro(), mes.getOutubro(), mes.getNovembro(), mes.getDezembro());

		return forecastMes;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getJaneiro() {
		return janeiro;
	}

	public void setJaneiro(Double janeiro) {
		this.janeiro = janeiro;
	}

	public Double getFevereiro() {
		return fevereiro;
	}

	public void setFevereiro(Double fevereiro) {
		this.fevereiro = fevereiro;
	}

	public Double getMarco() {
		return marco;
	}

	public void setMarco(Double marco) {
		this.marco = marco;
	}

	public Double getAbril() {
		return abril;
	}

	public void setAbril(Double abril) {
		this.abril = abril;
	}

	public Double getMaio() {
		return maio;
	}

	public void setMaio(Double maio) {
		this.maio = maio;
	}

	public Double getJunho() {
		return junho;
	}

	public void setJunho(Double junho) {
		this.junho = junho;
	}

	public Double getJulho() {
		return julho;
	}

	public void setJulho(Double julho) {
		this.julho = julho;
	}

	public Double getAgosto() {
		return agosto;
	}

	public void setAgosto(Double agosto) {
		this.agosto = agosto;
	}

	public Double getSetembro() {
		return setembro;
	}

	public void setSetembro(Double setembro) {
		this.setembro = setembro;
	}

	public Double getOutubro() {
		return outubro;
	}

	public void setOutubro(Double outubro) {
		this.outubro = outubro;
	}

	public Double getNovembro() {
		return novembro;
	}

	public void setNovembro(Double novembro) {
		this.novembro = novembro;
	}

	public Double getDezembro() {
		return dezembro;
	}

	public void setDezembro(Double dezembro) {
		this.dezembro = dezembro;
	}



}
