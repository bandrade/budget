package com.dupont.budget.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;

import com.dupont.budget.dto.BudgetAreaDTO;
import com.dupont.budget.dto.DespesaMesDTO;
import com.dupont.budget.dto.DespesasAgrupadasDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.model.BudgetEstipuladoAno;
import com.dupont.budget.model.BudgetMes;
import com.dupont.budget.model.Despesa;
import com.dupont.budget.model.StatusBudget;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.report.model.ReportBudgetOrcadoUtilizadoDetail;
import com.dupont.budget.report.model.ReportBudgetOrcadoUtilizadoDistribuicaoDetail;
import com.dupont.budget.report.model.ReportBudgetOrcadoUtilizadoDistribuicaoMaster;
import com.dupont.budget.report.model.ReportBudgetOrcadoUtilizadoMaster;
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


	public List<DespesaMesDTO> obterDespesaNoDetalheBudgetAsDTO(Long budgetId) throws Exception {

		List<Despesa> despesas = obterDespesaNoDetalheBudget(budgetId);
		List<DespesaMesDTO> despesasMes = new ArrayList<>();
		for(Despesa despesa : despesas)
		{
			DespesaMesDTO despesaMes = new DespesaMesDTO();
			despesaMes.setIdDespesa(despesa.getId());
			despesaMes.setTipoDespesa(despesa.getTipoDespesa().getNome());
			despesaMes.setCliente(despesa.getCliente() !=null ? despesa.getCliente().getNome(): null);
			despesaMes.setVendedor(despesa.getVendedor() !=null ? despesa.getVendedor().getNome() : "");
			despesaMes.setAcao(despesa.getAcao() !=null ? despesa.getAcao().getNome() : "");
			despesaMes.setProduto(despesa.getProduto() !=null ? despesa.getProduto().getNome() : "");
			despesaMes.setCultura(despesa.getCultura() !=null ? despesa.getCultura().getNome() : "");
			despesaMes.setDistrito(despesa.getDistrito() !=null ? despesa.getDistrito().getNome() : "");
			despesaMes.setValor(despesa.getValor());
			despesaMes.setComentario(despesa.getComentario());
			despesasMes.add(despesaMes);
		}

		return despesasMes;
	}

	public void mensalisarBudget(List<DespesaMesDTO> despesas) throws Exception
	{
		for(DespesaMesDTO despesa : despesas)
		{
			BudgetMes budgetMes = new BudgetMes();
			budgetMes.setId(despesa.getIdDespesa());
			budgetMes.setJaneiro(despesa.getJaneiro());
			budgetMes.setFevereiro(despesa.getFevereiro());
			budgetMes.setMarco(despesa.getMarco());
			budgetMes.setAbril(despesa.getAbril());
			budgetMes.setMaio(despesa.getMaio());
			budgetMes.setJunho(despesa.getJunho());
			budgetMes.setJulho(despesa.getJulho());
			budgetMes.setSetembro(despesa.getSetembro());
			budgetMes.setNovembro(despesa.getNovembro());
			budgetMes.setDezembro(despesa.getDezembro());
			BudgetMes _budgetMes= null;
			try
			{
				_budgetMes= em.find(BudgetMes.class, budgetMes.getId());
			}
			catch(NoResultException nre)
			{
				logger.debug("Nao existe budget mes submetido deste ano");
			}
			if(_budgetMes !=null)
			{
				em.merge(budgetMes);
			}
			else
			{
				em.persist(budgetMes);

			}

		}
	}

	@Override
	public List<Despesa> obterDespesaNoDetalhe(Long tipoDespesaId,Long budgetId)  throws Exception{

		return em.createNamedQuery("Despesa.obterDespesaNoDetalhe",Despesa.class).setParameter("budgetId", budgetId)
				.setParameter("id", tipoDespesaId).getResultList();
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
	public Budget findByAnoAndCentroDeCusto(String ano, Long centroDeCustoId) {
		Budget budget = null;
		try
		{
			budget =em.createNamedQuery("Budget.findByAnoAndCentroDeCusto", Budget.class)
				.setParameter("centroDeCustoId", centroDeCustoId).setParameter("ano", ano).getSingleResult();
		}
		catch(NoResultException e)
		{
			logger.info("Nenhum resultado encontrado");
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

	@Path("submeter")
	@POST
	public void submeterBudget(String budgetId){
 			Budget budget = em.find(Budget.class,Long.valueOf(budgetId));
			budget.setStatus(StatusBudget.SUBMETIDO);
			em.merge(budget);
			for(Despesa despesa : budget.getDespesas())
			{
				despesa.setValorProposto(despesa.getValor());
				em.merge(despesa);
			}
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
									  .setParameter("ano", ano)
									  .getResultList();

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
				budgetAno = obterValoresAprovadosESubmetidos(budget.getArea().getId(), budget.getAno());

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

	public BudgetEstipuladoAno obterValoresAprovadosESubmetidos(Long areaId, String ano) throws Exception
	{

		BudgetEstipuladoAno budgetAno = em.createNamedQuery("BudgetEstipulado.findByAnoAndArea",BudgetEstipuladoAno.class)
				.setParameter("ano",ano)
				.setParameter("area_id",areaId).getSingleResult();
		return budgetAno;
	}

	public List<Budget> obterBudgetsPorArea (Long areaId, String ano) throws Exception
	{

		List<Budget> budgets = em.createNamedQuery("Budget.findByAnoAndArea",Budget.class)
				.setParameter("ano",ano)
				.setParameter("area_id",areaId).getResultList();
		for(Budget budget : budgets )
		{
			budget.setValorTotalBudget(em.createNamedQuery("Despesa.obterSomaDespesa",Double.class)
			.setParameter("budgetId",budget.getId()).getSingleResult());
			budget.setValorTotalProposto(em.createNamedQuery("Despesa.obterSomaValorPropostoDespesa",Double.class)
					.setParameter("budgetId",budget.getId()).getSingleResult());

		}
		return budgets;
	}


	@Path("aprovar")
	@POST
	public void aprovarDespesasBudget(String budgetId) {
		em.createNamedQuery("Despesa.aprovarDespesasBudget")
		.setParameter("budgetId",Long.valueOf(budgetId)).executeUpdate();


	}

	
	@Override
	public List<ReportBudgetOrcadoUtilizadoMaster> getBudgetOrcadoUtilizadoTipoDespesaAcaoReport(String ano, Long centroCustoId) {
		
		Budget budget = findByAnoAndCentroDeCusto(ano, centroCustoId);
		
		if( budget == null )
			return null;
		
		List<Object[]> budgetQueryResult   = em.createNamedQuery("Report.Budget.Orcado.TipoDespesa_Acao", Object[].class)
									           .setParameter("budgetId", budget.getId())
									           .getResultList();
		
		List<Object[]> utilizadoQueryResult = em.createNamedQuery("Report.SolicPag.Utilizado.TipoDespesa_Acao", Object[].class)
									           .setParameter("ano", new Integer(ano))
									           .setParameter("centroCustoId", centroCustoId)
									           .getResultList();
		
		return createBudgetOrcadoUtilizadoReport(budgetQueryResult, utilizadoQueryResult);
	}
	

	@Override
	public List<ReportBudgetOrcadoUtilizadoDistribuicaoMaster> getBudgetOrcadoUtilizadoTipoDespesaAcaoDistribuicaoReport(String ano, Long centroCustoId) {
		
		// São os mesmos dados desse relatório, mas será enriquecido.
		List<ReportBudgetOrcadoUtilizadoMaster> oldReport = getBudgetOrcadoUtilizadoTipoDespesaAcaoReport(ano,centroCustoId);
				
		if( oldReport == null )
			return null;
		
		// Cria um cache apra recuperar os valores a seguir e trocar os objetos para uma versao extendida
		List<ReportBudgetOrcadoUtilizadoDistribuicaoMaster> newReport = new ArrayList<ReportBudgetOrcadoUtilizadoDistribuicaoMaster>();
		Map<String, ReportBudgetOrcadoUtilizadoDistribuicaoMaster> cacheMap = new HashMap<String, ReportBudgetOrcadoUtilizadoDistribuicaoMaster>();
		
		for (ReportBudgetOrcadoUtilizadoMaster item : oldReport) {
			
			// Troca a lista de detalhes
			List<ReportBudgetOrcadoUtilizadoDetail> newDetails = new ArrayList<ReportBudgetOrcadoUtilizadoDetail>();
			List<ReportBudgetOrcadoUtilizadoDetail> oldDetails = item.getDetails();
			
			for (int i = 0; i < oldDetails.size(); i++) {
				
				ReportBudgetOrcadoUtilizadoDetail oldDetail = oldDetails.get(i); 
				ReportBudgetOrcadoUtilizadoDistribuicaoDetail newDetail 
									= new ReportBudgetOrcadoUtilizadoDistribuicaoDetail(oldDetail.getDetail(), oldDetail.getOrcado(), oldDetail.getUtilizado(), 0.0);
				
				newDetails.add(newDetail);
			}
		
			// Troca o objeto mater
			ReportBudgetOrcadoUtilizadoDistribuicaoMaster newMaster = new ReportBudgetOrcadoUtilizadoDistribuicaoMaster(item.getMaster(), item.getTotalOrcado(), item.getTotalUtilizado(), 0.0);			
			newMaster.setDetails(newDetails);			
			newReport.add(newMaster);
			cacheMap.put(item.getMaster(), newMaster);
		}
		
		// Busca novos dados do relatorio
		Budget budget = findByAnoAndCentroDeCusto(ano, centroCustoId);
		
		List<Object[]> forecastQueryResult   = em.createNamedQuery("Report.Forecast.TipoDespesa_Acao", Object[].class)
									           .setParameter("budgetId", budget.getId())
									           .getResultList();
		
		for (Object[] line : forecastQueryResult) {
			
			String master    = (String) line[0];
			String detail    = (String) line[1];
			Double forecast  = (Double) line[2];
			
			ReportBudgetOrcadoUtilizadoDistribuicaoMaster item = cacheMap.get(master);
			
			if( item == null ){
				continue;
			}
			
			ReportBudgetOrcadoUtilizadoDistribuicaoDetail _detail = (ReportBudgetOrcadoUtilizadoDistribuicaoDetail) item.getDetail(detail);
			
			if( _detail == null)
				continue;
			
			// Adicona o valor utilizado total da despesa
			item.setTotalForecast(item.getTotalForecast() + forecast);;
			_detail.setForecast(forecast);
		}
		
		
		return newReport;
	}
	
	@Override
	public List<ReportBudgetOrcadoUtilizadoMaster> getBudgetOrcadoUtilizadoCentroCustoCulturaReport(String ano, Long centroCustoId) {
				
		List<Long> budgetsIds = new ArrayList<Long>();
		
		if( centroCustoId != null ) {
			Budget budget = findByAnoAndCentroDeCusto(ano, centroCustoId);
			
			if( budget != null )
				budgetsIds.add(budget.getId());
		} else {
			List<Budget> budgets = findBudgetsByAno(ano);
			
			if( !budgets.isEmpty() ) {
				for (Budget budget : budgets) {
					budgetsIds.add(budget.getId());
				}
			}
		}
		
		// Caso não possua budgets retornar nulo
		if( budgetsIds.isEmpty() )
			return null;
		
		List<Object[]> budgetQueryResult   = em.createNamedQuery("Report.Budget.Orcado.CentroCusto_Cultura", Object[].class)
									           .setParameter("budgetsIds", budgetsIds)
									           .getResultList();
		
		List<Object[]> utilizadoQueryResult = em.createNamedQuery("Report.SolicPag.Utilizado.CentroCusto_Cultura", Object[].class)
											   .setParameter("ano", new Integer(ano))
											   .setParameter("centroCustoId", centroCustoId)
									           .getResultList();
		
		return createBudgetOrcadoUtilizadoReport(budgetQueryResult, utilizadoQueryResult);
	}
	
	@Override
	public List<ReportBudgetOrcadoUtilizadoMaster> getBudgetOrcadoUtilizadoProdutoAcaoReport( String ano, Long centroCustoId) {
		
		Budget budget = findByAnoAndCentroDeCusto(ano, centroCustoId);
		
		if( budget == null )
			return null;
		
		List<Object[]> budgetQueryResult   = em.createNamedQuery("Report.Budget.Orcado.Produto_Acao", Object[].class)
									           .setParameter("budgetId", budget.getId())
									           .getResultList();
		
		List<Object[]> utilizadoQueryResult = em.createNamedQuery("Report.SolicPag.Utilizado.Produto_Acao", Object[].class)
											   .setParameter("ano", new Integer(ano))
											   .setParameter("centroCustoId", centroCustoId)
									           .getResultList();
		
		return createBudgetOrcadoUtilizadoReport(budgetQueryResult, utilizadoQueryResult);
	}


	protected List<ReportBudgetOrcadoUtilizadoMaster> createBudgetOrcadoUtilizadoReport( List<Object[]> budgetQueryResult, List<Object[]> utilizadoQueryResult) {
		
		List<ReportBudgetOrcadoUtilizadoMaster> result          = new ArrayList<ReportBudgetOrcadoUtilizadoMaster>();		
		Map<String, ReportBudgetOrcadoUtilizadoMaster> cacheMap = new HashMap<String, ReportBudgetOrcadoUtilizadoMaster>();
		
		// Carrega os dados do BUDGET: ORÇADO
		for ( Object[] line : budgetQueryResult ) {
			
			String master = (String) line[0];
			String detail = (String) line[1];
			Double orcado = (Double) line[2];
			
			ReportBudgetOrcadoUtilizadoMaster item = cacheMap.get(master);
			
			if( item == null ) {
				item = new ReportBudgetOrcadoUtilizadoMaster(master, 0.0, 0.0);
				result.add(item);
			}
			
			ReportBudgetOrcadoUtilizadoDetail acaoReport = new ReportBudgetOrcadoUtilizadoDetail(detail, orcado, 0.0);
			
			item.addDetail(acaoReport);			
			
			
			cacheMap.put(master, item);
		}
		
		// Carrega os dados UTILIZADO
		for ( Object[] line : utilizadoQueryResult ) {
			
			String master    = (String) line[0];
			String detail    = (String) line[1];
			Double utilizado = (Double) line[2];
			
			ReportBudgetOrcadoUtilizadoMaster item = cacheMap.get(master);
			
			if( item == null ){
				// Lançar erro
				continue;
			}
			
			ReportBudgetOrcadoUtilizadoDetail _detail = item.getDetail(detail);
			
			if( _detail == null)
				continue;
			
			// Adicona o valor utilizado total da despesa
			item.setTotalUtilizado(item.getTotalUtilizado() + utilizado);
			_detail.setUtilizado(utilizado);
		}
		
		return result;
	}

	@Override
	public List<String> getBudgetsAnos() {
		
		List<String> result = em.createQuery("select distinct b.ano from Budget b", String.class)
								.getResultList();
				
		return result;
	}


	@Override
	public List<Budget> findBudgetsByAno(String ano) {
		
		List<Budget> result = em.createNamedQuery("Budget.findByAno", Budget.class)
								.setParameter("ano", ano)
								.getResultList();
		
		return result;
	}


	@Override
	public void encerrarBudget(Budget budget) {
		budget.setStatus(StatusBudget.FINALIZADO);
		em.merge(budget);
	}




	


	
}
