package com.dupont.budget.web.actions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.crypto.CryptoUtil;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;

import com.dupont.budget.bpm.custom.user.UserGroupCallbackCacheManager;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Perfil;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Bean de criação o usuário.
 *
 * A autenticação do usuário é feita atraves do Login Module do EAP. A configuração segue descrita abaixo: <br/>
 *
 * <security-domain name="dupont-security-domain">
 *    <authentication>
 *        <login-module code="Database" flag="required">
 *            <module-option name="dsJndiName" value="java:/jboss/datasources/BudgetDS"/>
 *            <module-option name="principalsQuery" value="select password from usuario where login = ?"/>
 *            <module-option name="rolesQuery" value="select perfil, 'Roles' from usuario INNER JOIN perfil_usuario
 *            	on usuario.id = perfil_usuario.usuario_id where login = ?"/>
 *            <module-option name="hashAlgorithm" value="SHA-256"/>
 *            <module-option name="hashEncoding" value="base64"/>
 *        </login-module>
 *	  </authentication>
 * </security-domain>
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Model
@RolesAllowed(value = "ADMINISTRADOR")
public class UsuarioAction extends GenericAction<Usuario> {

	/*   Para criar a senha HASH do password, utilizar o seguinte código.
	 *
	 *   Criar o hash no JAVA:
	 *
	 *   String pass = Util.createPasswordHash("SHA-256", "BASE64", null, null, "123");
     *
     *   Criar o encoding no MySQL:
     *
     *   TO_BASE64( sha2('PASSWORD', 256) )
	 */

	private static final long serialVersionUID = -6061667499898003022L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@Inject
	private UserGroupCallbackCacheManager userCallBackCache;

	private String senha;

	private String confirmacaoSenha;

	private Set<PapelUsuario> papeis;

	private DualListModel<Papel> papelList;

	private DualListModel<Perfil> perfilList;

	@Named
	@Produces
	public Usuario getColaborador() {
		return getEntidade();
	}

	@Named
	@Produces
	public List<Usuario> getUsuarioList() {
		return service.findAll(Usuario.class);
	}

	@Named
	@Produces
	public DualListModel<Perfil> getPerfilList() {
		if (perfilList == null) {
			List<Perfil> source = new LinkedList<>(Arrays.asList(Perfil.values()));
			List<Perfil> target = new LinkedList<Perfil>();
			if (getEntidade().getId() != null) {
				Set<Perfil> list = service.findById(getEntidade()).getPerfis();
				if (!list.isEmpty()) {
					for (Perfil o: list) {
						target.add(o);
						source.remove(o);
					}
				}
			}
			perfilList = new DualListModel<>(source, target);
		}
		return perfilList;
	}

	@Named
	@Produces
	public DualListModel<Papel> getPapelList() {
		if (papelList == null) {
			List<Papel> source = service.findAll(Papel.class);
			List<Papel> target = new LinkedList<Papel>();
			if (getEntidade().getId() != null) {
				Set<PapelUsuario> list = getEntidade().getPapeis();
				for (PapelUsuario o: list) {
					target.add(o.getPapel());
					source.remove(o.getPapel());
				}
			}
			papelList = new DualListModel<>(source, target);
		}
		return papelList;
	}

	@Override
	public String persist() {
		if (senha != null && !senha.isEmpty()) {
			String pass = CryptoUtil.createPasswordHash("SHA-256", "BASE64", null, null, senha);
			entidade.setPassword(pass);
		} else {
			if (entidade.getPassword() == null || entidade.getPassword().isEmpty()) {
				facesUtils.addErrorMessage("Senha é obrigatória.");
				return null;
			}
		}

		if (!mustCreate()) {
			for (PapelUsuario p : service.findById(entidade).getPapeis()) {
				if (!papelList.getTarget().contains(p.getPapel())) {
					if (p.getCentroCusto() != null) {
						facesUtils.addErrorMessage(String.format("Usuário associado a um centro de custo, não é possível fazer a remoção do perfil %s.", p.getPapel().getNome()));
						return null;
					}

					if (p.getArea() != null) {
						facesUtils.addErrorMessage(String.format("Usuário é lider de uma área, não é possível fazer a remoção do perfil %s.", p.getPapel().getNome()));
						return null;
					}
				}
			}
		}

		List<PapelUsuario> references = service.listPapelReferences(papelList.getTarget());
		if (!references.isEmpty()) {
			for (PapelUsuario p: references) {
				if (p.getCentroCusto() != null) {
					facesUtils.addErrorMessage(String.format("Papel %s só pode ser associado no cadastro do Centro de custo.", p.getPapel().getNome()));
					papelList.getTarget().remove(p.getPapel());
					papelList.getSource().add(p.getPapel());
					return null;
				}

				if (p.getArea() != null) {
					facesUtils.addErrorMessage(String.format("Papel %s só pode ser associado no cadastro da Área.", p.getPapel().getNome()));
					papelList.getTarget().remove(p.getPapel());
					papelList.getSource().add(p.getPapel());
					return null;
				}
			}
		}

		if (papelList.getTarget().isEmpty()) {
			entidade.getPapeis().clear();
		} else {
			for (Papel p: papelList.getTarget()) {
				entidade.getPapeis().add(new PapelUsuario(p, entidade));
			}
		}

		entidade.getPerfis().clear();

		for (Object s : perfilList.getTarget()) {
			entidade.getPerfis().add(Perfil.valueOf(s.toString()));
		}

		userCallBackCache.removeGroupsFromCache(entidade.getLogin());
		return super.persist();
	}

	public void showRoles(Usuario usuario) {
		Usuario u = service.getUsuarioByLogin(usuario.getLogin());
		papeis = u.getPapeis();
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public Set<PapelUsuario> getPapeis() {
		return papeis;
	}

	public void setPapeis(Set<PapelUsuario> papeis) {
		this.papeis = papeis;
	}

	public void setPapelList(DualListModel<Papel> papelList) {
		this.papelList = papelList;
	}

	public void setPerfilList(DualListModel<Perfil> perfilList) {
		this.perfilList = perfilList;
	}
}
