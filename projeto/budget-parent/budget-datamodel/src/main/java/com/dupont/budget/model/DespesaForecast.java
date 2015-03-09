package com.dupont.budget.model;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	@NamedQuery(name="DespesaForecast.findByForecastTipoDespesaAndAcao",
			query="select d from DespesaForecast d where d.forecast.id=:forecastId and d.tipoDespesa.id=:tipoDespesaId and d.acao.id=:acaoId"),
	@NamedQuery(name="DespesaForecast.obterDespesaPorTipoEAcao", query="select c from DespesaForecast c where c.forecast.id = :forecastId and c.tipoDespesa.id=:tipoDespesaId and c.acao.id=:acaoId"),
	@NamedQuery(name="DespesaForecast.obterDespesaTipoDespesa", query="select distinct c.tipoDespesa from DespesaForecast c where c.forecast.id = :forecastId and c.ativo=true "),
	@NamedQuery(name="DespesaForecast.obterAcoesDespesaPorTipoDespesa", query="select distinct c.acao from DespesaForecast c where c.forecast.id = :forecastId and c.ativo=true and c.tipoDespesa.id=:tipoDespesaId")

})
public class DespesaForecast {

	private static final long serialVersionUID = -4344680928557842077L;

	public DespesaForecast(){}

	@Id
	private Long id;

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

	private BigDecimal valor;
	private BigDecimal plm;

	private BigDecimal ytd;

	@Transient
	private BigDecimal valorComprometido;

	@ManyToOne
	@JoinColumn(name="despesa_budget_id")
	private Despesa despesaBudget;

	private String comentario;

	@Transient
	private boolean alterada;

	@Transient
	private DespesaForecastMes despesaMensalisada;

	@Transient
	private DespesaSolicitacaoPagamento despesaSolicitacaoPagamento;


	//bi-directional many-to-one association to DespesaForecastAno
	@OneToMany(mappedBy="despesaForecast",fetch=FetchType.EAGER)
	private Set<DespesaForecastAno> despesaForecastMes;


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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
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

	public Despesa getDespesaBudget() {
		return despesaBudget;
	}

	public void setDespesaBudget(Despesa despesaBudget) {
		this.despesaBudget = despesaBudget;
	}


	public boolean isAlterada() {
		return alterada;
	}

	public void setAlterada(boolean alterada) {
		this.alterada = alterada;
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
			Distrito distrito, Boolean ativo, BigDecimal valor,
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

	public DespesaForecast(DespesaForecast despesaForecast, Integer mesSeguinte) {
		this(despesaForecast.getTipoDespesa(),despesaForecast.getCliente(),despesaForecast.getAcao(),
				despesaForecast.getVendedor(),despesaForecast.getProduto(),despesaForecast.getCultura(),despesaForecast.getDistrito(),true,
				despesaForecast.getValor(),
				despesaForecast.getComentario());

		this.setDespesaBudget(despesaForecast.getDespesaBudget());
		this.setForecast(despesaForecast.getForecast());
		DespesaForecastMes despesaForecastMes =  DespesaForecastMes.createFromDespesaMes(despesaForecast.getDespesaMensalisada());
		this.setDespesaMensalisada(despesaForecastMes);
	}


	public static DespesaForecast createFromDespesa(Despesa despesa) {
		DespesaForecast despesaForecast = new DespesaForecast(despesa.getTipoDespesa(),despesa.getCliente(),despesa.getAcao(),
				despesa.getVendedor(),despesa.getProduto(),despesa.getCultura(),despesa.getDistrito(),true,despesa.getValor(),
				despesa.getComentario());
		return despesaForecast;
	}

	public BigDecimal getPlm() {
		return plm;
	}

	public void setPlm(BigDecimal plm) {
		this.plm = plm;
	}

	public BigDecimal getYtd() {
		return ytd;
	}

	public void setYtd(BigDecimal ytd) {
		this.ytd = ytd;
	}

	public BigDecimal getValorComprometido() {
		return valorComprometido;
	}

	public void setValorComprometido(BigDecimal valorComprometido) {
		this.valorComprometido = valorComprometido;
	}

	public Set<DespesaForecastAno> getDespesaForecastMes() {
		if(despesaForecastMes==null)
			despesaForecastMes = new HashSet<>();
		return despesaForecastMes;
	}

	public void setDespesaForecastMes(Set<DespesaForecastAno> despesaForecastMes) {
		this.despesaForecastMes = despesaForecastMes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DespesaForecastMes getDespesaMensalisada() {
		if(despesaMensalisada==null)
			despesaMensalisada = new DespesaForecastMes();
		return despesaMensalisada;
	}

	public void setDespesaMensalisada(DespesaForecastMes despesaMensalisada) {

		this.despesaMensalisada = despesaMensalisada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DespesaForecast other = (DespesaForecast) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public DespesaSolicitacaoPagamento getDespesaSolicitacaoPagamento() {
		return despesaSolicitacaoPagamento;
	}

	public void setDespesaSolicitacaoPagamento(
			DespesaSolicitacaoPagamento despesaSolicitacaoPagamento) {
		this.despesaSolicitacaoPagamento = despesaSolicitacaoPagamento;
	}





}

