package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Papel do usuário no sistema.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "papel")
public class Papel extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = -8321943677400253472L;
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
