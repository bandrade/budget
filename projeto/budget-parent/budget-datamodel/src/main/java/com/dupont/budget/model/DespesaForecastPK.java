package com.dupont.budget.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author bandrade
 *
 */
@Embeddable
public class DespesaForecastPK  implements Serializable{
	@Column
	private String ano;
	@Column
	private Long mes;

	@Column
	private Long id;
	public DespesaForecastPK()
	{}

	public DespesaForecastPK(String ano, Long mes, Long id) {
		super();
		this.ano = ano;
		this.mes = mes;
		this.id = id;
	}


	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public Long getMes() {
		return mes;
	}
	public void setMes(Long mes) {
		this.mes = mes;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DespesaForecastPK)) {
			return false;
		}
		DespesaForecastPK castOther = (DespesaForecastPK)other;
		return
			(this.id == castOther.id)
			&& this.ano.equals(castOther.ano)
			&& (this.mes == castOther.mes);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + (this.id !=null ? this.id.hashCode():0);
		hash = hash * prime + this.ano.hashCode();
		hash = hash * prime + this.mes.hashCode();

		return hash;
	}
}
