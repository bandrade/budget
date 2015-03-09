package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.ProcessType;
import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.service.centrodecusto.CentroDeCustoService;
import com.dupont.budget.web.util.FacesUtils;

@Named
@SessionScoped
public class StatusForecastAction implements Serializable {

	@Inject
	protected BPMSTaskService bpmsTask;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	private List<TarefaDTO> tarefas;


	private Area areaSelecionada;

	@Inject
	private DomainService domainService;

	private CentroCusto centroCustoSelecionado;

	private List<Area> areaList;

	private List<CentroCusto> centroCustoList;

	@Inject
	private CentroDeCustoService ccService;


	public String inicializarTela(String tela)
	{
		tarefas = null;
		centroCustoList = null;
		centroCustoSelecionado = null;
		areaSelecionada = null;
		pesquisar();
		return tela;
	}
	public void pesquisar()
	{

		try {
			tarefas = bpmsTask.obterTarefasProcesso(areaSelecionada, centroCustoSelecionado, ProcessType.FORECAST);
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
	}


	public void doSelectCentroDeCusto()
	{
		try {
			centroCustoList = ccService.findByArea(areaSelecionada.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<TarefaDTO> getTarefas() {

		return tarefas;
	}

	public List<CentroCusto> autoCompleteCentroCusto(String input)
	{
		return facesUtils.autoCompleteCC(centroCustoList,input);
	}

	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public CentroCusto getCentroCustoSelecionado() {
		return centroCustoSelecionado;
	}

	public void setCentroCustoSelecionado(CentroCusto centroCustoSelecionado) {
		this.centroCustoSelecionado = centroCustoSelecionado;
	}

	public List<Area> getAreaList() {
		if(areaList == null)
		{
			areaList = domainService.findAll(Area.class);
		}
		return areaList;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}

	public List<CentroCusto> getCentroCustoList() {
		return centroCustoList;
	}

	public void setCentroCustoList(List<CentroCusto> centroCustoList) {
		this.centroCustoList = centroCustoList;
	}

}
