package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Ação.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "acao")
public class Acao extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = -1359075483819011154L;

}
