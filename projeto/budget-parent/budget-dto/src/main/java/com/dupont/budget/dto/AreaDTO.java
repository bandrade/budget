package com.dupont.budget.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AreaDTO implements Serializable {
	private static final long serialVersionUID = 4140788853802341849L;
	private Long id;
	private String nome;
	private String regex="[^a-zA-Z0-9 -]";
	private List<CentroDeCustoDTO> centrosDeCusto;
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
	public String getNomePapel() {
		return "LIDER_"+nome.replaceAll(regex, "").toUpperCase();
	}

	public String getNomePapelLancamentoNotas()
	{
		return "NOTAS_"+nome.replaceAll(regex, "").toUpperCase();
	}
	
	public List<CentroDeCustoDTO> getCentrosDeCusto() {
		if(centrosDeCusto == null)
			centrosDeCusto = new ArrayList<CentroDeCustoDTO>();
		return centrosDeCusto;
	}
	public void setCentrosDeCusto(List<CentroDeCustoDTO> centrosDeCusto) {
		this.centrosDeCusto = centrosDeCusto;
	}
	
	
}
