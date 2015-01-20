package com.dupont.budget.web.actions;

import java.util.Calendar;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.slf4j.Logger;

import com.dupont.budget.bpm.custom.user.UserGroupCallbackCacheManager;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.web.util.FacesUtils;

import java.util.List;
/**
 * Controller das telas de manutenção da entidade produto
 *
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@ViewAccessScoped @Named
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
	
	private Usuario responsavelNotas;

	@Inject
    private BPMSProcessService bpms;

	@Inject
	private UserGroupCallbackCacheManager userCallBackCache;

	@Named
	@Produces
	public Area getArea() {
		return getEntidade();
	}

	@Override
	public String persist() {

		String result = null;

		if (mustCreate()) {
			PapelUsuario papelLider =new PapelUsuario(new Papel(createNomePapel(entidade)), lider, entidade);
			PapelUsuario papelResponsavelNotas =  new PapelUsuario(new Papel(createNomePapelNotas(entidade)), responsavelNotas, entidade);
			entidade.getPapeis().add(papelLider);
			entidade.getPapeis().add(papelResponsavelNotas);
			result = create();
			try {
				if (bpms.existeProcessoAtivo(Calendar.getInstance().get(Calendar.YEAR)+"")) {
					facesUtils.addInfoMessage("Area nao fara parte do processo de budget do ano "+Calendar.getInstance().get(Calendar.YEAR));
				}
			} catch(Exception e) {
				facesUtils.addErrorMessage("Erro inserir a Area");
				logger.error("Erro ao inserir a area",e);
			}
			
		} else {
			Area tmp = service.findById(entidade);
			entidade.setLider(tmp.getLider());
			entidade.getLider().setUsuario(lider);
			if(tmp.getResponsavelNotas() !=null)
			{
				entidade.setResponsavelNotas(tmp.getResponsavelNotas()) ;
				userCallBackCache.removeGroupsFromCache(tmp.getResponsavelNotas().getUsuario().getLogin());
			}
			else
			{
				Papel papel = new Papel(createNomePapelNotas(entidade));
				List<Papel> papeis = service.findByName(papel);
				if(papeis !=null && papeis.size()>0)
				{
					papel = papeis.get(0);
				}
				else
				{
					service.create(papel);
				}
				entidade.setResponsavelNotas(new PapelUsuario(papel, responsavelNotas, entidade) ) ;
			}
			entidade.getResponsavelNotas().setUsuario(responsavelNotas);
			
			userCallBackCache.removeGroupsFromCache(tmp.getLider().getUsuario().getLogin());
			
			result = update();
		}
		for(PapelUsuario papelUsuario : entidade.getPapeis())
		{
			userCallBackCache.removeGroupsFromCache(papelUsuario.getUsuario().getLogin());
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

	private String createNomePapelNotas(Area area) {
		StringBuilder nomePapel = new StringBuilder("NOTAS_");
		String nomeArea = area.getNome();
		nomeArea = nomeArea.trim().replaceAll(" ", "_");
		nomePapel.append(nomeArea.toUpperCase());
		return nomePapel.toString();
	}


	@Override
	public String update() {
		userCallBackCache.removeGroupsFromCache(entidade.getLider().getUsuario().getLogin());
		if(entidade.getResponsavelNotas() !=null)
		{
			userCallBackCache.removeGroupsFromCache(entidade.getResponsavelNotas().getUsuario().getLogin());
		}
		return super.update();
	}

	@Override
	public String edit(Area t) {
		this.setEntidade(t);

		if (t.getLider() != null) {
			lider = t.getLider().getUsuario();
		}

	    if (t.getResponsavelNotas() != null) {
			responsavelNotas = t.getResponsavelNotas().getUsuario();
		}
		return "edit";
	}
	
	@Override
	public String newPage() {
		setLider(new Usuario());
		setResponsavelNotas(new Usuario());
		return super.newPage();
	}
	@Override
	public void delete(Area t) {
		try {
			if (bpms.existeProcessoAtivo(Calendar.getInstance().get(Calendar.YEAR)+"")) {
				facesUtils.addErrorMessage("Não é possível remover uma Área enquanto haja um processo de budget ativo");
			} else {
				userCallBackCache.removeGroupsFromCache(t.getLider().getUsuario().getLogin());
				super.delete(t);
			}
		} catch(Exception e) {
			facesUtils.addErrorMessage("Erro ao remover a Area");
			logger.error("Erro ao remover a area",e);
		}
	}

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

	public Usuario getResponsavelNotas() {
		return responsavelNotas;
	}

	public void setResponsavelNotas(Usuario responsavelNotas) {
		this.responsavelNotas = responsavelNotas;
	}
	
}
