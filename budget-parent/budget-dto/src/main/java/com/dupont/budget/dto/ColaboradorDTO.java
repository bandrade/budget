package com.dupont.budget.dto;

import java.io.Serializable;

public class ColaboradorDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2260780975111426175L;
	private String nome;

	public ColaboradorDTO(){}
	public ColaboradorDTO(String nome) {
		super();
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
