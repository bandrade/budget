package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.NamedAbstractEntity;
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
	
	/**
	 * Retorna todas as entidades do tipo da classe epecificado do meio 
	 * persistente.
	 * 
	 * @param t o tipo da classe
	 * @return lista com as todas as entidade criadas.
	 */
	<T extends AbstractEntity<?>> List<T> findAll(Class<T> t);

	/**
	 * Cria a entidade especificada no meio persistente
	 * 
	 * @param t entidade a ser criada
	 * @return entidade criada
	 */
	<T extends AbstractEntity<?>> T create(T t);

	/**
	 * Retorna todas as entidades que atendam ao filtro
	 * @param nome String inicial do nome da cultura.
	 * @return lista com as entidades encontradas.
	 */
	<T extends NamedAbstractEntity<?>>List<T> findByName(T t);
	
	/**
	 * Remove a entidade do meio persistente.
	 * @param t entidade a ser removida.
	 */
	<T extends AbstractEntity<?>> void delete(T t);

	/**
	 * Atualiza a entidade passada.
	 * @param t entidade a ser atualizada.
	 * @return entidade atualizada
	 */
	<T extends AbstractEntity<?>> T update(T t);
	
	
}
