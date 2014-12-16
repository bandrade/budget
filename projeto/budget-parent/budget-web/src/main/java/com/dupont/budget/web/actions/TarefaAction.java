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
import com.dupont.budget.service.bpms.BPMSTaskService;
import com.dupont.budget.web.util.FacesUtils;

@ConversationScoped
@Named
public class TarefaAction implements Serializable {
	@Inject
	private BPMSTaskService bpms;

	private List<TarefaDTO> tarefas;

	private TarefaDTO tarefaSelecionada;

	@Inject
	private Conversation conversation;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	@PostConstruct
	public void init() {
		conversation.begin();
	}

	public void obterTarefasUsuario() {
		try {
			tarefas = bpms.obterTarefas(facesUtils.getUserLogin());
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter tarefas do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
	}

	public void aprovarTarefa() {

		System.out.println(tarefaSelecionada.getName());
		try {
			bpms.aprovarTarefa(facesUtils.getUserLogin(),
					tarefaSelecionada);
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao aprovar tarefa do usuario.");
			logger.error("Erro ao obter tarefas do usuario.", e);
		}
		conversation.end();
	}

	public List<TarefaDTO> getTarefas() {
		if (tarefas == null) {
			obterTarefasUsuario();
		}
		return tarefas;
	}

	public void setTarefas(List<TarefaDTO> tarefas) {
		this.tarefas = tarefas;
	}

	public TarefaDTO getTarefaSelecionada() {
		return tarefaSelecionada;
	}

	public void setTarefaSelecionada(TarefaDTO tarefaSelecionada) {
		this.tarefaSelecionada = tarefaSelecionada;
	}

}