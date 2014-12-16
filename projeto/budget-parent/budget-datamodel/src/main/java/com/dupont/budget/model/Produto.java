package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidade produto.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "produto")
@NamedQueries({
		@NamedQuery(name = "Produto.findAll", query = "select p from Produto p"),
		@NamedQuery(name = "Produto.findByName", query = "select p from Produto p where p.nome like :nome") })
public class Produto extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = 4661083022487742998L;

}
