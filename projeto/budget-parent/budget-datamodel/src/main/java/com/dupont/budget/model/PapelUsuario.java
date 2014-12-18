package com.dupont.budget.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class PapelUsuario extends AbstractEntity<Long> {
	
	private static final long serialVersionUID = 7181266797888273789L;

	@ManyToOne
	@JoinColumn(name="centro_custo_id")
	private CentroCusto centroCusto;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="papel_id")
	private Papel papel;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="area_id")
	private Area area;
	
	private Integer nivel;
		
	public PapelUsuario() {
		this(null);
	}
	
	public PapelUsuario(Papel papel) {
		this.papel = papel;
	}
	
	public PapelUsuario(Papel papel, Usuario usuario) {
		this.papel = papel;
		this.usuario = usuario;
	}
	
	public PapelUsuario(Usuario usuario, Area area) {
		this.area = area;
		this.usuario = usuario;
	}
	
	public PapelUsuario(Papel papel, Usuario usuario, Area area) {
		this.papel = papel;
		this.area = area;
		this.usuario = usuario;
	}
	
	public PapelUsuario(Papel papel, Usuario usuario, CentroCusto centroCusto) {
		this.papel = papel;
		this.centroCusto = centroCusto;
		this.usuario = usuario;
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result
				+ ((centroCusto == null) ? 0 : centroCusto.hashCode());
		result = prime * result + ((nivel == null) ? 0 : nivel.hashCode());
		result = prime * result + ((papel == null) ? 0 : papel.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PapelUsuario other = (PapelUsuario) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (centroCusto == null) {
			if (other.centroCusto != null)
				return false;
		} else if (!centroCusto.equals(other.centroCusto))
			return false;
		if (nivel == null) {
			if (other.nivel != null)
				return false;
		} else if (!nivel.equals(other.nivel))
			return false;
		if (papel == null) {
			if (other.papel != null)
				return false;
		} else if (!papel.equals(other.papel))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	
	
}
