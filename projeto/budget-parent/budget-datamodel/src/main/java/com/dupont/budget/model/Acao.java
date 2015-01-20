package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@NamedQueries({
	@NamedQuery(name = Acao.FIND_ACAO_BY_BUDGET, query = "select a from Acao a where a.budget.id =:budgetId")
})
public class Acao extends NamedAbstractEntity<Long> {
	
	public static final String FIND_ACAO_BY_BUDGET="Acao.findAcaoByBudget";
	private static final long serialVersionUID = -1359075483819011154L;
	
	//bi-directional many-to-one association to Budget
	@ManyToOne
	private Budget budget;

	public Acao() {
		this(null);
	}
	
	public Acao(String nome) {
		this.nome = nome;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}
	

}
