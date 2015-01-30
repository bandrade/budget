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
import com.dupont.budget.model.DespesaForecastAno;
import com.dupont.budget.model.DespesaForecastAnoPK;
import com.dupont.budget.model.DespesaForecastMes;
import com.dupont.budget.model.Forecast;
import com.dupont.budget.model.ForecastMensalisado;
import com.dupont.budget.model.ForecastMensalisadoPK;
import com.dupont.budget.model.MesEnum;
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
			//tx.commit();
			Set<DespesaForecast> despesas = new HashSet<>();;
			//tx.begin();
			for(int i=1;i<=12;i++)
			{
				ForecastMensalisado statusForecast= 
						new ForecastMensalisado(new ForecastMensalisadoPK(forecast.getId(), i),StatusForecastEnum.NAO_INICIADO,forecast);
				em.persist(statusForecast);
				forecast.getStatusForecasts().add(statusForecast);
			}
			//tx.commit();
			for(Despesa despesa : budget.getDespesas())
			{
				if(!despesa.getAprovado())
					continue;
				BudgetMes budgetMes = despesa.getDespesaMensalisada();
				//tx.begin();

				DespesaForecast despesaForecast = DespesaForecast.createFromDespesa(despesa);
				despesaForecast.setForecast(forecast);
				despesaForecast.setDespesaBudget(despesa);
				despesaForecast.setId(getGeneratedId());
				em.persist(despesaForecast);
				//tx.commit();
				//tx.begin();
				DespesaForecastMes despesaForecastMes = DespesaForecastMes.createFromBudgetMes(budgetMes);
				em.persist(despesaForecastMes);
				despesaForecast.setDespesaMensalisada(despesaForecastMes);
				em.merge(despesaForecast);
				//tx.commit();
				despesas.add(despesaForecast);
			}
			
			forecast.setDespesas(despesas);
			//tx.begin();
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
			Forecast forecast = em.find(Forecast.class, Long.valueOf(idForecast));
			ForecastMensalisado forecastMensalidado = obterForecastMensalidadeEmAndamento(forecast);
			
			Integer mesSeguinte = ((int)forecastMensalidado.getPk().getMes()+1);

			//mes de dezembro eh o ultimo mes do ano
			if(mesSeguinte > MesEnum.DEZEMBRO.getId())
			{
				return ;
			}
			
			tx.begin();
			for(DespesaForecast despesaForecast :forecast.getDespesas())
			{
			
				despesaForecast.setDespesaMensalisada(em.createNamedQuery("DespesaForecastAno.obterDespesasMensalizada",DespesaForecastMes.class)
				.setParameter("forecastId", forecast.getId())
				.setParameter("mes", forecastMensalidado.getPk().getMes()).getSingleResult());
				DespesaForecastMes despesaForecastMes = DespesaForecastMes.createFromDespesaMes(despesaForecast.getDespesaMensalisada());
				em.persist(despesaForecastMes);
				DespesaForecastAno despesaForecastAno = new DespesaForecastAno(new DespesaForecastAnoPK(despesaForecast.getId(),despesaForecastMes.getId()),mesSeguinte,despesaForecast,despesaForecastMes);
				em.persist(despesaForecastAno);
				despesaForecast.getDespesaForecastMes().add(despesaForecastAno);
				em.merge(despesaForecast);

			}
			forecastMensalidado.setStatusForecast(StatusForecastEnum.FINALIZADO);
			em.merge(forecastMensalidado);
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
	public ForecastMensalisado obterForecastMensalidadeEmAndamento(Forecast forecast)
	{
		ForecastMensalisado forecastMensalido = em.createNamedQuery("ForecastMensalisado.findByForecastEmAndamento",ForecastMensalisado.class)
		.setParameter("forecast_id", forecast.getId()).getSingleResult();
		return forecastMensalido;
		
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
		MesEnum mesEnum = MesEnum.valueOf(mes.toUpperCase());
		Forecast forecast = findForecastByCCAndAno(ano, idCentroCusto);
		for(DespesaForecast despesa : forecast.getDespesas())
		{
			obterValorComprometidoDespesa(forecast, despesa, mes);
			despesa.setYtd(obterYtd(despesa,mesEnum.getId()));
		}

		return new ArrayList<DespesaForecast>(forecast.getDespesas());
	}
	
	public void obterValorComprometidoDespesa(Forecast forecast, DespesaForecast despesa,String mes)
	{
		
		MesEnum mesEnum = MesEnum.valueOf(mes.toUpperCase());
		
		for(long _mes = mesEnum.getId() ; _mes<=MesEnum.DEZEMBRO.getId();_mes++)
		{
			//obtendo valores comprometidos cadastrados
			ValorComprometido valorComprometido =domainServiceBean.findValorComprometidoByFiltro(forecast.getBudget().getCentroCusto().getCodigo(), despesa.getTipoDespesa().getNome(), despesa.getAcao().getNome(),
					_mes);
			
			//obtendo valor total de notas que foram criadas no cover sheet e estao comprometidas
			//obtendo valor total de notas que estao pendentes de validacao no SAP
			
			Double valorD=+ obterValoresComprometidosNotas(despesa,(int)_mes);
			valorD+= valorComprometido !=null ? valorComprometido.getValor() : 0D;
			if(valorD !=null && valorD !=0D)
			{
				switch (MesEnum.values()[(int)_mes-1]) {
				case JANEIRO:
					
					if( despesa.getDespesaMensalisada().getDespesaJaneiro()==null)
						despesa.getDespesaMensalisada().setDespesaJaneiro(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaJaneiro(valorD+despesa.getDespesaMensalisada().getDespesaJaneiro());
					break;
				case FEVEREIRO:
					if( despesa.getDespesaMensalisada().getDespesaFevereiro()==null)
						despesa.getDespesaMensalisada().setDespesaFevereiro(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaFevereiro(valorD+despesa.getDespesaMensalisada().getDespesaFevereiro());
					break;

				case MARCO:
					if( despesa.getDespesaMensalisada().getDespesaMarco()==null)
						despesa.getDespesaMensalisada().setDespesaMarco(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaMarco(valorD+despesa.getDespesaMensalisada().getDespesaMarco());
					break;

				case ABRIL:
					if( despesa.getDespesaMensalisada().getDespesaAbril()==null)
						despesa.getDespesaMensalisada().setDespesaAbril(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaAbril(valorD+despesa.getDespesaMensalisada().getDespesaAbril());
					
					break;
					
				case MAIO:
					if(despesa.getDespesaMensalisada().getDespesaMaio()==null)
						despesa.getDespesaMensalisada().setDespesaMaio(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaMaio(valorD+despesa.getDespesaMensalisada().getDespesaAbril());
					
					break;
				
				case JUNHO:
					if(despesa.getDespesaMensalisada().getDespesaJunho()==null)
						despesa.getDespesaMensalisada().setDespesaJunho(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaJunho(valorD+despesa.getDespesaMensalisada().getDespesaJunho());
					
					break;

				case JULHO:
					if(despesa.getDespesaMensalisada().getDespesaJulho() ==null)
						despesa.getDespesaMensalisada().setDespesaJulho(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaJulho(valorD+despesa.getDespesaMensalisada().getDespesaJulho());
					
					break;

				case AGOSTO:
					if(despesa.getDespesaMensalisada().getDespesaAgosto()==null)
						despesa.getDespesaMensalisada().setDespesaAgosto(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaAgosto(valorD+despesa.getDespesaMensalisada().getDespesaAgosto());
					break;
				
				case SETEMBRO:
					if(despesa.getDespesaMensalisada().getDespesaSetembro() ==null)
						despesa.getDespesaMensalisada().setDespesaSetembro(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaSetembro(valorD+despesa.getDespesaMensalisada().getDespesaSetembro());
					break;
			
				case OUTUBRO:
					if(despesa.getDespesaMensalisada().getDespesaOutubro()==null)
						despesa.getDespesaMensalisada().setDespesaOutubro(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaOutubro(valorD+despesa.getDespesaMensalisada().getDespesaOutubro());
					break;
			
				case NOVEMBRO:
					if(despesa.getDespesaMensalisada().getDespesaNovembro()==null)
						despesa.getDespesaMensalisada().setDespesaNovembro(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaNovembro(valorD+despesa.getDespesaMensalisada().getDespesaNovembro());
					break;
			
				case DEZEMBRO:
					if(despesa.getDespesaMensalisada().getDespesaDezembro()==null)
						despesa.getDespesaMensalisada().setDespesaDezembro(valorD);
					else
						despesa.getDespesaMensalisada().setDespesaDezembro(valorD+despesa.getDespesaMensalisada().getDespesaDezembro());
					break;
			
				}
			}
		}
	}

	public void incluirDespesaForecast(DespesaForecast despesaForecast,Integer mes) throws Exception
	{
		despesaForecast.setAtivo(true);
		try
		{
			tx.begin();
			Long nextId =getGeneratedId();
			despesaForecast.setId(nextId);
			DespesaForecastAno despesaForecastAno = null;
			if(despesaForecast.getDespesaMensalisada() !=null && despesaForecast.getDespesaMensalisada().getId()!=null)
			{
				DespesaForecastAnoPK despesaForecastAnoPK = new DespesaForecastAnoPK(despesaForecast.getId(),despesaForecast.getDespesaMensalisada().getId());
				despesaForecastAno = new DespesaForecastAno(despesaForecastAnoPK,
						(int)mes,despesaForecast,despesaForecast.getDespesaMensalisada());
			}
			else
			{
				DespesaForecastMes despesaForecastMes = new DespesaForecastMes();
				em.persist(despesaForecastMes);
				DespesaForecastAnoPK despesaForecastAnoPK = new DespesaForecastAnoPK(despesaForecast.getId(),despesaForecastMes.getId());
				despesaForecastAno = new DespesaForecastAno(despesaForecastAnoPK,
						(int)mes,despesaForecast,despesaForecastMes);

			}
			
			em.persist(despesaForecast);
			despesaForecastAno.setDespesaForecast(despesaForecast);
			em.persist(despesaForecastAno);
			despesaForecast.getDespesaForecastMes().add(despesaForecastAno);
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

	public Double obterYtd(DespesaForecast despesaForecast,Long mes)
	{
		Double valorComprometido = 0D;
		String meses_ytd = "";
		Long mes_id = mes;
		for(long _mes = 0; _mes<mes_id ;_mes++ )
		{
			meses_ytd=""+(++_mes)+",";
		}
		meses_ytd = meses_ytd.substring(0, meses_ytd.length()-1);
		try
		{
			Object result = em.createNamedQuery("SolicitacaoPagamento.ObterSomaValorYTD")
				.setParameter("ano", despesaForecast.getForecast().getBudget().getAno())
			    .setParameter("meses",meses_ytd)
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

	
	public Double obterValoresComprometidosNotas(DespesaForecast despesaForecast, int mes)
	{
		Double valorComprometido = 0D;
		try
		{
			Object result = em.createNamedQuery("SolicitacaoPagamento.ObterSomaValorComprometido")
				.setParameter("ano", despesaForecast.getForecast().getBudget().getAno())
				.setParameter("centro_custo_id",despesaForecast.getForecast().getBudget().getCentroCusto().getId())
			    .setParameter("mes_anterior",mes)
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
	
	
	public List<DetalheValoresComprometidosDTO> obterDetalheValoresComprometidos(DespesaForecast despesa, int mes)
	{
		List<DetalheValoresComprometidosDTO> detalheList = obterDetalheValoresComprometidosNotas(despesa,mes);
		if(detalheList == null)
			detalheList = new ArrayList<>();
		ValorComprometido valor =domainServiceBean.findValorComprometidoByFiltro(despesa.getForecast().getBudget().getCentroCusto().getCodigo(), despesa.getTipoDespesa().getNome(), despesa.getAcao().getNome(),(long)mes);
		if(valor !=null)
		{
			DetalheValoresComprometidosDTO detalhe = new DetalheValoresComprometidosDTO();
			detalhe.setValor(valor.getValor());
			detalhe.setTipoDespesa(valor.getTipoDespesa().getNome());
			detalhe.setAcao(valor.getAcao().getNome() );
			detalheList.add(detalhe);
		}
		
		return detalheList;
		
	}
	public List<DetalheValoresComprometidosDTO> obterDetalheValoresComprometidosNotas(DespesaForecast despesaForecast, int mes)
	{
		List<DetalheValoresComprometidosDTO> detalheList = new ArrayList<>();
		try
		{
			List<Object[]> result =(List<Object[]>)em.createNamedQuery("SolicitacaoPagamento.ObterDetalheValorComprometido")
				.setParameter("ano", despesaForecast.getForecast().getBudget().getAno())
				.setParameter("centro_custo_id",despesaForecast.getForecast().getBudget().getCentroCusto().getId())
			    .setParameter("mes_anterior",mes)
			    .setParameter("acao_id", despesaForecast.getAcao().getId())
				.setParameter("tipo_despesa_id", despesaForecast.getTipoDespesa().getId()).getResultList();
			if(result !=null)
			{
				for(Object[] object : result)
				{
					DetalheValoresComprometidosDTO detalhe =  new DetalheValoresComprometidosDTO();
					detalhe.setValor(Double.valueOf(object[0]+""));
					detalhe.setTipoDespesa(String.valueOf(object[1]));
					detalhe.setAcao(String.valueOf(object[2]));
					detalhe.setNotaFiscal(String.valueOf(object[3]));
					detalheList.add(detalhe);
				}
				
			}
				
		}
		catch(NoResultException e)
		{
		}
		return detalheList;
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
			TipoDespesa tipoDespesa, Acao acao)  {
		DespesaForecast despesaForecast = null;
		try
		{
			Long acaoId =  acao !=null ? acao.getId():null;
			despesaForecast =em.createNamedQuery("DespesaForecast.findByForecastTipoDespesaAndAcao", DespesaForecast.class)
			.setParameter("forecastId", forecast.getId())
			.setParameter("tipoDespesaId",tipoDespesa.getId())
			.setParameter("acaoId",acaoId )
			.getSingleResult();
		}
		catch(NoResultException e)
		{
				return null;
		}
		
		return despesaForecast;
	}

	@Override
	public boolean isDespesaExistente(DespesaForecast despesa) {
		return obterDespesaPorTipoEAcao(despesa)!=null;
	}

	@Override
	public DespesaForecast obterDespesaPorTipoEAcao(DespesaForecast despesa) {
		
		return obterDespesaForecast(despesa.getForecast(), despesa.getTipoDespesa(), despesa.getAcao());
	}



}
