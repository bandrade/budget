package com.dupont.budget.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
			query="select d from DespesaForecast d where d.forecast.id=:forecastId and d.tipoDespesa.id=:tipoDespesaId and d.acao.id=:acaoId")
})
public class DespesaForecast {

	private static final long serialVersionUID = -4344680928557842077L;

	public DespesaForecast(){}

	@EmbeddedId
	private DespesaForecastPK despesaPK;

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

	@ManyToOne
	@JoinColumn(name = "despesa_forecast_mes_id")
	private DespesaForecastMes despesaMensalisada ;

	private Double plm;

	private Double ytd;

	@Transient
	private Double valorComprometido;

	@ManyToOne
	@JoinColumn(name="despesa_budget_id")
	private Despesa despesaBudget;

	private String comentario;

	@Transient
	private boolean alterada;


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


	public DespesaForecastMes getDespesaMensalisada() {
			if(despesaMensalisada==null)
			{
				despesaMensalisada = new DespesaForecastMes();
			}
		return despesaMensalisada;
	}

	public void setDespesaMensalisada(DespesaForecastMes despesaMensalisada) {
		this.despesaMensalisada = despesaMensalisada;
	}

	public DespesaForecastPK getDespesaPK() {
			if(despesaPK ==null)
				despesaPK = new DespesaForecastPK();
		return despesaPK;
	}

	public void setDespesaPK(DespesaForecastPK despesaPK) {
		this.despesaPK = despesaPK;
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

	public Double obterPLM(DespesaForecast despesaForecast , Long mes)
	{
		Double[]  valores = new Double[]{despesaForecast.getDespesaMensalisada().getJaneiro(),
									 despesaForecast.getDespesaMensalisada().getFevereiro(),
									 despesaForecast.getDespesaMensalisada().getMarco(),
									 despesaForecast.getDespesaMensalisada().getAbril(),
									 despesaForecast.getDespesaMensalisada().getMaio(),
									 despesaForecast.getDespesaMensalisada().getJunho(),
									 despesaForecast.getDespesaMensalisada().getJulho(),
									 despesaForecast.getDespesaMensalisada().getAgosto(),
									 despesaForecast.getDespesaMensalisada().getSetembro(),
									 despesaForecast.getDespesaMensalisada().getOutubro(),
									 despesaForecast.getDespesaMensalisada().getNovembro(),
									 despesaForecast.getDespesaMensalisada().getDezembro()
					};

		Double ytd= 0d;
		for(int i=0; i<mes;i++)
		{
			ytd += valores[i] !=null ? valores[i] : 0d;
		}
		return ytd;
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

	public DespesaForecast(DespesaForecast despesaForecast, Integer mesSeguinte) {
		this(despesaForecast.getTipoDespesa(),despesaForecast.getCliente(),despesaForecast.getAcao(),
				despesaForecast.getVendedor(),despesaForecast.getProduto(),despesaForecast.getCultura(),despesaForecast.getDistrito(),true,
				despesaForecast.getValor(),
				despesaForecast.getComentario());
		DespesaForecastPK pk  = new DespesaForecastPK(despesaForecast.getDespesaPK().getAno(),
				Long.valueOf(mesSeguinte),despesaForecast.getDespesaPK().getId());
		this.setDespesaPK(pk);
		this.setYtd(obterPLM(despesaForecast, despesaForecast.getDespesaPK().getMes()));
		this.setPlm(obterPLM(despesaForecast, 12L));
		this.setDespesaBudget(despesaForecast.getDespesaBudget());
		this.setForecast(despesaForecast.getForecast());
		//XXX VERIFICAR SE PRECISA CRIAR UMA NOVA DESPESA MENSALIZADA
		this.setDespesaMensalisada(despesaForecast.getDespesaMensalisada());
	}


	public static DespesaForecast createFromDespesa(Despesa despesa) {
		DespesaForecast despesaForecast = new DespesaForecast(despesa.getTipoDespesa(),despesa.getCliente(),despesa.getAcao(),
				despesa.getVendedor(),despesa.getProduto(),despesa.getCultura(),despesa.getDistrito(),true,despesa.getValor(),
				despesa.getComentario());
		return despesaForecast;
	}

	public Double getPlm() {
		return plm;
	}

	public void setPlm(Double plm) {
		this.plm = plm;
	}

	public Double getYtd() {
		return ytd;
	}

	public void setYtd(Double ytd) {
		this.ytd = ytd;
	}

	public Double getValorComprometido() {
		return valorComprometido;
	}

	public void setValorComprometido(Double valorComprometido) {
		this.valorComprometido = valorComprometido;
	}

}

