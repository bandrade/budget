package com.dupont.budget.dto;

import java.io.Serializable;
import java.util.List;


public class CentroDeCustoDTO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4288095550251406045L;
	private Long  id;
	private String nome;
	private String numero;
	private String area;
	private List<PapelDTO> papeis;
	
	public CentroDeCustoDTO(){}
	public CentroDeCustoDTO(Long id ,String nome, String numero,String area,List<PapelDTO> papeis) {
		super();
		this.id = id;
		this.nome = nome;
		this.numero = numero;
		this.area = area;
		this.papeis = papeis;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "nome=" + nome + ", numero=" + numero + "]";
	}
	
	
	
}
