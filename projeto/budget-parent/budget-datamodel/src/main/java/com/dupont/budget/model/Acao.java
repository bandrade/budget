package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
	@NamedQuery(name = Acao.FIND_ACAO_BY_BUDGET, query = "select a from Acao a where a.budget.id =:budgetId"),
	@NamedQuery(name = Acao.FIND_ACAO_BY_NAME_AND_BUDGET, query = "select a from Acao a where a.budget.id =:budgetId and lower(a.nome) = lower(:nomeAcao)"),
	@NamedQuery(name = Acao.FIND_ACAO_BY_BUDGET_OR_FORECAST, query = "select a from Acao a where (a.budget.id =:budgetId or a.forecast.id =:forecastId)"),
	@NamedQuery(name = Acao.FIND_ACAO_BY_BUDGET_OR_FORECAST_AND_NOMEACAO, query = "select a from Acao a where (a.budget.id =:budgetId or a.forecast.id =:forecastId) and lower(a.nome) like :nomeAcao")
})
public class Acao extends NamedAbstractEntity<Long> {
	
	public static final String FIND_ACAO_BY_BUDGET="Acao.findAcaoByBudget";
	public static final String FIND_ACAO_BY_BUDGET_OR_FORECAST_AND_NOMEACAO="Acao.findAcaoByBudgetAndForecastAndNomeAcao";
	public static final String FIND_ACAO_BY_BUDGET_OR_FORECAST="Acao.findAcaoByBudgetAndForecast";
	public static final String FIND_ACAO_BY_NAME_AND_BUDGET="Acao.findAcaoByNameAndBudget";
	private static final long serialVersionUID = -1359075483819011154L;
	
	//bi-directional many-to-one association to Budget
	@ManyToOne
	@JoinColumn(name="budget_id")
	private Budget budget;

	@ManyToOne
	@JoinColumn(name="forecast_id")
	private Forecast forecast;
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

	public Forecast getForecast() {
		return forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}
	

}
