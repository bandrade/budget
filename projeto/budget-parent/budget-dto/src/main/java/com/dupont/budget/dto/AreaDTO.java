package com.dupont.budget.dto;

public class AreaDTO {
	private Long id;
	private ColaboradorDTO lider;
	private String nome;

	public ColaboradorDTO getLider() {
		return lider;
	}
	public void setLider(ColaboradorDTO lider) {
		this.lider = lider;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}



}
