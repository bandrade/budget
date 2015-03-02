package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;

@Named
@ConversationScoped
public class StatusAction implements Serializable {
	@Inject
	protected Conversation conversation;


	@Inject
    private BPMSProcessService bpms;

	@Inject
	protected BPMSTaskService bpmsTask;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	private List<TarefaDTO> tarefas;

	private List<TarefaDTO> tarefasCompletadas;

	@PostConstruct
	private void init()
	{
		if(conversation.isTransient())
			conversation.begin();
	}

	public List<TarefaDTO> getTarefas() {

		try {
			if(tarefas == null)
				tarefas = bpmsTask.obterTarefasAdm();
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
		return tarefas;
	}

	public List<TarefaDTO> getTarefasCompletadas() {

		try {
			if(tarefasCompletadas == null)
				tarefasCompletadas = bpmsTask.obterTarefasAdmCompletas();
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
		return tarefasCompletadas;
	}




}
