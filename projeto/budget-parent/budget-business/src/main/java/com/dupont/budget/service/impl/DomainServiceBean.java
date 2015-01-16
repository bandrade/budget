package com.dupont.budget.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.NamedAbstractEntity;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
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
	public ValorComprometido findValorComprometidoByFiltro(String centroCusto, String tipoDespesa, String acao, Integer mes) {
		if (centroCusto == null || tipoDespesa == null || acao == null || mes == null) {
			return null;
		}
		
		try {
			return em.createNamedQuery(ValorComprometido.FIND_BY_FILTRO, ValorComprometido.class)
						.setParameter("centroCusto", centroCusto.toLowerCase())
						.setParameter("tipoDespesa", tipoDespesa.toLowerCase())
						.setParameter("acao", acao.toLowerCase())
						.setParameter("mes", mes)
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
	public List<SolicitacaoPagamento> listSolicitacaoByFiltro(String numeroNotaFiscal, TipoSolicitacao tipo, StatusPagamento status, String fornecedor) {
		
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
		
		if (tipo != null) {
			params.put("tipoSolicitacao", tipo);
		}
		
		if (!params.isEmpty()) {
			q.append("where ");
			boolean con = false;
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
		
		Query query = em.createQuery(q.toString());
		
		for (Entry<String, Object> e: params.entrySet()) {
			String n = e.getKey();
			if (n.contains(".")) {
				n = n.substring(0, n.lastIndexOf("."));
			}
			query.setParameter(n, e.getValue() instanceof String ? "%".concat(e.getValue().toString().toLowerCase()).concat("%") : e.getValue());
		}
		
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
}
