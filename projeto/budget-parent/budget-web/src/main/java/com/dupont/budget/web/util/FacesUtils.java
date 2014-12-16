package com.dupont.budget.web.util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Classe utilitária para adicionar mensagens no contexto JSF.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public class FacesUtils implements Serializable{

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
	 * Retorna o usuario logado
	 * @return Usuario logado
	 */
	
	public String getUserLogin()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	}
	
}
