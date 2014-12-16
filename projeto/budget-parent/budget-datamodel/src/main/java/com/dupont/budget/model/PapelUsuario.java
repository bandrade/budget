package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Representa o papel do usuário no gerenciamento dos centros de custos.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="papel_usuario")
public class PapelUsuario {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id; 
	
	@ManyToOne
	@JoinColumn(name="centro_custo_id")
	private CentroCusto centroCusto;
	
	@ManyToOne
	@JoinColumn(name="papel_id")
	private Papel papel;
	
	public PapelUsuario() {
		this(null);
	}
	
	public PapelUsuario(Papel papel) {
		this.papel = papel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
}
