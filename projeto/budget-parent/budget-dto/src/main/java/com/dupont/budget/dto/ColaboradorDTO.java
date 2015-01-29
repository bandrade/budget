package com.dupont.budget.dto;

import java.io.Serializable;

public class ColaboradorDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2260780975111426175L;
	private String nome;
	private String login;
	private String email;
	private Long id;
	
	public ColaboradorDTO(){}
	public ColaboradorDTO(String nome,String login,String email) {
		super();
		this.nome = nome;
		this.email = email;
		this.login = login;
	}
	
	public ColaboradorDTO(String nome,String login,String email,Long id) {
		super();
		this.nome = nome;
		this.email = email;
		this.login = login;
		this.id=id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColaboradorDTO other = (ColaboradorDTO) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ColaboradorDTO [nome=" + nome + ", login=" + login + ", email="
				+ email + "]";
	}
	
	
	
	
	
}
