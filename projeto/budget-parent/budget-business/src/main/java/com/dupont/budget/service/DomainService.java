package com.dupont.budget.service;

import java.util.List;

import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.model.Acao;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.NamedAbstractEntity;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.model.StatusPagamento;
import com.dupont.budget.model.TipoCentroCusto;
import com.dupont.budget.model.TipoDespesa;
import com.dupont.budget.model.TipoSolicitacao;
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
	
	<T extends NamedAbstractEntity<?>>List<T> findByNameEqual(T t);
	
	
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
			String tipoDespesa, String acao, Long mes);
	
	/**
	 * Retorna uma solicitação de pagamento com base no numero da nota.
	 * 
	 * @param numeroNotaFiscal numero da nota fiscal
	 * 
	 * @return a solicitação de pagamento ou <code>null</code> caso não encontre
	 */
	SolicitacaoPagamento findSolicitacaoByNumeroNota(String numeroNotaFiscal);

	/**
	 * Consulta um centro de custo através do código solicitado.
	 * 
	 * @param codigo codigo do centro de custo
	 * @return o centro de custo ou <code>null</code> caso não exista
	 */
	CentroCusto findCentroCustoByCodigo(String codigo);
	
	
	/**
	  * Retorna todas as entidades da named query
	  * @param nome nome da named query
	  * @return lista com as entidades encontradas.
	  */
	<T extends NamedAbstractEntity<?>>List<T> findByNamedQuery(String namedQuery, Class<T> clazz);

	/**
	 * Retorna uma lista de solicitações de pagamento que atende aos filtros indicados
	 * 
	 * @param numeroNotaFiscal número da nota fiscal
	 * @param tipo tipo da solicitação
	 * @param status status da solicitacao de pagamento
	 * @param fornecedor TODO
	 * @return
	 */
	List<SolicitacaoPagamento> listSolicitacaoByFiltro(String numeroNotaFiscal,
			TipoSolicitacao tipo, StatusPagamento status, String fornecedor);
	
	
	public Usuario createUsuario(Usuario usuario);
	
	public <T extends NamedAbstractEntity<?>> List<T> findByNamePaging(T t) ;
	
	public List<Acao> findAcaoByBudget(Long budgetId);
	
	public Acao findAcaoByForecastOrBudget(Long budgetId,Long forecastId, String nomeAcao);
	
	public void insertAcao(Acao acao);
	
	public List<Acao> findAcaoByForecastOrBudget(Long budgetId,Long forecastId); 
	 
	public List<TipoDespesa> findTiposDespesaForecast(Long forecastId); 
	
	public List<Acao> findAcaoDespesaForecastByTipo(Long forecastId,Long tipoDespesaId) ; 
	
	public String obterEmailsUsuarios();
	
	
}
