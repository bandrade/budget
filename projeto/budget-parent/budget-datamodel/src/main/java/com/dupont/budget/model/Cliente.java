package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Cliente da Dupont
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="cliente")
@NamedQueries({
	@NamedQuery(name="Cliente.findAll"   , query="select c from Cliente c"),
	@NamedQuery(name="Cliente.findByName", query="select c from Cliente c where c.nome like :nome")
}) 
public class Cliente extends NamedAbstractEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6748809974787032862L;


}
