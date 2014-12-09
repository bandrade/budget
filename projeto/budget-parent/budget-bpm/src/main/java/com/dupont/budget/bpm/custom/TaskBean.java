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

package com.dupont.budget.bpm.custom;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.task.api.InternalTaskService;

@ApplicationScoped
public class TaskBean {


    @Inject
    TaskService taskService;


    public List<TaskSummary> retrieveTaskList(String actorId){
        return taskService.getTasksAssignedAsPotentialOwner(actorId, "en-UK");
    }

    public void aproveTask(String actorId, long taskId, Map<String,Object> content)  {
            taskService.start(taskId, actorId);
            taskService.complete(taskId, actorId, content);
    }

    public Task getTask(long taskId)  {
        return taskService.getTaskById(taskId);
    }

    
    public Map<String,Object> getTaskContent(long taskId) {
    	Map<String,Object> content = ((InternalTaskService) taskService).getTaskContent(taskId);
        return content;
    }

}
