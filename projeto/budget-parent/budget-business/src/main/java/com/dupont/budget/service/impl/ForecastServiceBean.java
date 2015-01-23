package com.dupont.budget.service.impl;

import java.util.ArrayList;
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

import com.dupont.budget.dto.DetalheValoresComprometidosDTO;
import com.dupont.budget.model.Acao;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetMes;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.DespesaForecastMes;
import com.dupont.budget.model.DespesaForecastPK;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.ForecastMensalisado;
import com.dupont.budget.model.ForecastMensalisadoPK;
import com.dupont.budget.model.MesEnum;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusForecastEnum;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.ValorComprometido;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.DomainService;
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

	@Inject
	private DomainService domainServiceBean;

	@Path("criar")
	@POST
	public void criarPrimeiroForecast(String budgetId) throws Exception {
		try
		{
			//CRIANDO FORECAST
			tx.begin();
			Budget budget =em.find(Budget.class, Long.valueOf(budgetId));
			budgetService.encerrarBudget(budget);
			Forecast forecast = new Forecast(budget.getUsuarioCriador(),budget.getCricao(),budget,budget.getCentroCusto(),budget.getAno());
			em.persist(forecast);
			tx.commit();
		
			for(long i=1;i<=12;i++)
			{
				tx.begin();
				ForecastMensalisado statusForecast= 
						new ForecastMensalisado(new ForecastMensalisadoPK(forecast.getId(), i),StatusForecastEnum.NAO_INICIADO,forecast);
				em.persist(statusForecast);
				tx.commit();
				forecast.getStatusForecasts().add(statusForecast);
			}
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
			//XXX
			Integer mesSeguinte = 0;

			//mes de dezembro eh o ultimo mes do ano
			if(mesSeguinte > MesEnum.DEZEMBRO.getId())
			{
				return ;
			}

			Set<DespesaForecast> despesasForecastMesSeguinte = new HashSet<>();
			for(DespesaForecast despesaForecast : forecast.getDespesas())
			{
				DespesaForecast despesaMesSeguinte = new DespesaForecast(despesaForecast,mesSeguinte);
				em.persist(despesaMesSeguinte);
				despesasForecastMesSeguinte.add(despesaMesSeguinte);
			}
			forecast.getDespesas().addAll(despesasForecastMesSeguinte);
			em.merge(forecast);
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

		for(DespesaForecast despesa : forecast.getDespesas())
		{
			obterValorComprometidoDespesa(forecast, despesa, mes);
			//despesa.setValorComprometido(valorTotal);
			despesa.setYtd(obterYtd(despesa));
		}

		return new ArrayList<DespesaForecast>(forecast.getDespesas());
	}
	
	public void obterValorComprometidoDespesa(Forecast forecast, DespesaForecast despesa,String mes)
	{
		
		MesEnum mesEnum = MesEnum.valueOf(mes.toUpperCase());
		
		for(long _mes = mesEnum.getId() ; _mes<=MesEnum.DEZEMBRO.getId();_mes++)
		{
			ValorComprometido valorComprometido =domainServiceBean.findValorComprometidoByFiltro(forecast.getBudget().getCentroCusto().getCodigo(), despesa.getTipoDespesa().getNome(), despesa.getAcao().getNome(),
					_mes);
			
			Double valorTotal  = obterValoresComprometidosNotas(despesa);
			Double valorD=0d;
			if(valorComprometido!=null)
			{
				switch (MesEnum.values()[(int)_mes-1]) {
				case JANEIRO:
					
					valorD=  despesa.getDespesaMensalisada().getDespesaJaneiro();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaJaneiro(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaJaneiro(valorD+valorComprometido.getValor());
					break;
				case FEVEREIRO:
					valorD=  despesa.getDespesaMensalisada().getDespesaFevereiro();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaFevereiro(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaFevereiro(valorD+valorComprometido.getValor());
					break;

				case MARCO:
					valorD=  despesa.getDespesaMensalisada().getDespesaMarco();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaMarco(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaMarco(valorD+valorComprometido.getValor());
					break;

				case ABRIL:
					valorD=  despesa.getDespesaMensalisada().getDespesaAbril();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaAbril(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaAbril(valorD+valorComprometido.getValor());
					
					break;
					
				case MAIO:
					valorD=  despesa.getDespesaMensalisada().getDespesaMaio();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaMaio(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaMaio(valorD+valorComprometido.getValor());
					
					break;
				
				case JUNHO:
					valorD=  despesa.getDespesaMensalisada().getDespesaJunho();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaJunho(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaJunho(valorD+valorComprometido.getValor());
					
					break;

				case JULHO:
					valorD=  despesa.getDespesaMensalisada().getDespesaJulho();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaJulho(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaJulho(valorD+valorComprometido.getValor());
					
					break;

				case AGOSTO:
					valorD=  despesa.getDespesaMensalisada().getDespesaAgosto();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaAgosto(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaAgosto(valorD+valorComprometido.getValor());
					break;
				
				case SETEMBRO:
					valorD=  despesa.getDespesaMensalisada().getDespesaSetembro();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaSetembro(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaSetembro(valorD+valorComprometido.getValor());
					break;
			
				case OUTUBRO:
					valorD=  despesa.getDespesaMensalisada().getDespesaOutubro();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaOutubro(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaOutubro(valorD+valorComprometido.getValor());
					break;
			
				case NOVEMBRO:
					valorD=  despesa.getDespesaMensalisada().getDespesaNovembro();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaNovembro(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaNovembro(valorD+valorComprometido.getValor());
					break;
			
				case DEZEMBRO:
					valorD=  despesa.getDespesaMensalisada().getDespesaDezembro();
					if(valorD==null)
						despesa.getDespesaMensalisada().setDespesaDezembro(valorComprometido.getValor());
					else
						despesa.getDespesaMensalisada().setDespesaDezembro(valorD+valorComprometido.getValor());
					break;
			
				}
				valorTotal +=  valorComprometido.getValor();
			}
		}
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
		Long mes_id = despesaForecast.getDespesaPK().getMes();
		for(long _mes = 0; _mes<mes_id ;_mes++ )
		{
			meses_ytd=""+(++_mes)+",";
		}
		meses_ytd = meses_ytd.substring(0, meses_ytd.length()-1);
		try
		{
			Object result = em.createNativeQuery(SolicitacaoPagamento.QUERY_SOMA_YTD.toString())
				.setParameter("ano", despesaForecast.getForecast().getBudget().getAno())
			    .setParameter("meses",meses_ytd)
			    .setParameter("mes_forecast",mes_id)
			    .setParameter("centro_custo_id",despesaForecast.getForecast().getBudget().getCentroCusto().getId())
			    .setParameter("acao_id", despesaForecast.getAcao().getId())
				.setParameter("tipo_despesa_id", despesaForecast.getTipoDespesa().getId()).getSingleResult();
			if(result !=null)
				valorComprometido = Double.valueOf(result.toString());
		}
		catch(NoResultException e)
		{
		}
		return valorComprometido;

	}

	public Double obterValoresComprometidosNotas(DespesaForecast despesaForecast)
	{
		Double valorComprometido = 0D;
		try
		{
			Object result = em.createNativeQuery(SolicitacaoPagamento.QUERY_SOMA_VALOR_COMPROMETIDO.toString())
				.setParameter("ano", despesaForecast.getForecast().getBudget().getAno())
				.setParameter("centro_custo_id",despesaForecast.getForecast().getBudget().getCentroCusto().getId())
				.setParameter("mes_forecast",despesaForecast.getDespesaPK().getMes())
			    .setParameter("mes_anterior",despesaForecast.getDespesaPK().getMes()-1)
			    .setParameter("acao_id", despesaForecast.getAcao().getId())
				.setParameter("tipo_despesa_id", despesaForecast.getTipoDespesa().getId()).getSingleResult();
			if(result !=null)
				valorComprometido = Double.valueOf(result.toString());
		}
		catch(NoResultException e)
		{
		}
		return valorComprometido;
	}

	public List<DetalheValoresComprometidosDTO> obterDetalhesNotas()
	{
		return null;

	}

	@Override
	public boolean isForecastMensalisado(Long mes, String ano) {
		Forecast forecast = em.createNamedQuery("Forecast.findByAno", Forecast.class)
		.setParameter("ano", ano)
		.setMaxResults(1).getSingleResult();
		 
		ForecastMensalisado forecastMensalisado = 
				em.createNamedQuery("ForecastMensalisado.findByForecastMes",ForecastMensalisado.class)
				.setParameter("mes", mes)
				.setParameter("forecast_id", forecast.getId())
				.setMaxResults(1)
				.getSingleResult();
		
		return forecastMensalisado.getStatusForecast().equals(StatusForecastEnum.NAO_INICIADO);
	}

	@Override
	public void alterarForecastMensalisado(Long mes, String ano) throws Exception{
		List<Forecast> forecasts = em.createNamedQuery("Forecast.findByAno", Forecast.class)
				.setParameter("ano", ano)
				.getResultList();
		tx.begin();
		for(Forecast forecast : forecasts)
		{
			ForecastMensalisado forecastMensalisado = 
					em.createNamedQuery("ForecastMensalisado.findByForecastMes",ForecastMensalisado.class)
					.setParameter("mes", mes)
					.setParameter("forecast_id", forecast.getId())
					.setMaxResults(1)
					.getSingleResult();
			forecastMensalisado.setStatusForecast(StatusForecastEnum.EM_ANDAMENTO);
			em.merge(forecastMensalisado);
		}
		tx.commit();
	}
	

	@Override
	public Forecast findForecastByCCAndAno(String ano, Long centroCustoId) {
		Forecast forecast;
		try
		{
			 forecast = em.createNamedQuery("Forecast.findByAnoAndCC", Forecast.class)
					.setParameter("ano", ano)
					.setParameter("centroCustoId", centroCustoId)
					.getSingleResult();
			 
		}
		catch(NoResultException e)
		{
				return null;
		}
		
		return forecast;
	}

	@Override
	public DespesaForecast obterDespesaForecast(Forecast forecast,
			TipoDespesa tipoDespesa, Acao acao) throws Exception {
		DespesaForecast despesaForecast = null;
		try
		{
			despesaForecast =em.createNamedQuery("DespesaForecast.findByForecastTipoDespesaAndAcao", DespesaForecast.class)
			.setParameter("forecastId", forecast.getId())
			.setParameter("tipoDespesaId",tipoDespesa.getId())
			.setParameter("acaoId",acao.getId())
			.getSingleResult();
		}
		catch(NoResultException e)
		{
				return null;
		}
		
		return despesaForecast;
	}


}
