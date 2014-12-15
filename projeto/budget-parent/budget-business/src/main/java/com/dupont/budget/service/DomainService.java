package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Usuario;

/**
 * Serviço que controla todas as classes de domínio.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public interface DomainService {

	/**
	 * Cria a entidade cultura no meio persistente
	 * @param cultura entidade a ser criada
	 * @return entidade criada
	 * @throws DuplicateEntityException se uma entidade com mesmo nome ja existe
	 */
	public Cultura createCultura(Cultura cultura) throws DuplicateEntityException;
	
	/**
	 * Retorna todas as entidades culturas do meio persistente
	 * @return lista com todas as entidades criadas
	 */
	public List<Cultura> findAllCulturas();
	
	/**
	 * Retorna todas as entidades culturas que atendam o filtrol
	 * @param nome String inicial do nome da cultura.
	 * @return lista com as culturas encontradas.
	 */
	public List<Cultura> findCulturasByNome(String nome);
	
	/**
	 * Remove a cultura do meio persistente.
	 * @param cultura entidade cultura.
	 */
	public void deleteCultura(Cultura cultura);
	
	/**
	 * Atualiza a entidade cultura.
	 * @param cultura entidade a ser atualizada.
	 * @return entidade atualizada
	 */
	public Cultura updateCultura(Cultura cultura);
	
	/**
	 * Retorna um usuario pelo login.
	 * @param login login do usuario
	 * @return usuario encontrado ou null
	 */
	public Usuario getUsuarioByLogin(String login);
	
}
