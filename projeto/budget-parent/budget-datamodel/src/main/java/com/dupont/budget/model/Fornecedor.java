package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entidade fornecedor
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="fornecedor")
public class Fornecedor extends NamedAbstractEntity<Long>{
	
	private static final long serialVersionUID = -2495897745436716001L;
	
	public Fornecedor() {
		this(null, false);
	}
	
	public Fornecedor(String nome) {
		this(nome, false);
	}
	
	public Fornecedor(String nome, Boolean status) {
		this.nome = nome;
		this.ativo = status;
	}
	
	private Boolean ativo;
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean status) {
		this.ativo = status;
	}
	

}
