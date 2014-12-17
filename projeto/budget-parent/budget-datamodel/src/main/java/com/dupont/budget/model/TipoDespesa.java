package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Tipo de despesa.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "tipo_despesa")
@NamedQueries({
		@NamedQuery(name = "TipoDespesa.findAll", query = "select t from TipoDespesa t"),
		@NamedQuery(name = "TipoDespesa.findByName", query = "select t from TipoDespesa t where t.nome like :nome") })
public class TipoDespesa extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = -1150860018776728153L;
	private String conta_contabil;
	public String getConta_contabil() {
		return conta_contabil;
	}
	public void setConta_contabil(String conta_contabil) {
		this.conta_contabil = conta_contabil;
	}
	
	

}
