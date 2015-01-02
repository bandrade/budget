package com.dupont.budget.model;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sun.crypto.provider.DESParameters;

/**
 * Itens de despesa do Forecast
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name="despesa_forecast")
@NamedQueries({
})
public class DespesaForecast extends AbstractEntity<Long> {

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
	@JoinColumn(name="forecast_id")
	private Forecast forecast;


	private Boolean ativo;

	private Double valor;

	@OneToOne
	@JoinColumn(name = "id")
	private DespesaForecastMes despesaMensalisada ;

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

	public Forecast getForecast() {
		return forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}


	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}


	public DespesaForecastMes getDespesaMensalisada() {
		return despesaMensalisada;
	}

	public void setDespesaMensalisada(DespesaForecastMes despesaMensalisada) {
		this.despesaMensalisada = despesaMensalisada;
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


	public DespesaForecast(TipoDespesa tipoDespesa, Cliente cliente,
			Acao acao, Vendedor vendedor, Produto produto, Cultura cultura,
			Distrito distrito, Boolean ativo, Double valor,
			String comentario) {
		this.tipoDespesa = tipoDespesa;
		this.cliente = cliente;
		this.acao = acao;
		this.vendedor = vendedor;
		this.produto = produto;
		this.cultura = cultura;
		this.distrito = distrito;
		this.ativo = ativo;
		this.valor = valor;
		this.comentario = comentario;
	}

	public static DespesaForecast createFromDespesa(Despesa despesa) {
		DespesaForecast despesaForecast = new DespesaForecast(despesa.getTipoDespesa(),despesa.getCliente(),despesa.getAcao(),
				despesa.getVendedor(),despesa.getProduto(),despesa.getCultura(),despesa.getDistrito(),true,despesa.getValor(),
				despesa.getComentario());

		return despesaForecast;
	}





}

