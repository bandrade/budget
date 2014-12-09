package com.dupont.budget.web;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.service.bpms.BPMSTaskService;
@Model
public class TarefaController {
	@Inject
    private BPMSTaskService bpms;
	
	private String usuario;
	
	private List<TarefaDTO> tarefas;
	
	public void obterTarefasUsuario()
	{
		tarefas = bpms.obterTarefas(usuario);
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

}
