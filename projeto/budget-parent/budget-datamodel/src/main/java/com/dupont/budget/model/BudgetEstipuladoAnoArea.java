package com.dupont.budget.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="budget_estipulado_area")
@NamedQueries({
	@NamedQuery(name = "BudgetEstipulado.findByAnoAndArea", query = "select b from BudgetEstipuladoAnoArea b where b.ano=:ano and b.area.id like :area_id")
	})
public class BudgetEstipuladoAnoArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="area_id")
	private Area area;

	private String ano;

	@Column(name="valor_submetido")
	private BigDecimal valorSubmetido;

	@Column(name="valor_aprovado")
	private BigDecimal valorAprovado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public BigDecimal getValorSubmetido() {
		return valorSubmetido;
	}

	public void setValorSubmetido(BigDecimal valorSubmetido) {
		this.valorSubmetido = valorSubmetido;
	}

	public BigDecimal getValorAprovado() {
		return valorAprovado;
	}

	public void setValorAprovado(BigDecimal valorAprovado) {
		this.valorAprovado = valorAprovado;
	}




}
