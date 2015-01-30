package com.dupont.budget.service.jms;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.dupont.budget.dto.EmailDTO;
import com.dupont.budget.service.jms.mdb.DupontMailMDB;

@Stateless
public class DupontMailSenderImpl implements DupontMailSender,Serializable {
	
		@Resource(mappedName = "java:/ConnectionFactory")
	    private ConnectionFactory connectionFactory;

	    @Resource(mappedName = "java:/queue/budgetMail")
	    private Queue queue;
	    
	    private final static Logger LOGGER = Logger.getLogger(DupontMailMDB.class.toString());
	    
	    public void sendMail(EmailDTO emailDTO)
	    {
	    	
	    	Connection connection = null;
	    	try
	    	{
		    	Destination destination = queue;
		    	connection = connectionFactory.createConnection();
		        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		        MessageProducer messageProducer = session.createProducer(destination);
		        connection.start();
		        TextMessage message = session.createTextMessage();
		        message.setStringProperty("To", emailDTO.getTo());
		        message.setStringProperty("From", emailDTO.getFrom());
		        message.setStringProperty("Content", emailDTO.getContent());
		        message.setStringProperty("Subject", emailDTO.getSubject());
	            messageProducer.send(message);
	    	}
	    	catch(Exception e)
	    	{
	    		LOGGER.log(Level.WARNING, "Erro ao enviar o email ",e);
	    	}
	    	finally
	    	{
	    		try {
	    			if(connection !=null)
						connection.close();
					} catch (JMSException e) {
				   		LOGGER.log(Level.WARNING, "Erro ao fechar a conexao ",e);
					}
	    	}
	            
	    }
}
