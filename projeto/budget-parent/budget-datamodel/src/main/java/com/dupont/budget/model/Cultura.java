package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidade cultura.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="cultura")
@NamedQueries({
	@NamedQuery(name="Cultura.findAll"   , query="select c from Cultura c"),
	@NamedQuery(name="Cultura.findByName", query="select c from Cultura c where c.nome like :nome")
})
public class Cultura extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = -8820882730092460844L;

	@Override
	public String toString() {
		return nome;
	}
	
	
}
