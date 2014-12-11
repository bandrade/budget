package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Divisao do valor do Budget por mes.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="budget_mes")
public class BudgetMes {
	
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
