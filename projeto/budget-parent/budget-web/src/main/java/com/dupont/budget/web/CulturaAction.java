package com.dupont.budget.web;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.Cultura;
import com.dupont.budget.service.DomainService;

/**
 * Controller das telas de manutenção da entidade cultura
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Model
public class CulturaAction {
	
	@Inject
	private DomainService service;

	private Cultura cultura = new Cultura();
	
	private List<Cultura> culturas;
	
	@Produces @Named
	public Cultura getCultura() {
		return cultura;
	}
		
	public List<Cultura> getCulturas(){
		
		if(culturas == null)
			culturas = service.findAllCulturas();
		
		return culturas;
	}
	
	/**
	 * Ação de se criar a entidade cultura.
	 */
	public String createCultura(){
		
		try {
			cultura = service.createCultura(cultura);
		} catch (DuplicateEntityException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Entidade Cultura com o mesmo já já cadastrada."));
			
			return null;
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Entidade Cultura criada com sucesso."));
		
		return "list";
	}
	
	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void findCulturas(){	
		culturas = service.findCulturasByNome(cultura.getNome());
	}
	
	/**
	 * Remove a cultura
	 * @param cultura cultura a ser removida.
	 */
	public void deleteCultura(Cultura cultura) {
		
		service.deleteCultura(cultura);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Entidade Cultura removida com sucesso."));
	}
	
	/**
	 * Atualiza a entidade cultura. 
	 * @return
	 */
	public String updateCultura(){
		service.updateCultura(cultura);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Entidade Cultura atualizada com sucesso."));
		
		return "list";
	}
}
