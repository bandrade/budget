package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;

import com.dupont.budget.exception.ApplicationException;
import com.dupont.budget.exception.ApplicationRuntimeException;
import com.dupont.budget.exception.ExistingNameRuntimeException;
import com.dupont.budget.model.AbstractEntity;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Ação genérica para auxílio do controle da persitência na camada de visualiação 
 *  
 * @author joel
 *
 * @param <T> tipo da entidade, ela deve estender {@link AbstractEntity} 
 */
public abstract class GenericAction<T extends AbstractEntity<?>>  implements Serializable {
	
	private static final long serialVersionUID = 4222420252345862886L;

	protected T entidade;
	
	protected List<T> list;
	
	protected Class<T> clazz;
	
	private static final int ITERATION_LIMIT = 1000;
	
	/**
	 * Atualiza ou persite a entidade no meio persistente.
	 * 
	 * @return a acao que deve ser executada.
	 */
	public String persist() {
		String action = null;
		try {
			if (getEntidade().getId() == null) {
				action = create();
			} else {
				action = update();
			}
			clearInstance();
		} catch (ExistingNameRuntimeException e) {
			getFacesUtils().addErrorMessage("Registro com mesmo nome já cadastrado!");
		}
		return action;
	}
	
	/**
	 * Ação de se criar a entidade cultura.
	 */
	public String create() {
		String tipo = getEntidade().getClass().getSimpleName();
		getLogger().debug(String.format("Criando uma nova entidade do tipo: %s", tipo));
		
		try {
			setEntidade(getService().create(getEntidade()));
			
			getFacesUtils().addInfoMessage(String.format("%s criado(a) com sucesso.", tipo));
		} catch (Exception e) {
			handleException(e);
		}

		return "list";
	}
	
	/**
	 * Remove a entidade
	 * 
	 * @param t
	 *            entidade a ser removida.
	 */
	public void delete(T t) {
		String tipo = getEntidade().getClass().getSimpleName();
		getLogger().debug(String.format("Removendo entidade do tipo %s, com o id: ", tipo, t.getId()));

		try {
			// Remove a entidade do meio persistente
			getService().delete(t);
			
			// Remove a entidade da tabela
			list.remove(t);
			
			getFacesUtils().addInfoMessage(String.format("%s removido(a) com sucesso.", tipo));
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Atualiza a entidade.
	 */
	public String update() {
		String tipo = getEntidade().getClass().getSimpleName();
		getLogger().debug("Atualizando a entidade cultura com o id: "
				+ getEntidade().getId());
		try {
			setEntidade(getService().update(getEntidade()));
			
			getFacesUtils().addInfoMessage(String.format("%s atualizado(a) com sucesso.", tipo));
		} catch (Exception e) {
			handleException(e);
		}


		return "list";
	}
	
	/**
	 * Abre a pagina de edição de cultura
	 * 
	 * @param cultura
	 *            entidade a ser atualizada.
	 */
	public String edit(T t) {
		this.setEntidade(t);

		return "edit";
	}
	
	/**
	 * Trata as exceções lançadas durante os processos.
	 * 
	 * @param e exceção
	 */
	private void handleException(Throwable e) {
		if (e instanceof ApplicationException || e instanceof ApplicationRuntimeException) {
			getFacesUtils().addErrorMessage(e.getLocalizedMessage());
		} else {
			Throwable t = e.getCause();
			int counter = 0;
			while (t.getCause() != null && counter++ < ITERATION_LIMIT) {
				t = t.getCause();
			}
			getFacesUtils().addErrorMessage(String.format("Erro inesperado: %s", t.getLocalizedMessage()));
		}
	}
	
	/**
	 * Reinicia a entidade criando uma nova instância.
	 */
	private void clearInstance() {
		try {
			
			setEntidade((T) getClazz().newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			getLogger().error(String.format("Não foi possível criar uma nova instância do tipo: %s.", getEntidade().getClass().getSimpleName()));
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Class<T> getClazz() {
		if (clazz == null) {
			ParameterizedType superClass = ((java.lang.reflect.ParameterizedType) this
					.getClass().getGenericSuperclass());
			clazz = (Class<T>) superClass.getActualTypeArguments()[0];
		}
		return clazz;
	}
	
	public T getEntidade() {
		if (entidade == null) {
			clearInstance();
		}
		return entidade;
	}

	public void setEntidade(T entidade) {
		this.entidade = entidade;
	}

	public List<T> getList() {
		if (list == null) {
			list = (List<T>) getService().findAll(getClazz());
		}
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	/**
	 * Template method para retorno da instância do logger.
	 * @return o logger da aplicação
	 */
	protected abstract Logger getLogger();
	
	/**
	 * Template method para retorno da instância do serviço de persistência
	 * @return o servico de persistência
	 */
	protected abstract DomainService getService();
	
	/**
	 * Template method para retorno do utilitário do JSF.
	 * @return a classe de utilidades do JSF.
	 */
	protected abstract FacesUtils getFacesUtils();
	
	/**
	 * Buscar as entidades a partir do filtro.
	 */
	public abstract void find();

}
