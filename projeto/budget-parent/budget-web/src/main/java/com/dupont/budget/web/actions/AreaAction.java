package com.dupont.budget.web.actions;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.model.Area;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;
/**
 * Controller das telas de manutenção da entidade produto
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
@RolesAllowed(value = "ADMINISTRADOR")
public class AreaAction extends GenericAction<Area> {

	private static final long serialVersionUID = -9064126463852854590L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;
	
	private Usuario lider;

	@Named
	@Produces
	public Area getArea() {
		return getEntidade();
	}
	
	@Override
	public String persist() {
		String result = null;
		if (mustCreate()) {
			result = create();
		} else {
			result = update();
		}
		
		String nomePapel = createNomePapel(entidade);
		Papel papel = new Papel(nomePapel.toString());
		List<Papel> papeis = service.findByName(papel);
		PapelUsuario pu = new PapelUsuario(lider, entidade);
		boolean ignore = false;
		
		if (papeis.isEmpty()) {
			pu.setPapel(papel);
		} else {
			Usuario usuario = service.getUsuarioByLogin(lider.getLogin());
			for (PapelUsuario p: usuario.getPapeis()) {
				if (p.getPapel().equals(papel)) {
					ignore = true;
					break;
				}
			}
			pu.setPapel(papeis.get(0));
		}
		
		if (!ignore) {
			service.create(pu);
		}
		
		clearInstance();
		
		return result;
	}

	private String createNomePapel(Area area) {
		StringBuilder nomePapel = new StringBuilder("LIDER_");
		String nomeArea = area.getNome();
		nomeArea = nomeArea.trim().replaceAll(" ", "_");
		nomePapel.append(nomeArea.toUpperCase());
		return nomePapel.toString();
	}
	
	public String edit(Area t) {
		this.setEntidade(t);
		
		List<Usuario> list = service.listUsuarioByNomePapel(createNomePapel(t));
		if (!list.isEmpty()) {
			lider = list.get(0);
		}

		return "edit";
	}

	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void find() {
		list = service.findByName(entidade);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected DomainService getService() {
		return service;
	}

	@Override
	protected FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public Usuario getLider() {
		return lider;
	}

	public void setLider(Usuario lider) {
		this.lider = lider;
	}
}
	