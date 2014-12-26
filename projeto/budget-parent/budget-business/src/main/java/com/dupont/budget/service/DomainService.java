package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.model.NamedAbstractEntity;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.model.ValorComprometido;

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
	 * Retorna todas as entidades filtrando por id
	 * @param id
	 * @return entidade
	 */
    <T extends AbstractEntity<?>> T findById(T t);
    
    /**
     * Retorna todas as entidades conforme a entidade passada como exemplo.
     * Esse método ainda não trata coleções e itera apenas no primeiro nível 
     * da hierarquia.
     * 
     * @param t a entidade preenchida com os campos especificados
     * @return uma lista com os objetos que atendem as restrições
     */
    <T extends AbstractEntity<?>> List<T> findByExample(T t);
	
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
     * Retorna todas as referencias de uma lista de papeis específicos.
     * 
     * @param papeis a lista de papéis
     * 
     * @return todas as referências
     */
	List<PapelUsuario> listPapelReferences(List<Papel> papeis);

	/**
	 * Retorna um valor comprometido com base na UK definida na entidade.
	 * 
	 * WARN: por se tratar de string o método pode não achar a entidade correta,
	 * é necessário que certifique-se de que os nomes estejam corretos.
	 * 
	 * @param centroCusto nome do centro de custo
	 * @param tipoDespesa nome do tipo de despesa
	 * @param acao nome da acao
	 * @param mes mes de referência
	 * @return o valor comprometido ou <code>null</code> caso não encontre
	 */
	ValorComprometido findValorComprometidoByFiltro(String centroCusto,
			String tipoDespesa, String acao, Integer mes);
	
	/**
	 * Retorna uma solicitação de pagamento com base nos parametros 
	 * especificados como filtro.
	 * 
	 * @param numeroNotaFiscal numero da nota fiscal
	 * @param fornecedor nome do fornecedor 
	 * @param codigoCentroCusto codigo do centro de custo
	 * 
	 * @return a solicitação de pagamento ou <code>null</code> caso não encontre
	 */
	SolicitacaoPagamento findSolicitacaoPagamentoByFiltro(String numeroNotaFiscal,
			String fornecedor, String codigoCentroCusto);
}
