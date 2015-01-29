package com.dupont.budget.bpm.custom.workitem;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.jbpm.process.workitem.email.EmailWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.dupont.budget.dto.EmailDTO;
import com.dupont.budget.service.jms.DupontMailSender;
@ApplicationScoped
@Named
public class DupontEmailWorkItemHandler extends EmailWorkItemHandler implements Serializable {
	
	@Override
	public void executeWorkItem(WorkItem workitem, WorkItemManager workItemManager) {
		EmailDTO email = new EmailDTO((String)workitem.getParameter("From"), (String)workitem.getParameter("To"), 
				(String)workitem.getParameter("Subject"), (String)workitem.getParameter("Body"));
		  Context context = null;
		    try {
		        context = new InitialContext();
		        DupontMailSender dupointMail = (DupontMailSender)context.lookup("java:global/budget/DupontMailSenderImpl!com.dupont.budget.service.jms.DupontMailSender");
		        dupointMail.sendMail(email);
		    } catch (Exception e)
		    {
		    	e.printStackTrace();
		    }
		 }
}
