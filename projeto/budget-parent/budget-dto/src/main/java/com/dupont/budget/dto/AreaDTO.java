package com.dupont.budget.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AreaDTO implements Serializable {
	private static final long serialVersionUID = 4140788853802341849L;
	private Long id;
	private String nome;
	private String nomePapelLider;
	private String nomePapelLancamento;
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
		return nomePapelLider;
	}

	public String getNomePapelLancamentoNotas()
	{
		return nomePapelLancamento;
	}

	public List<CentroDeCustoDTO> getCentrosDeCusto() {
		if(centrosDeCusto == null)
			centrosDeCusto = new ArrayList<CentroDeCustoDTO>();
		return centrosDeCusto;
	}
	public void setCentrosDeCusto(List<CentroDeCustoDTO> centrosDeCusto) {
		this.centrosDeCusto = centrosDeCusto;
	}
	@Override
	public String toString() {
		return "AreaDTO [nome=" + nome + " lider=" + getNomePapel() +"]";
	}
	public String getNomePapelLider() {
		return nomePapelLider;
	}
	public void setNomePapelLider(String nomePapelLider) {
		this.nomePapelLider = nomePapelLider;
	}
	public String getNomePapelLancamento() {
		return nomePapelLancamento;
	}
	public void setNomePapelLancamento(String nomePapelLancamento) {
		this.nomePapelLancamento = nomePapelLancamento;
	}


}
