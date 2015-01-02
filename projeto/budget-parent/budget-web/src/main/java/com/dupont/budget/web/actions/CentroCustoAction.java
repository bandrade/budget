package com.dupont.budget.web.actions;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.bpm.custom.user.UserGroupCallbackCacheManager;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.TipoCentroCusto;
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
public class CentroCustoAction extends GenericAction<CentroCusto> {

	private static final long serialVersionUID = -9064126463852854590L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@Inject
    private BPMSProcessService bpms;


	private Usuario responsavel;

	private Usuario gestor;

	@Inject
	private UserGroupCallbackCacheManager userCallBackCache;



	@Override
	public String persist() {
		String result = null;

		if (mustCreate()) {
			entidade.getResponsaveis().add(new PapelUsuario(new Papel(createNomePapel(entidade, 1)), responsavel, entidade, 1));
			entidade.getResponsaveis().add(new PapelUsuario(new Papel(createNomePapel(entidade, 2)), gestor, entidade, 2));

			result = create();
			try {
				if (bpms.existeProcessoAtivo(Calendar.getInstance().get(Calendar.YEAR) + "")) {
					facesUtils.addInfoMessage("Centro de Custo nao fara parte do processo de budget do ano "+Calendar.getInstance().get(Calendar.YEAR));
				}
			} catch(Exception e) {
				facesUtils.addErrorMessage("Erro ao consultar o processo BPM");
				logger.error("Erro ao consultar o processo BPM",e);
			}
		} else {
			CentroCusto tmp = service.findById(entidade);
			List<PapelUsuario> list = tmp.getResponsaveis();

			for (Iterator<PapelUsuario> i = list.iterator(); i.hasNext();) {
				PapelUsuario o = i.next();
				userCallBackCache.removeGroupsFromCache(o.getUsuario().getLogin());
				switch (o.getNivel()) {
				case 1:
					o.setUsuario(responsavel);
					break;
				case 2:
					if (gestor != null) {
						o.setUsuario(gestor);
					} else {
						i.remove();
					}
					break;
				}
			}
			entidade.setResponsaveis(list);
			entidade.setCodigo(tmp.getCodigo());

			result = update();
		}

		userCallBackCache.removeGroupsFromCache(responsavel.getLogin());
		userCallBackCache.removeGroupsFromCache(gestor.getLogin());
		clearInstance();
				return result;
	}

	@Override
	public String edit(CentroCusto t) {
		this.setEntidade(t);

		CentroCusto cc = service.findById(t);

		for (PapelUsuario pu: cc.getResponsaveis()) {
			switch (pu.getNivel()) {
			case 1:
				responsavel = pu.getUsuario();
				break;
			case 2:
				gestor = pu.getUsuario();
				break;
			}
		}

		return "edit";
	}

	@Override
	public String update() {
		userCallBackCache.removeGroupsFromCache(responsavel.getLogin());
		userCallBackCache.removeGroupsFromCache(gestor.getLogin());
		return super.update();
	}
	
	private String createNomePapel(CentroCusto cc, int nivel) {
		final String[] papel = {"RESPONSAVEL_", "GESTOR_","GERENTE_"};
		StringBuilder nomePapel = new StringBuilder(papel[nivel - 1]);
		String nomeArea = cc.getCodigo();
		nomeArea = nomeArea.trim().replaceAll(" ", "_");
		nomePapel.append(nomeArea.toUpperCase());
		return nomePapel.toString();
	}

	@Override
	public void delete(CentroCusto t) {

		try {
			if (bpms.existeProcessoAtivo(Calendar.getInstance().get(Calendar.YEAR) + "")) {
				facesUtils.addErrorMessage("Não é possível remover um Centro de Custo enquanto haja um processo de budget ativo");
			} else {
				userCallBackCache.removeGroupsFromCache(responsavel.getLogin());
				userCallBackCache.removeGroupsFromCache(gestor.getLogin());
				super.delete(t);
			}
		} catch(Exception e ) {
			facesUtils.addErrorMessage("Erro ao consultar o processo BPM");
			logger.error("Erro ao consultar o processo BPM",e);
		}
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
	public TipoCentroCusto[] getTipoCentroCustoList() {
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
}
