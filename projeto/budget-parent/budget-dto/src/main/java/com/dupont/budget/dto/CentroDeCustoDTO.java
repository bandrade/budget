package com.dupont.budget.dto;

import java.io.Serializable;


public class CentroDeCustoDTO  implements Serializable{
	
	private static final long serialVersionUID = 2777657230661210967L;
	private String nome;
	private String numero;
	private String area;
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
	}
	

}
