package com.dupont.budget.web.util;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.dupont.budget.model.NamedAbstractEntity;

/**
 * Classe utilitária para adicionar mensagens no contexto JSF.
 *
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Named
@ApplicationScoped
public class FacesUtils implements Serializable{

	private static final long serialVersionUID = 374768315446835217L;

	/**
	 * Adiciona uma mensagem de erro no contexto JSF.
	 * @param message mensagem a ser adicionada
	 */
	public void addErrorMessage(String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", message));
	}

	/**
	 * Adiciona uma mensagem de informação no contexto JSF.
	 * @param message mensagem a ser adicionada
	 */
	public void addInfoMessage(String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", message));
	}
	
	/**
	 * Adiciona uma mensagem de aviso no contexto JSF.
	 * @param message mensagem a ser adicionada
	 */
	public void addWarnMessage(String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", message));
	}


	/**
	 * Retorna o usuario logado
	 * @return Usuario logado
	 */

	public String getUserLogin()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	}

	public <T extends NamedAbstractEntity<Long>> List<T> autoComplete(List<T> lista,String input)
	{
		List<T> result =  new ArrayList<T>();
		for(T t : lista)
		{
			if(t.getNome().toUpperCase().contains(input.toUpperCase()))
			{
				result.add(t);
			}
		}
		return result;
	}

	public String formatarDinheiro(Double value){
		Locale locale = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(locale).format(value);
	}
}
