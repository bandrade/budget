package com.dupont.budget.dto;

import java.io.Serializable;

public class PapelDTO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4012214329468320261L;
	private String nomePapel;
	private ColaboradorDTO colaborador;

	public PapelDTO(){}
	public PapelDTO(String nomePapel, ColaboradorDTO colaborador) {
		super();
		this.nomePapel = nomePapel;
		this.colaborador = colaborador;
	}
	public String getNomePapel() {
		return nomePapel;
	}
	public void setNomePapel(String nomePapel) {
		this.nomePapel = nomePapel;
	}
	public ColaboradorDTO getColaborador() {
		return colaborador;
	}
	public void setColaborador(ColaboradorDTO colaborador) {
		this.colaborador = colaborador;
	}
	@Override
	public String toString() {
		return "PapelDTO [nomePapel=" + nomePapel + ", colaborador="
				+ colaborador + "]";
	}



}
