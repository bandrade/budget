package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Distrito: divisão interna da Dupont do mapa brasileiro.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "distrito")
@NamedQueries({
		@NamedQuery(name = "Distrito.findAll", query = "select d from Distrito d"),
		@NamedQuery(name = "Distrito.findByName", query = "select d from Distrito d where d.nome like :nome") })
public class Distrito extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = 4289247518816317111L;

}
