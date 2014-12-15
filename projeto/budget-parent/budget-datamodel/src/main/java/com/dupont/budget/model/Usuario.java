package com.dupont.budget.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@NamedQuery(name="Usuario.findByLogin", query="select u from Usuario u where u.login = :login")
})
public class Usuario {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String nome;
	
	private String login;
	
	private String password;
	
	private String email;
	
	@Enumerated(EnumType.STRING)
	private Perfil perfil;
	
	@OneToMany(targetEntity=PapelUsuario.class)
	@JoinColumn(name="usuario_id")
	private Set<PapelUsuario> papeis;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

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

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Set<PapelUsuario> getPapeis() {
		return papeis;
	}

	public void setPapeis(Set<PapelUsuario> papeis) {
		this.papeis = papeis;
	}
}
