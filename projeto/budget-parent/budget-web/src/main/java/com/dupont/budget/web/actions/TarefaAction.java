package com.dupont.budget.web.actions;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.service.bpms.BPMSTaskService;

@Model
public class TarefaAction {
	@Inject
    private BPMSTaskService bpms;
	
	private String usuario;
	
	private List<TarefaDTO> tarefas;
	
	private TarefaDTO tarefaSelecionada;
	
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
