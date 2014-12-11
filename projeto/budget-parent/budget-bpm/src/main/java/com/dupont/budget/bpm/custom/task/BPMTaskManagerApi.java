package com.dupont.budget.bpm.custom.task;

import java.util.List;
import java.util.Map;

import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;

import com.dupont.budget.bpm.custom.exception.BPMException;

public interface BPMTaskManagerApi {
	List<TaskSummary> retrieveTaskList(String actorId) throws BPMException;
	void aproveTask(String actorId, long taskId, Map<String,Object> content) throws BPMException ;
	Task getTask(long taskId)  throws BPMException;
	Map<String,Object> getTaskContent(long taskId) throws BPMException;
	
	
	
}
