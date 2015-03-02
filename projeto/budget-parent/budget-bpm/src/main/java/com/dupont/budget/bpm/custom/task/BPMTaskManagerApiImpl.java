/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dupont.budget.bpm.custom.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.UserTransaction;

import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.task.api.InternalTaskService;

import com.dupont.budget.bpm.custom.exception.BPMException;

@Stateless
public class BPMTaskManagerApiImpl implements BPMTaskManagerApi, Serializable {
	@Inject
	TaskService taskService;

	public List<TaskSummary> retrieveTaskList(String actorId,List<String> grupos)
			throws BPMException {
		try {
			List<TaskSummary> tasks  = taskService.getTasksAssignedAsPotentialOwner(actorId,"en-UK");
			return tasks;
		} catch (Exception e) {
			throw new BPMException("Erro ao obter a tarefas", e);
		}

	}
	public List<TaskSummary> retrieveTaskListAdm()
			throws BPMException {
		List<TaskSummary> tasksReady = new ArrayList<>();
		try {
			List<TaskSummary> tasks = taskService.getTasksAssignedAsBusinessAdministrator("Administrator","en-UK");
			for(TaskSummary task : tasks)
			{
				if(task.getStatus().equals(Status.Ready))
					tasksReady.add(task);
			}
			return tasksReady;
		} catch (Exception e) {
			throw new BPMException("Erro ao obter a tarefas", e);
		}

	}

	public List<TaskSummary> retrieveTaskListAdmCompletas()
			throws BPMException {
		List<TaskSummary> tasksReady = new ArrayList<>();
		try {
			List<TaskSummary> tasks = taskService.getTasksAssignedAsBusinessAdministrator("Administrator","en-UK");
			for(TaskSummary task : tasks)
			{
				if(task.getStatus().equals(Status.Completed))
					tasksReady.add(task);
			}
			return tasksReady;
		} catch (Exception e) {
			throw new BPMException("Erro ao obter a tarefas", e);
		}

	}


	public synchronized void aproveTask(String actorId, long taskId,
			Map<String, Object> content) throws Exception {
		try {
			Task task = getTask(taskId);
			if(task.getTaskData().getStatus().equals(Status.Ready))
				taskService.start(taskId, actorId);
			taskService.complete(taskId, actorId, content);
		} catch (Exception e) {
			throw new BPMException("Erro ao aprovar a tarefa", e);
		}
	}

	public Task getTask(long taskId) throws BPMException {
		try {
			Task task = taskService.getTaskById(taskId);
			return task;
		} catch (Exception e) {
			throw new BPMException("Erro ao obter a tarefa", e);
		}
	}

	public Map<String, Object> getTaskContent(long taskId) throws BPMException {
		try {
			Map<String, Object> content = ((InternalTaskService) taskService)
					.getTaskContent(taskId);
			return content;
		} catch (Exception e) {
			throw new BPMException("Erro ao obter o conteudo da tarefa", e);
		}
	}

	public void claimTask(String userId, long taskId) throws BPMException {
		try {
			taskService.claim(taskId, userId);
		} catch (Exception e) {
			throw new BPMException("Erro ao nomear a tarefa", e);
		}
	}

	public void startTask(String userId, long taskId) throws BPMException {
		try {
			taskService.start(taskId, userId);
		} catch (Exception e) {
			throw new BPMException("Erro iniciar a tarefa", e);
		}
	}

	public void forwardTask(String userId, String targetUserId, long taskId)
			throws BPMException {
		try {
			taskService.forward(taskId, userId, targetUserId);
		} catch (Exception e) {
			throw new BPMException("Erro a iniciar a tarefa", e);
		}
	}

	public void releaseTask(String userId, long taskId) throws BPMException {
		try {
			taskService.release(taskId, userId);
		} catch (Exception e) {
			throw new BPMException("Erro ao liberar a tarefa", e);
		}
	}

	public void stopTask(String userId, long taskId) throws BPMException {
		try {
			taskService.stop(taskId, userId);
		} catch (Exception e) {
			throw new BPMException("Erro ao pausar a tarefa", e);
		}
	}

	public void resumeTask(String userId, long taskId) throws BPMException {
		try {
			taskService.resume(taskId, userId);
		} catch (Exception e) {
			throw new BPMException("Erro ao obter o conteudo da tarefa", e);
		}
	}

	public void skipTask(String userId, long taskId) throws BPMException {
		try {
			taskService.skip(taskId, userId);
		} catch (Exception e) {
			throw new BPMException("Erro pular a tarefa", e);
		}

	}

	public void delegateTask(String userId, String targetUserId, long taskId)
			throws BPMException {
		try {
			taskService.delegate(taskId, userId, targetUserId);
		} catch (Exception e) {
			throw new BPMException("Erro ao delegar a tarefa", e);
		}

	}

}
