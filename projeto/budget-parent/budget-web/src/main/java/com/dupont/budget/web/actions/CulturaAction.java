package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.model.Cultura;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller das telas de manutenção da entidade cultura
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Named("culturaAction")
@RequestScoped
@RolesAllowed(value = "ADMINISTRADOR")
public class CulturaAction implements Serializable {

	private static final long serialVersionUID = -6061667499898003022L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	private Cultura cultura = new Cultura();

	private List<Cultura> list;

	@Named
	@Produces
	public Cultura getCultura() {
		return cultura;
	}

	public List<Cultura> getList() {
		// Pré popula a lista de culturas
		if (list == null)
			list = service.findAllCulturas();

		return list;
	}

	public String persistCultura() {

		if (cultura.getId() == null)
			return createCultura();
		else
			return updateCultura();
	}

	/**
	 * Ação de se criar a entidade cultura.
	 */
	public String createCultura() {

		logger.debug("Criando nova cultura");

		cultura = service.create(cultura);

		facesUtils.addInfoMessage("Cultura criada com sucesso.");

		// Reset o objeto cultura.
		cultura = new Cultura();

		return "list";
	}

	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void findCulturas() {
		list = service.findCulturasByNome(cultura.getNome());
	}

	/**
	 * Remove a cultura
	 * 
	 * @param cultura
	 *            cultura a ser removida.
	 */
	public void deleteCultura(Cultura cultura) {

		logger.debug("Removendo entidade cultura com o id: " + cultura.getId());

		// Remove a entidade do meio persistente
		service.deleteCultura(cultura);

		// Remove a entidade da tabela
		list.remove(cultura);

		facesUtils.addInfoMessage("Cultura removida com sucesso.");
	}

	/**
	 * Atualiza a entidade cultura.
	 */
	public String updateCultura() {

		logger.debug("Atualizando a entidade cultura com o id: "
				+ cultura.getId());

		cultura = service.updateCultura(cultura);

		facesUtils.addInfoMessage("Cultura atualizada com sucesso.");

		// Reset o objeto cultura.
		cultura = new Cultura();

		return "list";
	}

	/**
	 * Abre a pagina de edição de cultura
	 * 
	 * @param cultura
	 *            entidade a ser atualizada.
	 */
	public String editCultura(Cultura cultura) {
		this.cultura = cultura;

		return "edit";
	}

}
