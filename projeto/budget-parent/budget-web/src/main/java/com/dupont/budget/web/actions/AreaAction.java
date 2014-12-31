package com.dupont.budget.web.actions;

import java.util.Calendar;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.bpm.custom.user.UserGroupCallbackCacheManager;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.bpms.BPMSProcessService;
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
	protected void clearInstance() {
		// TODO Auto-generated method stub
		super.clearInstance();
	}

	@Override
	public String persist() {

		String result = null;

		if (mustCreate()) {
			entidade.setLider(new PapelUsuario(new Papel(createNomePapel(entidade)), lider, entidade));
			result = create();
			try
			{
				if(bpms.existeProcessoAtivo(Calendar.getInstance().get(Calendar.YEAR)+""))
				{
					facesUtils.addInfoMessage("Area nao fara parte do processo de budget do ano "+Calendar.getInstance().get(Calendar.YEAR));
				}
			}
			catch(Exception e)
			{
				facesUtils.addErrorMessage("Erro inserir a Area");
				logger.error("Erro ao inserir a area",e);
			}
		} else {
			Area tmp = service.findById(entidade);
			entidade.setLider(tmp.getLider());
			entidade.getLider().setUsuario(lider);
			userCallBackCache.removeGroupsFromCache(lider.getLogin());
			result = update();
		}

		userCallBackCache.removeGroupsFromCache(entidade.getLider().getUsuario().getLogin());
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


	@Override
	public String update() {
		userCallBackCache.removeGroupsFromCache(entidade.getLider().getUsuario().getLogin());
		return super.update();
	}
	public String edit(Area t) {
		this.setEntidade(t);

		if (t.getLider() != null) {
			lider = t.getLider().getUsuario();
		}

		return "edit";
	}

	@Override
	public void delete(Area t) {

		try
		{
			if(bpms.existeProcessoAtivo(Calendar.getInstance().get(Calendar.YEAR)+""))
			{
				facesUtils.addErrorMessage("Não é possível remover uma Área enquanto haja um processo de budget ativo");

			}
			else
			{
				userCallBackCache.removeGroupsFromCache(entidade.getLider().getUsuario().getLogin());
				super.delete(t);
			}

		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao remover a Area");
			logger.error("Erro ao remover a area",e);
		}
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
