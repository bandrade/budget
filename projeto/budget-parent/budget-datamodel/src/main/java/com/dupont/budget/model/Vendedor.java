package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidade vendedor: ERC/ERR
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "vendedor")
@NamedQueries({
		@NamedQuery(name = "Vendedor.findAll", query = "select v from Vendedor v"),
		@NamedQuery(name = "Vendedor.findByName", query = "select v from Vendedor v where v.nome like :nome") })
public class Vendedor extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = -2613306498560545658L;

}
