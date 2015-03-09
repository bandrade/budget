package com.dupont.budget.bpm.custom.task;

import java.util.List;
import java.util.Map;

import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.dto.ProcessType;

public interface BPMTaskManagerApi {
	public List<TaskSummary> retrieveTaskList(String actorId,List<String> grupos) throws BPMException;
	void aproveTask(String actorId, long taskId, Map<String,Object> content) throws Exception ;
	Task getTask(long taskId)  throws BPMException;
	Map<String,Object> getTaskContent(long taskId) throws BPMException;
	public List<TaskSummary> retrieveTaskListAdm() throws BPMException ;
	public List<TaskSummary> retrieveTaskListAdmCompletas() throws BPMException ;
	public List<TaskSummary> retrieveTaskListFromGroups(List<String> groups,ProcessType type) throws BPMException ;


}
