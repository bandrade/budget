package com.dupont.budget.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
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

	private BigDecimal janeiro;

	private BigDecimal fevereiro;

	private BigDecimal marco;

	private BigDecimal abril;

	private BigDecimal maio;

	private BigDecimal junho;

	private BigDecimal julho;

	@Column(name="agosto")
	private BigDecimal agosto;

	private BigDecimal setembro;

	@Column(name="outubro")
	private BigDecimal outubro;

	private BigDecimal novembro;

	private BigDecimal dezembro;


	@Transient
	private BigDecimal despesaJaneiro;

	@Transient
	private BigDecimal despesaFevereiro;

	@Transient
	private BigDecimal despesaMarco;

	@Transient
	private BigDecimal despesaAbril;

	@Transient
	private BigDecimal despesaMaio;

	@Transient
	private BigDecimal despesaJunho;

	@Transient
	private BigDecimal despesaJulho;

	@Transient
	private BigDecimal despesaAgosto;

	@Transient
	private BigDecimal despesaSetembro;

	@Transient
	private BigDecimal despesaOutubro;

	@Transient
	private BigDecimal despesaNovembro;

	@Transient
	private BigDecimal despesaDezembro;

	//bi-directional many-to-one association to DespesaForecastAno
	@OneToMany(mappedBy="despesaForecastMes")
	private List<DespesaForecastAno> despesaForecastAnos;

	public Boolean isValorComprometidoMaiorQueForecast()
	{
		if( despesaJaneiro !=null && janeiro!=null && despesaJaneiro.compareTo(janeiro) > 0 ||
			despesaFevereiro !=null && fevereiro!=null && despesaFevereiro.compareTo(fevereiro) > 0  ||
			despesaMarco !=null &&   marco!=null && despesaMarco.compareTo(marco) > 0  ||
			despesaAbril !=null && abril!=null && despesaAbril.compareTo(abril) > 0  ||
			despesaMaio !=null && maio!=null && despesaMaio.compareTo(maio) > 0  ||
			despesaJunho !=null && junho!=null && despesaJunho.compareTo(junho) > 0 ||
			despesaJulho !=null && julho!=null && despesaJulho.compareTo(julho) > 0 ||
			despesaAgosto !=null && agosto!=null && despesaAgosto.compareTo(agosto) > 0  ||
			despesaSetembro !=null && setembro!=null && despesaSetembro.compareTo(setembro) > 0 ||
			despesaOutubro !=null && outubro!=null && despesaOutubro.compareTo(outubro) > 0 ||
			despesaNovembro !=null && novembro!=null && despesaNovembro.compareTo(novembro) > 0  ||
			despesaDezembro !=null && dezembro!=null && despesaDezembro.compareTo(dezembro) > 0
		)
		{
			return true;
		}
		return false;

	}

	public boolean possuiValorPreenchido()
	{
		 	 return
		 			janeiro !=null 	||
					fevereiro !=null ||
					marco !=null 	||
					abril !=null 	||
					maio !=null 		||
					junho !=null 	||
					julho !=null		||
					agosto !=null 	||
					setembro !=null 	||
					outubro !=null 	||
					novembro !=null 	||
					dezembro !=null ;
	}

	public DespesaForecastMes(){}
	public DespesaForecastMes( BigDecimal janeiro, BigDecimal fevereiro,
			BigDecimal marco, BigDecimal abril, BigDecimal maio, BigDecimal junho,
			BigDecimal julho, BigDecimal agosto, BigDecimal setembro, BigDecimal outubro,
			BigDecimal novembro, BigDecimal dezembro) {
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




	public BigDecimal getJaneiro() {
		return janeiro;
	}

	public void setJaneiro(BigDecimal janeiro) {
		this.janeiro = janeiro;
	}

	public BigDecimal getFevereiro() {
		return fevereiro;
	}

	public void setFevereiro(BigDecimal fevereiro) {
		this.fevereiro = fevereiro;
	}

	public BigDecimal getMarco() {
		return marco;
	}

	public void setMarco(BigDecimal marco) {
		this.marco = marco;
	}

	public BigDecimal getAbril() {
		return abril;
	}

	public void setAbril(BigDecimal abril) {
		this.abril = abril;
	}

	public BigDecimal getMaio() {
		return maio;
	}

	public void setMaio(BigDecimal maio) {
		this.maio = maio;
	}

	public BigDecimal getJunho() {
		return junho;
	}

	public void setJunho(BigDecimal junho) {
		this.junho = junho;
	}

	public BigDecimal getJulho() {
		return julho;
	}

	public void setJulho(BigDecimal julho) {
		this.julho = julho;
	}

	public BigDecimal getAgosto() {
		return agosto;
	}

	public void setAgosto(BigDecimal agosto) {
		this.agosto = agosto;
	}

	public BigDecimal getSetembro() {
		return setembro;
	}

	public void setSetembro(BigDecimal setembro) {
		this.setembro = setembro;
	}

	public BigDecimal getOutubro() {
		return outubro;
	}

	public void setOutubro(BigDecimal outubro) {
		this.outubro = outubro;
	}

	public BigDecimal getNovembro() {
		return novembro;
	}

	public void setNovembro(BigDecimal novembro) {
		this.novembro = novembro;
	}

	public BigDecimal getDezembro() {
		return dezembro;
	}

	public void setDezembro(BigDecimal dezembro) {
		this.dezembro = dezembro;
	}


	public BigDecimal getDespesaJaneiro() {
		return despesaJaneiro;
	}


	public void setDespesaJaneiro(BigDecimal despesaJaneiro) {
		this.despesaJaneiro = despesaJaneiro;
	}


	public BigDecimal getDespesaFevereiro() {
		return despesaFevereiro;
	}


	public void setDespesaFevereiro(BigDecimal despesaFevereiro) {
		this.despesaFevereiro = despesaFevereiro;
	}


	public BigDecimal getDespesaMarco() {
		return despesaMarco;
	}


	public void setDespesaMarco(BigDecimal despesaMarco) {
		this.despesaMarco = despesaMarco;
	}


	public BigDecimal getDespesaAbril() {
		return despesaAbril;
	}


	public void setDespesaAbril(BigDecimal despesaAbril) {
		this.despesaAbril = despesaAbril;
	}


	public BigDecimal getDespesaMaio() {
		return despesaMaio;
	}


	public void setDespesaMaio(BigDecimal despesaMaio) {
		this.despesaMaio = despesaMaio;
	}


	public BigDecimal getDespesaJunho() {
		return despesaJunho;
	}


	public void setDespesaJunho(BigDecimal despesaJunho) {
		this.despesaJunho = despesaJunho;
	}


	public BigDecimal getDespesaJulho() {
		return despesaJulho;
	}


	public void setDespesaJulho(BigDecimal despesaJulho) {
		this.despesaJulho = despesaJulho;
	}


	public BigDecimal getDespesaAgosto() {
		return despesaAgosto;
	}


	public void setDespesaAgosto(BigDecimal despesaAgosto) {
		this.despesaAgosto = despesaAgosto;
	}


	public BigDecimal getDespesaSetembro() {
		return despesaSetembro;
	}


	public void setDespesaSetembro(BigDecimal despesaSetembro) {
		this.despesaSetembro = despesaSetembro;
	}


	public BigDecimal getDespesaOutubro() {
		return despesaOutubro;
	}


	public void setDespesaOutubro(BigDecimal despesaOutubro) {
		this.despesaOutubro = despesaOutubro;
	}


	public BigDecimal getDespesaNovembro() {
		return despesaNovembro;
	}


	public void setDespesaNovembro(BigDecimal despesaNovembro) {
		this.despesaNovembro = despesaNovembro;
	}


	public BigDecimal getDespesaDezembro() {
		return despesaDezembro;
	}


	public void setDespesaDezembro(BigDecimal despesaDezembro) {
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
