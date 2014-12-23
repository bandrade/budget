package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.model.NamedAbstractEntity;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
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
	 * Retorna um usuario pelo login.
	 * @param login login do usuario
	 * @return usuario encontrado ou null
	 */
	Usuario getUsuarioByLogin(String login);
	
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
	
	/**
	 * Retorna todos os papeis relacionados a um Centro de Custo
	 * @param idCentroDeCusto codigo do cc
	 * @return papeis
	 */
    List<PapelUsuario> findPapeisByCentroDeCusto(Long idCentroDeCusto);

    /**
     * Retorna uma lista de usuarios que possuem o papel especificado
     * @param nomePapel nome do papel
     * @return a lista de usuários que atenda ao critério
     */
	List<Usuario> listUsuarioByNomePapel(String nomePapel); 
	

	/**
	 * Retorna todas as entidades filtrando por id
	 * @param id
	 * @return entidade
	 */
    <T extends AbstractEntity<?>> T findById(T t);
    
    /**
     * Retorna todas as entidades conforme a entidade passada como exemplo.
     * 
     * @param t a entidade preenchida com os campos especificados
     * @return uma lista com os objetos que atendem as restrições
     */
    <T extends AbstractEntity<?>> List<T> findByExample(T t);

    /**
     * Retorna todas as referencias de uma lista de papeis específicos.
     * 
     * @param papeis a lista de papéis
     * 
     * @return todas as referências
     */
	List<PapelUsuario> listPapelReferences(List<Papel> papeis);
	
}
