package com.dupont.budget.web.actions;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.service.bpms.BPMSTaskService;
@Named
@ConversationScoped
public class TarefaController implements Serializable {

	private static final long serialVersionUID = 4320697002989400101L;
	@Inject
	private Conversation conversation;
	@Inject
    private BPMSTaskService bpms;
	
	private String usuario;
	
	private List<TarefaDTO> tarefas;
	
	private TarefaDTO tarefaSelecionada;
	@PostConstruct
	public void init(){
		conversation.begin();
	}
	
	public void obterTarefasUsuario()
	{
		try
		{	
			tarefas = bpms.obterTarefas(usuario);
		}
		catch(Exception e)
		{
			//TODO TRATAMENTO VIEW
		}
	}
	
	public void aprovarTarefa()
	{
		System.out.println(tarefaSelecionada.getActualOwner());
		conversation.end();
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<TarefaDTO> getTarefas() {
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
