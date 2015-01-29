package com.dupont.budget.service.jms.mdb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
@MessageDriven(name = "DupontMailMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/budgetMail"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class DupontMailMDB implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(DupontMailMDB.class.toString());
    
    @Resource(mappedName="java:jboss/mail/Default")
    private Session mailSession;
    
    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message rcvMessage) {
        try {
            
        	MimeMessage m = new MimeMessage(mailSession);
        	String fromS= rcvMessage.getStringProperty("From");
            Address from = new InternetAddress(fromS);
            String toS = rcvMessage.getStringProperty("To");
            String content= rcvMessage.getStringProperty("Content");
            String subject= rcvMessage.getStringProperty("Subject");

            String[] toEmails = toS.split(",");
            Address[] to = new InternetAddress[toEmails.length] ;
           
            for(int count=0;count< toEmails.length;count++)
            {
            	to[count] = new InternetAddress(toEmails[count]);
            }
            
            m.setFrom(from);
            m.setRecipients(javax.mail.Message.RecipientType.TO, to);
            m.setSubject(subject);
            m.setSentDate(new java.util.Date());
            m.setContent(content,"text/plain");
            Transport.send(m);
        } catch (Exception e) {
        	LOGGER.log(Level.WARNING, "Erro ao enviar email ",e);
            throw new RuntimeException(e);
        }
    }
}
