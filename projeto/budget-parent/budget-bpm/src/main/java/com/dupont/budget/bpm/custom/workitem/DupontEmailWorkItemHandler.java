package com.dupont.budget.bpm.custom.workitem;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jbpm.process.workitem.email.EmailWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.dupont.budget.dto.EmailDTO;
import com.dupont.budget.service.jms.DupontMailSender;
@ApplicationScoped
@Named
public class DupontEmailWorkItemHandler extends EmailWorkItemHandler implements Serializable {
	   private final static Logger LOGGER = Logger.getLogger(DupontEmailWorkItemHandler.class.toString());
	@Override
	public void executeWorkItem(WorkItem workitem, WorkItemManager workItemManager) {
		EmailDTO email = new EmailDTO((String)workitem.getParameter("From"), (String)workitem.getParameter("To"), 
				(String)workitem.getParameter("Subject"),StringEscapeUtils.escapeHtml4((String)workitem.getParameter("Body")));
		  Context context = null;
		    try {
		        context = new InitialContext();
		        DupontMailSender dupointMail = (DupontMailSender)context.lookup("java:global/budget/DupontMailSenderImpl!com.dupont.budget.service.jms.DupontMailSender");
		        dupointMail.sendMail(email);
		        workItemManager.completeWorkItem(workitem.getId(), null);
		    } catch (Exception e)
		    {
		    	LOGGER.log(Level.WARNING,"Erro ao enviar o email ",e);
		    }
		    
		 }
}
