package com.dupont.budget.model;

import java.math.BigDecimal;
import java.text.Bidi;

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

	private BigDecimal janeiro;

	private BigDecimal fevereiro;

	private BigDecimal marco;

	private BigDecimal abril;

	private BigDecimal maio;

	private BigDecimal junho;

	private BigDecimal julho;

	private BigDecimal agosto;

	private BigDecimal setembro;

	private BigDecimal outubro;

	private BigDecimal novembro;

	private BigDecimal dezembro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getJaneiro() {
		return janeiro;
	}

	public void setJaneiro(BigDecimal janeiro) {
		this.janeiro = janeiro;
	}

	public BigDecimal getFevereiro() {
		return fevereiro;
	}

	public void setFevereiro(BigDecimal fevereiro) {
		this.fevereiro = fevereiro;
	}

	public BigDecimal getMarco() {
		return marco;
	}

	public void setMarco(BigDecimal marco) {
		this.marco = marco;
	}

	public BigDecimal getAbril() {
		return abril;
	}

	public void setAbril(BigDecimal abril) {
		this.abril = abril;
	}

	public BigDecimal getMaio() {
		return maio;
	}

	public void setMaio(BigDecimal maio) {
		this.maio = maio;
	}

	public BigDecimal getJunho() {
		return junho;
	}

	public void setJunho(BigDecimal junho) {
		this.junho = junho;
	}

	public BigDecimal getJulho() {
		return julho;
	}

	public void setJulho(BigDecimal julho) {
		this.julho = julho;
	}

	public BigDecimal getAgosto() {
		return agosto;
	}

	public void setAgosto(BigDecimal agosto) {
		this.agosto = agosto;
	}

	public BigDecimal getSetembro() {
		return setembro;
	}

	public void setSetembro(BigDecimal setembro) {
		this.setembro = setembro;
	}

	public BigDecimal getOutubro() {
		return outubro;
	}

	public void setOutubro(BigDecimal outubro) {
		this.outubro = outubro;
	}

	public BigDecimal getNovembro() {
		return novembro;
	}

	public void setNovembro(BigDecimal novembro) {
		this.novembro = novembro;
	}

	public BigDecimal getDezembro() {
		return dezembro;
	}

	public void setDezembro(BigDecimal dezembro) {
		this.dezembro = dezembro;
	}


}
