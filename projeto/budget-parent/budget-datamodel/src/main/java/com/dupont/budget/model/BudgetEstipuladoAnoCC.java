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
@Table(name="budget_estipulado_cc")
@NamedQueries({
	@NamedQuery(name = "BudgetEstipuladoAnoCC.findByAnoAndCC", query = "select b from BudgetEstipuladoAnoCC b where b.ano=:ano and b.centroCusto.id like :centro_custo_id")
	})
public class BudgetEstipuladoAnoCC {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="centro_custo_id")
	private CentroCusto centroCusto;

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

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

}
