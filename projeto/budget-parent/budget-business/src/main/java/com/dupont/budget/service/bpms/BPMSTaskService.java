package com.dupont.budget.service.bpms;

import java.util.List;

import com.dupont.budget.dto.TarefaDTO;

public interface BPMSTaskService {
	List<TarefaDTO> obterTarefas(String user);
	
}