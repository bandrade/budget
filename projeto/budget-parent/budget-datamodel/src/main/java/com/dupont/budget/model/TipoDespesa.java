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

}
