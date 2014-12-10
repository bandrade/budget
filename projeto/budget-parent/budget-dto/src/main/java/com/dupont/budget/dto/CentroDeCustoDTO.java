package com.dupont.budget.dto;

import java.io.Serializable;
import java.util.List;


public class CentroDeCustoDTO  implements Serializable{
	
	private static final long serialVersionUID = 2777657230661210967L;
	private String nome;
	private String numero;
	private String area;
	private List<PapelDTO> papeis;
	
	public CentroDeCustoDTO(){}
	public CentroDeCustoDTO(String nome, String numero,String area) {
		super();
		this.nome = nome;
		this.numero = numero;
		this.area = area;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public List<PapelDTO> getPapeis() {
		return papeis;
	}
	public void setPapeis(List<PapelDTO> papeis) {
		this.papeis = papeis;
	}
	
	
	/*
	public String getPapelResponsavel()
	{
		return "RESPONSAVEL_"+numero;
	}
	public String getPapelGerente()
	{
		return "GERENTE_"+numero;
	}
	public String getPapelLider()
	{
		return "LIDER_"+area;
	}*/
	

}
