package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.DespesaMesDTO;
import com.dupont.budget.model.Budget;
import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;

@ConversationScoped
@Named
public class DivisaoBudgetMesAction implements Serializable{
	private CentroDeCustoDTO centroDeCusto;
	private Budget budget;
	private Long idInstanciaProcesso;
	private Long idTarefa;
	private String ano;

	@Inject
	private Conversation conversation;

	@Inject
    private BPMSProcessService bpmsProcesso;

	@Inject
	private BPMSTaskService bpmsTask;

	@Inject
	private BudgetService budgetService;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	private List<DespesaMesDTO> despesas;

	private BigDecimal valorTotalDetalhe;


	@PostConstruct
	private void init()
	{
		if(conversation.isTransient())
			conversation.begin();
	}

	public void obterDadosBudget() {
		try {
			Map<String,Object> dados = bpmsTask.obterConteudoTarefa(idTarefa);
			centroDeCusto = (CentroDeCustoDTO)dados.get("centroCusto");
			ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
			budget = budgetService.findByAnoAndCentroDeCusto(ano, centroDeCusto.getId());
			if(despesas ==null)
				obterDespesaNoDetalhe(budget.getId());
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
	}

	public void obterDespesaNoDetalhe(Long budgetId)
	{
		try
		{
			setDespesas(budgetService.obterDespesaNoDetalheBudgetAsDTO(budget.getId()));
			calcularValorTotalDetalhe();
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao obter despesa no detalhe");
			logger.error("Erro ao obter despesa no detalhe", e);
		}

	}
	public BigDecimal calcularValorMensalisado(DespesaMesDTO despesa)
	{
		BigDecimal valor = new BigDecimal(0d);
		if(despesa.getJaneiro()!=null)
			valor =valor.add(despesa.getJaneiro());
		if(despesa.getFevereiro()!=null)
			valor =valor.add(despesa.getFevereiro());
		if(despesa.getMarco()!=null)
			valor =valor.add(despesa.getMarco());
		if(despesa.getAbril()!=null)
			valor =valor.add(despesa.getAbril());
		if(despesa.getMaio()!=null)
			valor =valor.add(despesa.getMaio());
		if(despesa.getJunho()!=null)
			valor =valor.add(despesa.getJunho());
		if(despesa.getJulho()!=null)
			valor =valor.add(despesa.getJulho());
		if(despesa.getAgosto()!=null)
			valor =valor.add(despesa.getAgosto());
		if(despesa.getSetembro()!=null)
			valor =valor.add(despesa.getSetembro());
		if(despesa.getOutubro()!=null)
			valor =valor.add(despesa.getOutubro());
		if(despesa.getNovembro()!=null)
			valor =valor.add(despesa.getNovembro());
		if(despesa.getDezembro()!=null)
			valor =valor.add(despesa.getDezembro());

		return valor;
	}
	public void calcularValorTotalDetalhe()
	{
		valorTotalDetalhe = new BigDecimal(0d);
		for(DespesaMesDTO despesa : despesas)
		{
			if(despesa !=null && despesa.getValor() !=null)
				valorTotalDetalhe =valorTotalDetalhe.add(despesa.getValor()) ;
		}
	}

	public String concluir()
	{
		try {
		budgetService.mensalisarBudget(despesas);
		boolean possuiErro = false ;
		if(!calcularValorColuna("TOT").setScale(2,BigDecimal.ROUND_HALF_UP).equals(getValorTotalDetalhe().setScale(2,BigDecimal.ROUND_HALF_UP)))
		{
			facesUtils.addErrorMessage("O valor total do budget mensalisado deve ser igual ao valor total aprovado");
			possuiErro = true;
		}
		else
		{
			for(DespesaMesDTO despesa : despesas)
			{

				BigDecimal valorTotalMensalisado = calcularValorMensalisado(despesa);
				logger.info("Valor despesa" + despesa.getValor() +"- Valor Mensalisado: " + valorTotalMensalisado);

				if(!(despesa.getValor().setScale(2,BigDecimal.ROUND_HALF_UP).equals(valorTotalMensalisado.setScale(2,BigDecimal.ROUND_HALF_UP))))
				{
					facesUtils.addErrorMessage("O valor total do budget mensalisado da despesa " +despesa.getTipoDespesa()+ ""
							+ " deve ser igual ao valor total aprovado "+facesUtils.formatarDinheiro(despesa.getValor())+ " . O valor total mensalidado foi "
							+facesUtils.formatarDinheiro(valorTotalMensalisado) );
					possuiErro = true;
				}
			}

		}

		if(!possuiErro)
		{
				HashMap<String, Object> param =  new HashMap<>();
				param.put("_budgetId", String.valueOf(budget.getId()));
				bpmsTask.aprovarTarefa(facesUtils.getUserLogin(), idTarefa,param);
				facesUtils.addInfoMessage("Tarefa concluida com sucesso");
				conversation.end();
				return "minhasTarefas";

			}
		} catch (Exception e) {

			facesUtils.addErrorMessage("Erro ao concluir a tarefa");
			logger.error("Erro ao concluir a tarefa",e);
		}
		return null;
	}

	public BigDecimal calcularValorColuna(String mes)
	{
		BigDecimal valor = new BigDecimal(0d);
		if(despesas == null)
			return null;

		for(DespesaMesDTO despesa : despesas)
		{
			switch(mes)
			{
				case "JAN":
					if(despesa.getJaneiro() !=null)
						valor= valor.add(despesa.getJaneiro());
					break;
				case "FEV":
					if(despesa.getFevereiro() !=null)
						valor= valor.add(despesa.getFevereiro());
					break;

				case "MAR":
					if(despesa.getMarco()!=null)
						valor= valor.add(despesa.getMarco());
					break;

				case "ABR":
					if(despesa.getAbril() !=null)
						valor= valor.add(despesa.getAbril());
					break;

				case "MAI":
					if(despesa.getMaio() !=null)
						valor= valor.add(despesa.getMaio());
					break;

				case "JUN":
					if(despesa.getJunho() !=null)
						valor= valor.add(despesa.getJunho());
					break;

				case "JUL":
					if(despesa.getJulho() !=null)
						valor= valor.add(despesa.getJulho());
					break;

				case "AGO":
					if(despesa.getAgosto() !=null)
						valor= valor.add(despesa.getAgosto());
					break;

				case "SET":
					if(despesa.getSetembro() !=null)
						valor= valor.add(despesa.getSetembro());
					break;

				case "OUT":
					if(despesa.getOutubro() !=null)
						valor= valor.add(despesa.getOutubro());
					break;

				case "NOV":
					if(despesa.getNovembro() !=null)
						valor= valor.add(despesa.getNovembro());
					break;

				case "DEZ":
					if(despesa.getDezembro() !=null)
						valor= valor.add(despesa.getDezembro());
					break;

				default:
					valor =valor.add(calcularValorMensalisado(despesa));
					break;

			}
		}
		return valor;
	}


	public List<DespesaMesDTO> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<DespesaMesDTO> despesas) {
		this.despesas = despesas;
	}

	public CentroDeCustoDTO getCentroDeCusto() {
		return centroDeCusto;
	}

	public void setCentroDeCusto(CentroDeCustoDTO centroDeCusto) {
		this.centroDeCusto = centroDeCusto;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Long getIdInstanciaProcesso() {
		return idInstanciaProcesso;
	}

	public void setIdInstanciaProcesso(Long idInstanciaProcesso) {
		this.idInstanciaProcesso = idInstanciaProcesso;
	}

	public Long getIdTarefa() {
		return idTarefa;
	}

	public void setIdTarefa(Long idTarefa) {
		this.idTarefa = idTarefa;
	}

	public BigDecimal getValorTotalDetalhe() {
		return valorTotalDetalhe;
	}

	public void setValorTotalDetalhe(BigDecimal valorTotalDetalhe) {
		this.valorTotalDetalhe = valorTotalDetalhe;
	}




}
