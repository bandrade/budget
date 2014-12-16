package com.dupont.budget.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Usuario;
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
	public Cultura createCultura(Cultura cultura) throws DuplicateEntityException {
		
		// Verifica se existe uma cultura com o mesmo nome. Caso exista, lançar uma exceção.
		List<Cultura> exists =  em.createNamedQuery("Cultura.findByName", Cultura.class)
									.setParameter("nome", cultura.getNome().trim())
									.getResultList();
		
		if( ! exists.isEmpty() )
			throw new DuplicateEntityException(cultura);
				
		em.persist(cultura);
		
		return cultura;
	}

	@Override
	public List<Cultura> findAllCulturas() {		
		
		return em.createNamedQuery("Cultura.findAll", Cultura.class).getResultList();
	}

	@Override
	public List<Cultura> findCulturasByNome(String nome) {
		
		// Se não preencheu o nome, retornar todas as entidades
		if( StringUtils.isBlank(nome) )
			return findAllCulturas();

		List<Cultura> result =  em.createNamedQuery("Cultura.findByName", Cultura.class)
									.setParameter("nome", nome.trim() + "%")
									.getResultList();
		
		return result;
	}

	@Override
	public void deleteCultura(Cultura cultura) {	
		cultura = em.merge(cultura); // attaching the entity again
		em.remove(cultura);
	}

	@Override
	public Cultura updateCultura(Cultura cultura) {

		return em.merge(cultura);
	}

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
		
		return 
				 em.createNamedQuery("PapelUsuario.findByCentroDeCusto", PapelUsuario.class)
					.setParameter("idCentroDeCusto", idCentroDeCusto)
					.getResultList();
	}
	
}
