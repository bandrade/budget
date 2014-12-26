package com.dupont.budget.service.bpms;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.dto.TarefaDTO;
@Local
public interface BPMSTaskService {
	List<TarefaDTO> obterTarefas(String user) throws Exception;
	void aprovarTarefa(String user, Long taskId,Map<String, Object> params) throws Exception;
	TarefaDTO obterTarefaPorId(Long id) throws BPMException;
	Map<String,Object> obterConteudoTarefa(long taskId) throws BPMException;

}
