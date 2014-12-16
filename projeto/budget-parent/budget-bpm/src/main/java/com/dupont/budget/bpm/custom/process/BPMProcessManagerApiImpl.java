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

package com.dupont.budget.bpm.custom.process;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jbpm.process.workitem.rest.RESTWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.runtime.manager.context.EmptyContext;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.bpm.custom.workitem.DupontEmailWorkItemHandler;

@ApplicationScoped
@TransactionManagement(TransactionManagementType.BEAN)
public class BPMProcessManagerApiImpl {

	@Inject
	@Singleton
	private RuntimeManager singletonManager;

	@Resource
	private UserTransaction ut;

	@PostConstruct
	private void configure() {
		// use toString to make sure CDI initializes the bean
		// this makes sure that RuntimeManager is started asap,
		// otherwise after server restart complete task won't move process
		// forward
		singletonManager.toString();
		RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext
				.get());

		runtime.getKieSession()
				.getWorkItemManager()
				.registerWorkItemHandler("Email",
						new DupontEmailWorkItemHandler());
		runtime.getKieSession().getWorkItemManager()
				.registerWorkItemHandler("Rest", new RESTWorkItemHandler());
	}

	public long startBudgetProcess() throws Exception {
		RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext
				.get());

		KieSession ksession = runtime.getKieSession();

		long processInstanceId = -1;

		try {
			ut.begin();
			// start a new process instance
			Map<String, Object> params = new HashMap<String, Object>();
			ProcessInstance processInstance = ksession.startProcess(
			/* "budget.EnvioEmail" */"com.dupont.bpm.criarbudget", params);
			processInstanceId = processInstance.getId();
			ut.commit();

		} catch (Exception e) {
			if (ut.getStatus() == Status.STATUS_ACTIVE) {
				ut.rollback();
			}
			throw new RuntimeException(e);
		}
		return processInstanceId;
	}

	public void abortProcess(long processInstanceId) {
		RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext
				.get());
		KieSession ksession = runtime.getKieSession();
		ksession.abortProcessInstance(processInstanceId);

	}

	public Object getProcessVariable(long processInstanceId, String variable)
			throws BPMException {
		try {
			RuntimeEngine runtime = singletonManager
					.getRuntimeEngine(EmptyContext.get());
			KieSession ksession = runtime.getKieSession();
			WorkflowProcessInstance process = ((WorkflowProcessInstance) ksession
					.getProcessInstance(processInstanceId));
			return process.getVariable(variable);
		} catch (Exception e) {
			throw new BPMException(e);
		}

	}
}