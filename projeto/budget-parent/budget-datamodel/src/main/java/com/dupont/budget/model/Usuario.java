package com.dupont.budget.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Usuário da aplicação.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.findByLogin", query = "select u from Usuario u where u.login = :login"),
	@NamedQuery(name = Usuario.FIND_BY_NOME_PAPEL, query = "select u from Usuario u join u.papeis p where p.papel.nome = :nome")
})
public class Usuario extends NamedAbstractEntity<Long> {
	
	private static final long serialVersionUID = 5218210368120783415L;
	
	public static final String FIND_BY_NOME_PAPEL = "Usuario.findByNomePapel";

	private String login;
	
	private String password;
	
	private String email;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "perfil_usuario",  joinColumns = @JoinColumn(name = "usuario_id"))
	@Column(name = "perfil")
	@Enumerated(EnumType.STRING)
	private Set<Perfil> perfis;
	
	@OneToMany(targetEntity = PapelUsuario.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "usuario_id")
	private Set<PapelUsuario> papeis;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Perfil> getPerfis() {
		if (perfis == null) {
			perfis = new HashSet<>();
		}
		return perfis;
	}

	public void setPerfis(Set<Perfil> perfil) {
		this.perfis = perfil;
	}

	public Set<PapelUsuario> getPapeis() {
		if (papeis == null) {
			papeis = new HashSet<>();
		}
		return papeis;
	}

	public void setPapeis(Set<PapelUsuario> papeis) {
		this.papeis = papeis;
	}
}
