package com.dupont.budget.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Divisao do valor da Despesa do Forecast por mes.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "despesa_forecast_mes")
public class DespesaForecastMes implements Serializable {

	private static final long serialVersionUID = -4848155767796227852L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  id;

	private Double janeiro;

	private Double fevereiro;

	private Double marco;

	private Double abril;

	private Double maio;

	private Double junho;

	private Double julho;

	private Double agosto;

	private Double setembro;

	private Double outubro;

	private Double novembro;

	private Double dezembro;


	@Transient
	private Double despesaJaneiro;

	@Transient
	private Double despesaFevereiro;

	@Transient
	private Double despesaMarco;

	@Transient
	private Double despesaAbril;

	@Transient
	private Double despesaMaio;

	@Transient
	private Double despesaJunho;

	@Transient
	private Double despesaJulho;

	@Transient
	private Double despesaAgosto;

	@Transient
	private Double despesaSetembro;

	@Transient
	private Double despesaOutubro;

	@Transient
	private Double despesaNovembro;

	@Transient
	private Double despesaDezembro;
	
	//bi-directional many-to-one association to DespesaForecastAno
	@OneToMany(mappedBy="despesaForecastMes")
	private List<DespesaForecastAno> despesaForecastAnos;

	public Boolean isValorComprometidoMaiorQueForecast()
	{
		if( despesaJaneiro !=null && janeiro!=null && despesaJaneiro > janeiro ||
			despesaFevereiro !=null && fevereiro!=null && despesaFevereiro> fevereiro ||
			despesaMarco !=null &&   marco!=null && despesaMarco> marco ||
			despesaAbril !=null && abril!=null && despesaAbril> abril ||
			despesaMaio !=null && maio!=null && despesaMaio > maio ||
			despesaJunho !=null && junho!=null && despesaJunho> junho ||
			despesaJulho !=null && julho!=null && despesaJulho> julho ||
			despesaAgosto !=null && agosto!=null && despesaAgosto> agosto ||
			despesaSetembro !=null && setembro!=null && despesaSetembro> setembro ||
			despesaOutubro !=null && outubro!=null && despesaOutubro> outubro ||
			despesaNovembro !=null && novembro!=null && despesaNovembro> novembro ||
			despesaDezembro !=null && dezembro!=null && despesaDezembro> dezembro
		)
		{
			return true;
		}
		return false;

	}

	public DespesaForecastMes(){}
	public DespesaForecastMes( Double janeiro, Double fevereiro,
			Double marco, Double abril, Double maio, Double junho,
			Double julho, Double agosto, Double setembro, Double outubro,
			Double novembro, Double dezembro) {
		this.janeiro = janeiro;
		this.fevereiro = fevereiro;
		this.marco = marco;
		this.abril = abril;
		this.maio = maio;
		this.junho = junho;
		this.julho = julho;
		this.agosto = agosto;
		this.setembro = setembro;
		this.outubro = outubro;
		this.novembro = novembro;
		this.dezembro = dezembro;
	}

	public static DespesaForecastMes createFromDespesaMes(DespesaForecastMes despesaMensalisada) {
		DespesaForecastMes forecastMes = new DespesaForecastMes(despesaMensalisada.getJaneiro(), despesaMensalisada.getFevereiro(),
				despesaMensalisada.getMarco(), despesaMensalisada.getAbril(), despesaMensalisada.getMaio(), despesaMensalisada.getJunho(),
				despesaMensalisada.getJulho(),
				despesaMensalisada.getAgosto(), despesaMensalisada.getSetembro(), despesaMensalisada.getOutubro(), despesaMensalisada.getNovembro(), 
				despesaMensalisada.getDezembro());
		return forecastMes;
	}

	public static DespesaForecastMes createFromBudgetMes(BudgetMes mes)
	{
		DespesaForecastMes forecastMes = new DespesaForecastMes(mes.getJaneiro(), mes.getFevereiro(),
				mes.getMarco(), mes.getAbril(), mes.getMaio(), mes.getJunho(), mes.getJulho(),
				mes.getAgosto(), mes.getSetembro(), mes.getOutubro(), mes.getNovembro(), mes.getDezembro());

		return forecastMes;
	}




	public Double getJaneiro() {
		return janeiro;
	}

	public void setJaneiro(Double janeiro) {
		this.janeiro = janeiro;
	}

	public Double getFevereiro() {
		return fevereiro;
	}

	public void setFevereiro(Double fevereiro) {
		this.fevereiro = fevereiro;
	}

	public Double getMarco() {
		return marco;
	}

	public void setMarco(Double marco) {
		this.marco = marco;
	}

	public Double getAbril() {
		return abril;
	}

	public void setAbril(Double abril) {
		this.abril = abril;
	}

	public Double getMaio() {
		return maio;
	}

	public void setMaio(Double maio) {
		this.maio = maio;
	}

	public Double getJunho() {
		return junho;
	}

	public void setJunho(Double junho) {
		this.junho = junho;
	}

	public Double getJulho() {
		return julho;
	}

	public void setJulho(Double julho) {
		this.julho = julho;
	}

	public Double getAgosto() {
		return agosto;
	}

	public void setAgosto(Double agosto) {
		this.agosto = agosto;
	}

	public Double getSetembro() {
		return setembro;
	}

	public void setSetembro(Double setembro) {
		this.setembro = setembro;
	}

	public Double getOutubro() {
		return outubro;
	}

	public void setOutubro(Double outubro) {
		this.outubro = outubro;
	}

	public Double getNovembro() {
		return novembro;
	}

	public void setNovembro(Double novembro) {
		this.novembro = novembro;
	}

	public Double getDezembro() {
		return dezembro;
	}

	public void setDezembro(Double dezembro) {
		this.dezembro = dezembro;
	}


	public Double getDespesaJaneiro() {
		return despesaJaneiro;
	}


	public void setDespesaJaneiro(Double despesaJaneiro) {
		this.despesaJaneiro = despesaJaneiro;
	}


	public Double getDespesaFevereiro() {
		return despesaFevereiro;
	}


	public void setDespesaFevereiro(Double despesaFevereiro) {
		this.despesaFevereiro = despesaFevereiro;
	}


	public Double getDespesaMarco() {
		return despesaMarco;
	}


	public void setDespesaMarco(Double despesaMarco) {
		this.despesaMarco = despesaMarco;
	}


	public Double getDespesaAbril() {
		return despesaAbril;
	}


	public void setDespesaAbril(Double despesaAbril) {
		this.despesaAbril = despesaAbril;
	}


	public Double getDespesaMaio() {
		return despesaMaio;
	}


	public void setDespesaMaio(Double despesaMaio) {
		this.despesaMaio = despesaMaio;
	}


	public Double getDespesaJunho() {
		return despesaJunho;
	}


	public void setDespesaJunho(Double despesaJunho) {
		this.despesaJunho = despesaJunho;
	}


	public Double getDespesaJulho() {
		return despesaJulho;
	}


	public void setDespesaJulho(Double despesaJulho) {
		this.despesaJulho = despesaJulho;
	}


	public Double getDespesaAgosto() {
		return despesaAgosto;
	}


	public void setDespesaAgosto(Double despesaAgosto) {
		this.despesaAgosto = despesaAgosto;
	}


	public Double getDespesaSetembro() {
		return despesaSetembro;
	}


	public void setDespesaSetembro(Double despesaSetembro) {
		this.despesaSetembro = despesaSetembro;
	}


	public Double getDespesaOutubro() {
		return despesaOutubro;
	}


	public void setDespesaOutubro(Double despesaOutubro) {
		this.despesaOutubro = despesaOutubro;
	}


	public Double getDespesaNovembro() {
		return despesaNovembro;
	}


	public void setDespesaNovembro(Double despesaNovembro) {
		this.despesaNovembro = despesaNovembro;
	}


	public Double getDespesaDezembro() {
		return despesaDezembro;
	}


	public void setDespesaDezembro(Double despesaDezembro) {
		this.despesaDezembro = despesaDezembro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DespesaForecastAno> getDespesaForecastAnos() {
		return despesaForecastAnos;
	}

	public void setDespesaForecastAnos(List<DespesaForecastAno> despesaForecastAnos) {
		this.despesaForecastAnos = despesaForecastAnos;
	}

	



}
