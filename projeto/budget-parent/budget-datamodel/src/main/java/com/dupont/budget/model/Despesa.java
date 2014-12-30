package com.dupont.budget.model;

import javax.persistence.Column;
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
	@NamedQuery(name="Despesa.agruparPorTipoDeDespesa", query="select c.tipoDespesa, sum(c.valor) from Despesa c where c.budget.id = :id group by c.tipoDespesa"),
	@NamedQuery(name="Despesa.obterDespesaNoDetalhe", query="select c from Despesa c where c.budget.id = :budgetId and c.tipoDespesa.id=:id"),
	@NamedQuery(name="Despesa.obterSomaDespesa", query="select sum(c.valor) from Despesa c where c.budget.id = :budgetId"),
	@NamedQuery(name="Despesa.obterDespesaNoDetalheBudget", query="select c from Despesa c where c.budget.id = :budgetId order by c.tipoDespesa.id"),
	@NamedQuery(name="Despesa.aprovarDespesasBudget", query="update Despesa c  set c.aprovado=true where c.budget.id = :budgetId")
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

	@Column(name="aprovacao")
	private Boolean aprovado;

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


	public void initLists(){
		if(acao==null)
			setAcao(new Acao());
		if(produto==null)
			setProduto(new Produto());
		if(cultura==null)
			setCultura(new Cultura());
		if(cliente==null)
			setCliente(new Cliente());
		if(distrito==null)
			setDistrito(new Distrito());
		if(vendedor==null)
			setVendedor(new Vendedor());
		if(tipoDespesa==null)
			setTipoDespesa(new TipoDespesa());

	}
	public void init(){
			initLists();
			setValor(null);
	}

	public Boolean getAprovado() {
		return aprovado;
	}

	public void setAprovado(Boolean aprovado) {
		this.aprovado = aprovado;
	}


}

