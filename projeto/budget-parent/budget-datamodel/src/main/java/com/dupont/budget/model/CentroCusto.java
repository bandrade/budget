package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidade de Centro de Custo.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="centro_custo")
@NamedQueries({
	@NamedQuery(name="CentroCusto.findAll"   , query="select c from CentroCusto c")	
})
public class CentroCusto extends NamedAbstractEntity<Long> {
	
	private static final long serialVersionUID = 763183827982065682L;

	private String codigo;
	
	@Enumerated(EnumType.STRING)
	private TipoCentroCusto tipo;
	
	@ManyToOne
	@JoinColumn(name="area_id")
	private Area area;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public TipoCentroCusto getTipo() {
		return tipo;
	}

	public void setTipo(TipoCentroCusto tipo) {
		this.tipo = tipo;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

}
