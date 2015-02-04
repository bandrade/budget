package com.dupont.budget.model;

import javax.persistence.Column;
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
public class TipoDespesa extends NamedAbstractEntity<Long> implements Comparable<TipoDespesa>{

	private static final long serialVersionUID = -1150860018776728153L;
	
	@Column(name = "conta_contabil")
	private String contaContabil;
	
	private String descricao;
	
	public TipoDespesa() {
		this(null);
	}
	
	public TipoDespesa(String nome) {
		this.nome = nome;
	}
	
	public String getContaContabil() {
		return contaContabil;
	}
	public void setContaContabil(String conta_contabil) {
		this.contaContabil = conta_contabil;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int compareTo(TipoDespesa o) {

		return getId().compareTo(o.getId());
	}
	
}
