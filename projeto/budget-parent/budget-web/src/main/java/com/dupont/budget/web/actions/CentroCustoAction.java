package com.dupont.budget.web.actions;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.TipoCentroCusto;
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
public class CentroCustoAction extends GenericAction<CentroCusto> {

	private static final long serialVersionUID = -9064126463852854590L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;
	
	private Usuario responsavel;
	
	private Usuario gestor;
	
	private Usuario gerente;
	
	@Override
	public String persist() {
		String result = null;
		if (mustCreate()) {
			result = create();
		} else {
			result = update();
		}
		
		criarPapel(responsavel);
		
		if (gestor != null) {
			criarPapel(gestor);
		}
		
		if (gerente != null) {
			criarPapel(gerente);
		}
		
		clearInstance();
		
		return result;
	}
	
	private void criarPapel(Usuario usuario) {
		String nomePapel = createNomePapel(entidade);
		Papel papel = new Papel(nomePapel.toString());
		List<Papel> papeis = service.findByName(papel);
		PapelUsuario pu = new PapelUsuario();
		boolean ignore = false;
		
		if (papeis.isEmpty()) {
			pu.setPapel(papel);
		} else {
			Usuario usr = service.getUsuarioByLogin(usuario.getLogin());
			for (PapelUsuario p: usr.getPapeis()) {
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
	}
	
	private String createNomePapel(CentroCusto cc) {
		StringBuilder nomePapel = new StringBuilder("LIDER_");
		String nomeArea = cc.getNome();
		nomeArea = nomeArea.trim().replaceAll(" ", "_");
		nomePapel.append(nomeArea.toUpperCase());
		return nomePapel.toString();
	}

	@Named
	@Produces
	public CentroCusto getCentroCusto() {
		return getEntidade();
	}
	
	@Named
	@Produces
	public List<Area> getAreaList() {
		return service.findAll(Area.class);
	}
	
	@Named
	@Produces
	public TipoCentroCusto[] getTpoCentroCustoList() {
		return TipoCentroCusto.values();
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

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public Usuario getGestor() {
		return gestor;
	}

	public void setGestor(Usuario gestor) {
		this.gestor = gestor;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
	}
}
	