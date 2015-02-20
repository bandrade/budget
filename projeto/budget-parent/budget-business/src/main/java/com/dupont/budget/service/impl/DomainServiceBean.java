package com.dupont.budget.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.dupont.budget.model.Acao;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.NamedAbstractEntity;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.TipoSolicitacao;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.model.ValorComprometido;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.GenericService;

/**
 * Implementação do serviço de domínio. O serviço grava as entidades de domínio em um banco de dados relacional.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Stateless
public class DomainServiceBean extends GenericService implements DomainService {

	@Override
	public Usuario getUsuarioByLogin(String login) {
		
		if( StringUtils.isBlank(login) )
			return null;
		
		Usuario result = em.createNamedQuery("Usuario.findByLogin", Usuario.class)
							.setParameter("login", login)
							.getSingleResult();
		return result;
	}

	@Override
	public List<PapelUsuario> findPapeisByCentroDeCusto(Long idCentroDeCusto) {
		
		return em.createNamedQuery("PapelUsuario.findByCentroDeCusto", PapelUsuario.class)
					.setParameter("idCentroDeCusto", idCentroDeCusto)
					.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findUsuarioByNomePapel(java.lang.String)
	 */
	@Override
	public List<Usuario> listUsuarioByNomePapel(String nomePapel) {
		return em.createNamedQuery(Usuario.FIND_BY_NOME_PAPEL, Usuario.class)
				.setParameter("nome", nomePapel)
				.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#listPapelReferences(java.util.List)
	 */
	@Override
	public List<PapelUsuario> listPapelReferences(List<Papel> papeis) {
		List<String> list = new LinkedList<>();
		for (Papel p : papeis) {
			list.add(p.getNome());
		}
		if (!list.isEmpty()) {
			return em.createNamedQuery(PapelUsuario.LIST_REFERENCES_BY_PAPEIS, PapelUsuario.class)
					.setParameter("papeis", list)
					.getResultList();
		} else {
			return new LinkedList<>();
		}
		
	}
	
	@Override
	public ValorComprometido findValorComprometidoByFiltro(String centroCusto, String tipoDespesa, String acao, Long mes) {
		if (centroCusto == null || tipoDespesa == null || acao == null || mes == null) {
			return null;
		}
		
		try {
			return em.createNamedQuery(ValorComprometido.FIND_BY_FILTRO, ValorComprometido.class)
						.setParameter("centroCusto", centroCusto.toLowerCase())
						.setParameter("tipoDespesa", tipoDespesa.toLowerCase())
						.setParameter("acao", acao.toLowerCase())
						.setParameter("mes", Integer.valueOf(mes+""))
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findSolicitacaoPagamentoByFiltro(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SolicitacaoPagamento findSolicitacaoByNumeroNota(String numeroNotaFiscal) {
		try {
			return em.createNamedQuery(SolicitacaoPagamento.FIND_BY_NUMERO_NOTA, SolicitacaoPagamento.class)
						.setParameter("numeroNotaFiscal", numeroNotaFiscal.toLowerCase())
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#listSolicitacaoByFiltro(java.lang.String, com.dupont.budget.model.TipoSolicitacao, com.dupont.budget.model.StatusPagamento)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SolicitacaoPagamento> listSolicitacaoByFiltro(String numeroNotaFiscal, TipoSolicitacao tipo, StatusPagamento status, String fornecedor,Date de, Date ate) {
		
		StringBuilder q = new StringBuilder("select o from SolicitacaoPagamento o ");
		Map<String, Object> params = new HashMap<>();
		
		if (numeroNotaFiscal != null && !numeroNotaFiscal.isEmpty()) {
			params.put("numeroNotaFiscal", numeroNotaFiscal.toLowerCase());
		}
		
		if (fornecedor != null && !fornecedor.isEmpty()) {
			params.put("fornecedor.nome", fornecedor);
		}
		
		if (status != null) {
			params.put("status", status);
		}
		
		boolean con = false;
		if (!params.isEmpty()) {
			q.append("where ");
			for (Entry<String, Object> e: params.entrySet()) {
				if (con) {
					q.append(" and ");
				}
				String n = e.getKey();
				if (n.contains(".")) {
					n = n.substring(0, n.lastIndexOf("."));
				}
				q.append(String.format("%s %s :%s", (e.getValue() instanceof String ? "lower(o.".concat(e.getKey()).concat(")") : "o.".concat(e.getKey())), e.getValue() instanceof String ? "like" : "=", n));
				con = true;
			}
		}
		
		if(de !=null && ate !=null)
		{
			params.put("de", de);
			params.put("ate", ate);
			
			if (con) {
				q.append(" and ");
			}
			else
			{
				q.append(" where ");
			}
			q.append("o.criacao>=:de ");
			q.append("and  o.criacao<=:ate " );
		}
		Query query = em.createQuery(q.toString());
		
		for (Entry<String, Object> e: params.entrySet()) {
			String n = e.getKey();
			if (n.contains(".")) {
				n = n.substring(0, n.lastIndexOf("."));
			}
			query.setParameter(n, e.getValue() instanceof String ? "%".concat(e.getValue().toString().toLowerCase()).concat("%") : e.getValue());
		}
		q.append("order by o.dataPagamentoRealizado" );
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return new LinkedList<SolicitacaoPagamento>();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.DomainService#findCentroCustoByCodigo(java.lang.String)
	 */
	@Override
	public CentroCusto findCentroCustoByCodigo(String codigo) {
		try {
			return em.createNamedQuery(CentroCusto.FIND_BY_CODIGO, CentroCusto.class)
						.setParameter("codigo", codigo)
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public <T extends NamedAbstractEntity<?>> List<T> findByNamedQuery(String namedQuery, Class<T> clazz) {

		if( namedQuery == null)
			return null;		

		return em.createNamedQuery(namedQuery, clazz).getResultList();
	}

	@Override
	public Usuario createUsuario(Usuario usuario) {
		
		Set<PapelUsuario> papeis = usuario.getPapeis();
		
		if( papeis != null ){
			for (PapelUsuario papelUsuario : papeis) {
				
				Papel p = papelUsuario.getPapel();
			
				papelUsuario.setPapel(findById(p));
				
			}
		}
		
		
		return create(usuario);
	}

	@Override
	public List<Acao> findAcaoByBudget(Long budgetId) {
		try {
			return em.createNamedQuery(Acao.FIND_ACAO_BY_BUDGET, Acao.class)
						.setParameter("budgetId", budgetId)
						.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Acao> findAcaoByForecastOrBudget(Long budgetId,Long forecastId){
		List<Acao> acao =null;
		try
		{
			acao = em.createNamedQuery(Acao.FIND_ACAO_BY_BUDGET_OR_FORECAST, Acao.class)
			.setParameter("budgetId", budgetId)
			.setParameter("forecastId", forecastId)
			.getResultList();
		} catch (NoResultException e) { 
			return null;
		}
		return acao;
	}

	
	public Acao findAcaoByForecastOrBudget(Long budgetId,Long forecastId, String nomeAcao){
		Acao acao =null;
		try
		{
			acao = em.createNamedQuery(Acao.FIND_ACAO_BY_BUDGET_OR_FORECAST_AND_NOMEACAO, Acao.class)
			.setParameter("budgetId", budgetId)
			.setParameter("forecastId", forecastId)
			.setParameter("nomeAcao",nomeAcao)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		return acao;
	}

	@Override
	public void insertAcao(Acao acao) {
			em.persist(acao);
	}

	public List<TipoDespesa> findTiposDespesaForecast(Long forecastId) {
		List<TipoDespesa> tiposDespesa = em.createNamedQuery("DespesaForecast.obterDespesaTipoDespesa", TipoDespesa.class)
				.setParameter("forecastId", forecastId)
				.getResultList();
		return tiposDespesa;
	}

	public List<Acao> findAcaoDespesaForecastByTipo(Long forecastId,Long tipoDespesaId) {
		List<Acao> acoes = em.createNamedQuery("DespesaForecast.obterAcoesDespesaPorTipoDespesa", Acao.class)
				.setParameter("forecastId", forecastId)
				.setParameter("tipoDespesaId", tipoDespesaId)
				.getResultList();
		return acoes;
	}

	@Override
	public String obterEmailsUsuarios() {
		List<String> emails = em.createNamedQuery("Usuario.findEmails",String.class).getResultList();
		StringBuilder emailString=new StringBuilder();
		for(String email : emails)
		{
			emailString.append(email);
			emailString.append(",");
		}
		return emailString.toString().substring(0,emailString.toString().length()-1);
	}
	
	public SolicitacaoPagamento findSolicitacaoByNumeroNotaEFornecedor(String numeroNotaFiscal , Long idFornecedor){
		SolicitacaoPagamento solicitacao = null;
		try
		{
			// valida solicitacao pagamento com numero da nota fiscal igual
			solicitacao	= em.createNamedQuery(SolicitacaoPagamento.FIND_BY_NUMERO_NOTA_FORNECEDOR, SolicitacaoPagamento.class)
												.setParameter("numeroNotaFiscal", numeroNotaFiscal)
												.setParameter("fornecedor", idFornecedor)
												.getSingleResult();
			return solicitacao;
		}
		catch(NoResultException e){}
		
		return null;
	}
		
}
