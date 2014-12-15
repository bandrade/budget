package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;

/**
 * Action que representa o usuario logado na aplicação
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@SessionScoped
public class LoggedUserAction implements Serializable {

	private static final long serialVersionUID = 5801127042696837189L;
	
	@Inject
	private DomainService service;

	private Usuario loggedUser;
	
	@Produces @Named
	public String getLoggedUsername(){
		
		if( loggedUser == null) 
			loggedUser = service.getUsuarioByLogin(getUser());
		
		return loggedUser.getNome();
	}

	/**
	 * Retorna o usuario logado
	 * @return
	 */
	private String getUser() {
		return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	}
}
