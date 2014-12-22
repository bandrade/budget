package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;

@SessionScoped
@Named
public class TarefaAction implements Serializable {
	@Inject
	private BPMSTaskService bpms;


	private TarefaDTO tarefaSelecionada;


	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	public List<TarefaDTO> getTarefas() {
		 List<TarefaDTO> tarefas = null;
		try {
			tarefas = bpms.obterTarefas(facesUtils.getUserLogin());
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
		return tarefas;
	}


	public TarefaDTO getTarefaSelecionada() {
		return tarefaSelecionada;
	}

	public void setTarefaSelecionada(TarefaDTO tarefaSelecionada) {
		this.tarefaSelecionada = tarefaSelecionada;
	}

}