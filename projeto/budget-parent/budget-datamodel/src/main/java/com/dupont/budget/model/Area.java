package com.dupont.budget.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Área do Centro de Custo
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "area")
public class Area extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = 8630905655777582547L;

	@OneToOne(mappedBy = "area", orphanRemoval = true, cascade = CascadeType.ALL)
	private PapelUsuario lider;

	public PapelUsuario getLider() {
		return lider;
	}

	public void setLider(PapelUsuario lider) {
		this.lider = lider;
	}

}
