package com.dupont.budget.service.bpms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.NotImplementedException;
import org.kie.api.task.model.TaskSummary;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.bpm.custom.task.BPMTaskManagerApiImpl;
import com.dupont.budget.dto.StatusTarefaEnum;
import com.dupont.budget.dto.TarefaDTO;

@Stateless
public class BPMSTaskServiceImpl implements BPMSTaskService {

	@Inject
	private BPMTaskManagerApiImpl taskApi;

	public List<TarefaDTO> obterTarefas(String user) throws Exception {
		List<TaskSummary> tasks = taskApi.retrieveTaskList(user);
		List<TarefaDTO> tarefas = new ArrayList<TarefaDTO>();
		for (TaskSummary task : tasks) {
			TarefaDTO tarefaDTO = parseTaskToTarefa(task);
			tarefas.add(tarefaDTO);
		}
		return tarefas;
	}

	public void aprovarTarefa(String usuario, Long taskId, Map<String, Object> params ) throws Exception {

		taskApi.aproveTask(usuario, taskId, params);
	}

	@Override
	public TarefaDTO obterTarefaPorId(Long taskId) throws BPMException {
		//TODO IMPLEMENTAR
		throw new NotImplementedException("Metodo nao implementado");
	}


	private TarefaDTO parseTaskToTarefa(TaskSummary task) throws BPMException
	{
		TarefaDTO tarefaDTO = new TarefaDTO();
		tarefaDTO.setActivationTime(task.getActivationTime());
		tarefaDTO.setActualOwner(task.getActualOwner() != null ? task
				.getActualOwner().getId() : null);
		tarefaDTO.setCreatedBy(task.getCreatedBy() != null ? task
				.getCreatedBy().getId() : null);
		tarefaDTO.setCreatedOn(task.getCreatedOn());
		String subject = String.valueOf(taskApi.getTaskContent(task.getId()).get("Subject"));
		tarefaDTO.setDescription(subject ==null? "":subject);
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

		return tarefaDTO;

	}
}
