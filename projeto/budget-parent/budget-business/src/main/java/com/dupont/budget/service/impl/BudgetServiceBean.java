package com.dupont.budget.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;

import com.dupont.budget.dto.BudgetAreaDTO;
import com.dupont.budget.dto.DespesasAgrupadasDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetEstipuladoAno;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.StatusBudget;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.GenericService;

@Stateless
@Path("budget")
public class BudgetServiceBean extends GenericService implements BudgetService {

	@Inject
	private Logger logger;
	@Override
	public List<DespesasAgrupadasDTO> obterDespesaAgrupadas(Long budgetId) throws Exception{

		List<Object[]> result = em.createNamedQuery("Despesa.agruparPorTipoDeDespesa",Object[].class)
		.setParameter("id", budgetId).getResultList();
		List<DespesasAgrupadasDTO> despesas = new ArrayList<DespesasAgrupadasDTO>();
		for(Object[] object : result)
		{
			DespesasAgrupadasDTO despesa =  new DespesasAgrupadasDTO();
			TipoDespesa t = (TipoDespesa) object[0];
			despesa.setTipoDespesaId(t.getId());
			despesa.setNomeDespesa(t.getNome());
			despesa.setValorAgrupado((Double) object[1] );
			despesas.add(despesa);
		}

		return despesas;
	}


	@Override
	public List<Despesa> obterDespesaNoDetalheBudget(Long budgetId) throws Exception {

		return em.createNamedQuery("Despesa.obterDespesaNoDetalheBudget",Despesa.class).setParameter("budgetId", budgetId)
				.getResultList();
	}

	@Override
	public List<Despesa> obterDespesaNoDetalhe(Long tipoDespesaId,Long budgetId)  throws Exception{

		return em.createNamedQuery("Despesa.obterDespesaNoDetalhe",Despesa.class).setParameter("budgetId", budgetId).getResultList();
	}

	@Override
	public Budget insertBudget(Budget budget) throws Exception{
		em.persist(budget);
		return budget;
	}

	@Override
	public void insertItemDespesa(Despesa despesa) throws Exception {
		em.persist(despesa);
	}


	@Override
	public void updateItemDespesa(Despesa despesa)  throws Exception{
		em.merge(despesa);

	}

	@Override
	public Budget findByAnoAndCentroDeCusto(String ano, Long centroDeCustoId) throws Exception{
		Budget budget = null;
		try
		{
			budget =em.createNamedQuery("Budget.findByAnoAndCentroDeCusto", Budget.class)
				.setParameter("centroDeCustoId", centroDeCustoId).setParameter("ano", ano).getSingleResult();
		}
		catch(NoResultException e)
		{
			logger.info("Nenhum resultado encontrado");
			throw e;
		}

		return  budget;
	}


	@Override
	public void atualizarDespesas(List<Despesa> despesasNoDetalhe) throws Exception {

		for(Despesa despesa : despesasNoDetalhe)
		{
			em.merge(despesa);
		}
	}

	@POST
	public void submeterBudget(String budgetId){
 			Budget budget = em.find(Budget.class,Long.valueOf(budgetId));
			budget.setStatus(StatusBudget.SUBMETIDO);
			em.merge(budget);
	}


	@Override
	public List<BudgetAreaDTO> listarBudgetsAprovadosPorArea(String ano) throws Exception{
		StringBuilder query = new StringBuilder()
			.append(	" select area.id, area.nome, sum(despesa.valor),'OK' from budget")
			.append(	" inner join despesa on despesa.budget_id = budget.id")
			.append(	" inner join centro_custo on centro_custo.id = budget.centro_custo_id")
			.append(	" inner join area on centro_custo.area_id = area.id")
			.append(	" where budget.ano=:ano and despesa.aprovacao=1")
			.append(	" group by area.id");

		List<Object[]> result = em.createNativeQuery(query.toString())
				.setParameter("ano", ano).getResultList();

		List<BudgetAreaDTO> lista = new ArrayList<>();
		for(Object[] object : result)
		{
			BudgetAreaDTO budget = new BudgetAreaDTO();
			budget.setIdArea(Long.valueOf(String.valueOf(object[0])));
			budget.setNomeArea(String.valueOf(object[1]));
			budget.setValorTotalBudget(Double.valueOf(String.valueOf(object[2])));
			budget.setStatus(String.valueOf(object[3]));
			lista.add(budget);

		}

		return lista;
	}


	@Override
	public void adicionarBudgetsSubmetidos(
			List<BudgetEstipuladoAno> budgets) throws Exception {
		for(BudgetEstipuladoAno budget : budgets)
		{
			BudgetEstipuladoAno budgetAno = null;
			try
			{
				budgetAno = em.createNamedQuery("BudgetEstipulado.findByAnoAndArea",BudgetEstipuladoAno.class)
				.setParameter("ano", budget.getAno())
				.setParameter("area_id", budget.getArea().getId()).getSingleResult();
			}
			catch(NoResultException nre)
			{
				logger.debug("Nao existe budget submetido deste ano");
			}
			if(budgetAno !=null)
			{

				budgetAno.setValorSubmetido(budget.getValorSubmetido());
				budgetAno.setValorAprovado(budget.getValorAprovado());
				em.merge(budgetAno);
			}
			else
			{
				em.persist(budget);
			}
		}
	}


}
