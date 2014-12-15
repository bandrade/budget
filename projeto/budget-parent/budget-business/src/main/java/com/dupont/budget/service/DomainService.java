package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.Cliente;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.model.Distrito;
import com.dupont.budget.model.Produto;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.model.Vendedor;

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
	 * Retorna todas as entidades produtos do meio persistente
	 * @return lista com todas as entidades criadas
	 */
	public List<Produto> findAllProdutos();
	
	
	/**
	 * Retorna todas as entidades vendedores do meio persistente
	 * @return lista com todas as entidades criadas
	 */
	public List<Vendedor> findAllVendedores();	
	
	
	/**
	 * Retorna todas as entidades distritos do meio persistente
	 * @return lista com todas as entidades criadas
	 */
	public List<Distrito> findAllDistritos();
	
	

	/**
	 * Retorna todas as entidades tipos de despesa do meio persistente
	 * @return lista com todas as entidades criadas
	 */
	public List<TipoDespesa> findAllTiposDespesa();
	
	/**
	 * Retorna todas as entidades tipos de despesa do meio persistente
	 * @return lista com todas as entidades criadas
	 */
	public List<Cliente> findAllClientes();
	
	
	
}
