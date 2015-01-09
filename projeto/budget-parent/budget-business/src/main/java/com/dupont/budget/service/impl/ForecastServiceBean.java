package com.dupont.budget.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;

import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetMes;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.DespesaForecastMes;
import com.dupont.budget.model.DespesaForecastPK;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.MesEnum;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusForecast;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.ForecastService;
import com.dupont.budget.service.GenericService;

@Stateless
@Path("forecast")
@TransactionManagement(TransactionManagementType.BEAN)
public class ForecastServiceBean extends GenericService implements ForecastService{
	@Inject
	private Logger logger;

	@Resource
	private UserTransaction tx;

	@Inject
	private BudgetService budgetService;

	@Path("criar")
	@POST
	public void criarPrimeiroForecast(String budgetId) throws Exception {
		try
		{
			tx.begin();
			Budget budget =em.find(Budget.class, Long.valueOf(budgetId));
			Forecast forecast = new Forecast(1, budget.getUsuarioCriador(),budget.getCricao(),budget);
			forecast.setStatusForecast(StatusForecast.EM_ANDAMENTO);
			em.persist(forecast);
			tx.commit();
			Set<DespesaForecast> despesas = new HashSet<>();
			for(Despesa despesa : budget.getDespesas())
			{
				if(!despesa.getAprovado())
					continue;
				BudgetMes budgetMes = despesa.getDespesaMensalisada();
				tx.begin();

				DespesaForecast despesaForecast = DespesaForecast.createFromDespesa(despesa);
				despesaForecast.setForecast(forecast);
				despesaForecast.setDespesaBudget(despesa);
				despesaForecast.setDespesaPK(new DespesaForecastPK(budget.getAno(),1L,getGeneratedId()));
				em.persist(despesaForecast);
				tx.commit();
				tx.begin();
				DespesaForecastMes despesaForecastMes = DespesaForecastMes.createFromBudgetMes(budgetMes);
				em.persist(despesaForecastMes);
				despesaForecast.setDespesaMensalisada(despesaForecastMes);
				em.merge(despesaForecast);
				tx.commit();
				despesas.add(despesaForecast);
			}
			forecast.setDespesas(despesas);
			tx.begin();
			em.merge(forecast);
			tx.commit();

		}
		catch(Exception e)
		{
			if (tx.getStatus() == Status.STATUS_ACTIVE) {
				tx.rollback();
			}
			logger.error("Erro ao criar o forecast ",e);
			throw e;
		}


	}

	@Path("criarForecastSeguinte")
	@POST
	public void criarForecastSeguinte(String idForecast) throws Exception
	{
		try
		{
			tx.begin();
			Forecast forecast = em.find(Forecast.class, Long.valueOf(idForecast));
			Integer mesSeguinte = forecast.getMes()+1;

			//mes de dezembro eh o ultimo mes do ano
			if(mesSeguinte > MesEnum.DEZEMBRO.getId())
			{
				return ;
			}

			Forecast forecastMesSeguinte = new Forecast(mesSeguinte, forecast.getUsuarioCriador(),new Date(),forecast.getBudget());
			Set<DespesaForecast> despesasForecastMesSeguinte = new HashSet<>();
			for(DespesaForecast despesaForecast : forecast.getDespesas())
			{
				DespesaForecast despesaMesSeguinte = new DespesaForecast(despesaForecast,mesSeguinte);
				em.persist(despesaMesSeguinte);
				despesasForecastMesSeguinte.add(despesaMesSeguinte);
			}
			forecastMesSeguinte.setDespesas(despesasForecastMesSeguinte);
			forecastMesSeguinte.setStatusForecast(StatusForecast.EM_ANDAMENTO);
			em.persist(forecastMesSeguinte);
			forecast.setStatusForecast(StatusForecast.FINALIZADO);
			em.merge(forecastMesSeguinte);
			tx.commit();
		}
		catch(Exception e)
		{
			if (tx.getStatus() == Status.STATUS_ACTIVE) {
				tx.rollback();
			}
			throw e;
		}

	}
	private Long getGeneratedId()
	{
		Integer id = (Integer) em.createNativeQuery("select MAX(id) from despesa_forecast").getSingleResult();
		if(id==null)
			id=0;
		return Long.valueOf((++id).toString());
	}

	@Override
	public List<Forecast> findForecastsByBudgetId(Long id) {

		return em.createNamedQuery("Forecast.findByBudgetId", Forecast.class).setParameter("budgetId", id).getResultList();
	}

	public List<DespesaForecast> obterDespesasForecast(String mes, String  ano, Long idCentroCusto) throws Exception{
		Budget budget = budgetService.findByAnoAndCentroDeCusto(ano, idCentroCusto);
		Forecast forecast = em.createNamedQuery("Forecast.findByBudgetId", Forecast.class).setParameter("budgetId", budget.getId()).getResultList().get(0);
		//TODO IMPLEMENTAR VALORES COMPROMETIDOS

		for(DespesaForecast despesa : forecast.getDespesas())
		{
			//despesa.setValorComprometido(obterValorComprometido(despesa));
			despesa.setYtd(obterYtd(despesa));
		}

		return new ArrayList<DespesaForecast>(forecast.getDespesas());
	}

	public void incluirDespesaForecast(DespesaForecast despesaForecast) throws Exception
	{
		despesaForecast.setAtivo(true);
		try
		{
			tx.begin();
			em.persist(despesaForecast.getDespesaMensalisada());
			despesaForecast.getDespesaPK().setId(getGeneratedId());
			em.persist(despesaForecast);
			tx.commit();
		}
		catch(Exception e)
		{
			if (tx.getStatus() == Status.STATUS_ACTIVE) {
				tx.rollback();
			}
			throw e;
		}
	}
	@Override
	public void atualizarDespesaForecast(DespesaForecast despesaForecast)
			throws Exception {
		try
		{
			tx.begin();
			em.merge(despesaForecast.getDespesaMensalisada());
			em.merge(despesaForecast);
			tx.commit();
		}
		catch(Exception e)
		{
			if (tx.getStatus() == Status.STATUS_ACTIVE) {
				tx.rollback();
			}
			throw e;
		}

	}

	public Double obterYtd(DespesaForecast despesaForecast)
	{
		Double valorComprometido = 0D;
		String meses_ytd = "";
		Integer mes_id = despesaForecast.getForecast().getMes();
		for(int _mes = 0; _mes<mes_id ;_mes++ )
		{
			meses_ytd=""+(++_mes)+",";
		}
		meses_ytd = meses_ytd.substring(0, meses_ytd.length()-1);
		try
		{
			Object result = em.createNativeQuery(SolicitacaoPagamento.QUERY_SOMA_YTD.toString())
				.setParameter("ano", despesaForecast.getForecast().getBudget().getAno())
			    .setParameter("meses",meses_ytd)
			    .setParameter("mes_forecast",despesaForecast.getForecast().getMes())
			    .setParameter("centro_custo_id",despesaForecast.getForecast().getBudget().getCentroCusto().getId())
			  //  .setParameter("produto_id",despesaForecast.getProduto().getId())
			    .setParameter("acao_id", despesaForecast.getAcao().getId())
				.setParameter("tipo_despesa_id", despesaForecast.getTipoDespesa().getId()).getSingleResult();
			//	.setParameter("cultura_id", despesaForecast.getCultura().getId())
			//	.setParameter("distrito_id", despesaForecast.getDistrito().getId())
			//	.setParameter("cliente_id", despesaForecast.getCliente().getId())
			//	.setParameter("vendedor_id", despesaForecast.getVendedor().getId()).getSingleResult();
			if(result !=null)
				valorComprometido = Double.valueOf(result.toString());
		}
		catch(NoResultException e)
		{
		}
		return valorComprometido;


	}

	public Double obterValoresComprometidos(DespesaForecast despesaForecast)
	{
		Double valorComprometido = 0D;
		try
		{
			Object result = em.createNativeQuery(SolicitacaoPagamento.QUERY_SOMA_YTD.toString())
				.setParameter("ano", despesaForecast.getForecast().getBudget().getAno())
			    .setParameter("mes", despesaForecast.getForecast().getMes())
			    .setParameter("acao_id", despesaForecast.getAcao().getId())
				.setParameter("tipo_despesa_id", despesaForecast.getTipoDespesa().getId());
			if(result !=null)
				valorComprometido = Double.valueOf(result.toString());
		}
		catch(NoResultException e)
		{
		}
		return valorComprometido;


	}

	public void obterDetalhesNotas()
	{


	}
}
