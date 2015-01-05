package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
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

	private static final long serialVersionUID = -554836510005741119L;

	@Inject
	private BPMSTaskService bpms;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;
	
	private TarefaDTO tarefaSelecionada;

	public List<TarefaDTO> getTarefas() {
		 List<TarefaDTO> tarefas = null;
		try {
			//tarefas = bpms.obterTarefas(facesUtils.getUserLogin());
			TarefaDTO dto = new TarefaDTO();
			dto.setId(1L);
			dto.setProcessInstanceId(2L);
			dto.setDescription("Tarefa de Testes");
			dto.setName("Detalhar Despesa");
			dto.setCreatedOn(new Date());
			tarefas = new LinkedList<>();
			tarefas.add(dto);
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