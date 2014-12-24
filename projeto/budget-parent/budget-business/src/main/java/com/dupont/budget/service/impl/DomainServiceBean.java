package com.dupont.budget.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
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
		
		return em.createNamedQuery(ValorComprometido.FIND_BY_FILTRO, ValorComprometido.class)
					.setParameter("centroCusto", centroCusto)
					.setParameter("tipoDespesa", tipoDespesa)
					.setParameter("acao", acao)
					.setParameter("mes", mes)
					.getSingleResult();
	}
}
