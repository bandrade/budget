package com.dupont.budget.web.actions;

import java.io.Serializable;
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

	private Double valorTotalDetalhe;


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
	public Double calcularValorMensalisado(DespesaMesDTO despesa)
	{
		Double valor = 0d;
		valor+= despesa.getJaneiro() !=null ? despesa.getJaneiro() :0d;
		valor+= despesa.getFevereiro() !=null ? despesa.getFevereiro() : 0d;
		valor+=despesa.getMarco() !=null ? despesa.getMarco():0d ;
		valor+=despesa.getAbril() !=null ? despesa.getAbril() :0d;
		valor+=despesa.getMaio() !=null ? despesa.getMaio() : 0d;
		valor+=despesa.getJunho() !=null ? despesa.getJunho() :0d ;
		valor+=despesa.getJulho() !=null ? despesa.getJulho() : 0d;
		valor+=despesa.getAgosto()  !=null ? despesa.getAgosto() : 0d;
		valor+=despesa.getSetembro() !=null ? despesa.getSetembro() : 0d;
		valor+=despesa.getOutubro() !=null ? despesa.getOutubro() : 0d;
		valor+=despesa.getNovembro() !=null ? despesa.getNovembro() :0d;
		valor+=despesa.getDezembro() !=null ? despesa.getDezembro() : 0d;

		return valor;
	}
	public void calcularValorTotalDetalhe()
	{
		valorTotalDetalhe = 0d;
		for(DespesaMesDTO despesa : despesas)
		{
			valorTotalDetalhe += despesa.getValor();
		}
	}

	public String concluir()
	{
		try {
		budgetService.mensalisarBudget(despesas);
		boolean possuiErro = false ;
		if(!calcularValorColuna("TOT").equals(getValorTotalDetalhe()))
		{
			facesUtils.addErrorMessage("O valor total do budget mensalisado deve ser igual ao valor total aprovado");
			possuiErro = true;
		}
		else
		{
			for(DespesaMesDTO despesa : despesas)
			{
				Double valorTotalMensalisado = calcularValorMensalisado(despesa);
				logger.info("Valor despesa" + despesa.getValor() +"- Valor Mensalisado: " + valorTotalMensalisado);

				if(!(Double.compare(despesa.getValor(), Math.floor(valorTotalMensalisado))==0))
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

	public Double calcularValorColuna(String mes)
	{
		Double valor = 0d;
		if(despesas == null)
			return null;

		for(DespesaMesDTO despesa : despesas)
		{
			switch(mes)
			{
				case "JAN":
					valor+=despesa.getJaneiro() !=null ? despesa.getJaneiro() :0d;
					break;
				case "FEV":
					valor+=despesa.getFevereiro() !=null ? despesa.getFevereiro() : 0d;
					break;

				case "MAR":
					valor+=despesa.getMarco() !=null ? despesa.getMarco():0d ;
					break;

				case "ABR":
					valor+=despesa.getAbril() !=null ? despesa.getAbril() :0d;
					break;

				case "MAI":
					valor+=despesa.getMaio() !=null ? despesa.getMaio() : 0d;
					break;

				case "JUN":
					valor+=despesa.getJunho() !=null ? despesa.getJunho() :0d ;
					break;

				case "JUL":
					valor+=despesa.getJulho() !=null ? despesa.getJulho() : 0d;
					break;

				case "AGO":
					valor+=despesa.getAgosto()  !=null ? despesa.getAgosto() : 0d;
					break;

				case "SET":
					valor+=despesa.getSetembro() !=null ? despesa.getSetembro() : 0d;
					break;

				case "OUT":
					valor+=despesa.getOutubro() !=null ? despesa.getOutubro() : 0d;
					break;

				case "NOV":
					valor+=despesa.getNovembro() !=null ? despesa.getNovembro() :0d;
					break;

				case "DEZ":
					valor+=despesa.getDezembro() !=null ? despesa.getDezembro() : 0d;
					break;

				default:
					valor+=calcularValorMensalisado(despesa);
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

	public Double getValorTotalDetalhe() {
		return valorTotalDetalhe;
	}

	public void setValorTotalDetalhe(Double valorTotalDetalhe) {
		this.valorTotalDetalhe = valorTotalDetalhe;
	}


}
