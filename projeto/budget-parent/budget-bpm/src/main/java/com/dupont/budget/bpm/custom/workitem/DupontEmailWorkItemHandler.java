package com.dupont.budget.bpm.custom.workitem;

import org.jbpm.process.workitem.email.EmailWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class DupontEmailWorkItemHandler extends EmailWorkItemHandler {

	/*
	 *TODO IMPLEMENTAR COM SMTP DA DUPONT
	 * mail.smtp.host=smtp.gmail.com
	 * mail.smtp.port=587
	 * mail.smtp.user=dupontbpm@gmail.com
	 * mail.smtp.password=admin123@
	 */
	public DupontEmailWorkItemHandler() {
		/*
		String smtpHost =  System.getProperty("mail.smtp.host");
		String smtpPort =  System.getProperty("mail.smtp.port");
		String user =  System.getProperty("mail.smtp.user");
		String password =  System.getProperty("mail.smtp.password");*/
		String smtpHost = "smtp.gmail.com";
		String smtpPort =  "587";
		String user =  "dupontbpm@gmail.com";
		String password =  "admin123@";

		setConnection(smtpHost,smtpPort,user,password);
		getConnection().setStartTls(true);
	}

	@Override
	public void executeWorkItem(WorkItem workitem, WorkItemManager workItemManager) {


		// TODO Auto-generated method stub
		super.executeWorkItem(workitem, workItemManager);
	}

}
