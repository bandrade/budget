package com.dupont.budget.service.bpms;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.kie.api.task.model.TaskSummary;

import com.dupont.budget.bpm.custom.task.BPMTaskManagerApiImpl;
import com.dupont.budget.dto.StatusTarefaEnum;
import com.dupont.budget.dto.TarefaDTO;

@Model
public class BPMSTaskServiceImpl implements BPMSTaskService {

	@Inject
	private BPMTaskManagerApiImpl taskApi;

	public List<TarefaDTO> obterTarefas(String user) throws Exception {
		List<TaskSummary> tasks = taskApi.retrieveTaskList(user);
		List<TarefaDTO> tarefas = new ArrayList<TarefaDTO>();
		for (TaskSummary task : tasks) {
			TarefaDTO tarefaDTO = new TarefaDTO();
			tarefaDTO.setActivationTime(task.getActivationTime());
			tarefaDTO.setActualOwner(task.getActualOwner() != null ? task
					.getActualOwner().getId() : null);
			tarefaDTO.setCreatedBy(task.getCreatedBy() != null ? task
					.getCreatedBy().getId() : null);
			tarefaDTO.setCreatedOn(task.getCreatedOn());
			tarefaDTO.setDescription(taskApi.getTaskContent(task.getId()).get("Subject").toString());
			tarefaDTO.setExpirationTime(task.getExpirationTime());
			tarefaDTO.setId(task.getId());
			tarefaDTO.setName(task.getName());
			tarefaDTO.setPotentialOwners(task.getPotentialOwners());
			tarefaDTO.setPriority(task.getPriority());
			tarefaDTO.setProcessInstanceId(task.getProcessInstanceId());
			tarefaDTO.setProcessId(task.getProcessId());
			tarefaDTO.setProcessSessionId(task.getProcessSessionId());
			tarefaDTO.setSkipable(task.isSkipable());
			tarefaDTO.setStatus(StatusTarefaEnum.findByMeaning(task.getStatus()
					.name()));
			tarefas.add(tarefaDTO);
		}
		return tarefas;
	}

	public boolean aprovarTarefa(String usuario, TarefaDTO tarefa) throws Exception {
		taskApi.aproveTask(usuario, tarefa.getId(), null);

		return true;
	}

}
