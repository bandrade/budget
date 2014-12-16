package com.dupont.budget.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Itens de despesa do Budget
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="despesa")
@NamedQueries({
	@NamedQuery(name="Despesa.agruparPorTipoDeDespesa", query="select c from Despesa c where c.budget.id = :id group by c.tipoDespesa")
}) 
public class Despesa extends AbstractEntity<Long> {
	
	private static final long serialVersionUID = -4344680928557842077L;

	@ManyToOne
	@JoinColumn(name="tipo_despesa_id")
	private TipoDespesa tipoDespesa;
	
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="acao_id")
	private Acao acao;
	
	@ManyToOne
	@JoinColumn(name="vendedor_id")
	private Vendedor vendedor;

	@ManyToOne
	@JoinColumn(name="produto_id")
	private Produto produto;
	
	@ManyToOne
	@JoinColumn(name="cultura_id")
	private Cultura cultura;
	
	@ManyToOne
	@JoinColumn(name="distrito_id")
	private Distrito distrito;
	
	@ManyToOne
	@JoinColumn(name="budget_id")
	private Budget budget;
	
	private Double valor;
	
	private String comentario;

	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Cultura getCultura() {
		return cultura;
	}

	public void setCultura(Cultura cultura) {
		this.cultura = cultura;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}
	
	public void init(){
		setAcao(new Acao());
		setProduto(new Produto());
		setCultura(new Cultura());
		setCliente(new Cliente());
		setDistrito(new Distrito());
		setVendedor(new Vendedor());
		setTipoDespesa(new TipoDespesa());
	}
}

