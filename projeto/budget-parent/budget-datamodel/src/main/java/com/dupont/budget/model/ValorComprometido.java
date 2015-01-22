package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "valor_comprometido", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"centro_custo_id", "tipo_despesa_id", "acao_id", "mes" })
})
@NamedQueries({
	@NamedQuery(name = ValorComprometido.FIND_BY_FILTRO, query = "select v from ValorComprometido v where lower(v.acao.nome) = :acao and lower(v.centroCusto.nome) = :centroCusto and lower(v.tipoDespesa.nome) = :tipoDespesa and v.mes = :mes")
})
public class ValorComprometido extends AbstractEntity<Long> {

	private static final long serialVersionUID = -8857965323037059577L;

	public static final String FIND_BY_FILTRO = "ValorCmprometido.findByFiltro";

	@ManyToOne
	@JoinColumn(name = "centro_custo_id")
	private CentroCusto centroCusto;

	@ManyToOne
	@JoinColumn(name = "tipo_despesa_id")
	private TipoDespesa tipoDespesa;

	@ManyToOne
	@JoinColumn(name = "acao_id")
	private Acao acao;

	private Integer mes;
	
	private Integer ano;

	private Double valor;

	private Boolean ativo;
	
	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}


}
