package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@NamedQueries({
@NamedQuery(name="PapelUsuario.findByCentroDeCusto"   , query="select p from PapelUsuario p where p.centroCusto.id=:idCentroDeCusto")
})
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
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
		
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}
