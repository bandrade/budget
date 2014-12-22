package com.dupont.budget.service.impl;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.dupont.budget.service.EventDispatcherService;
import com.dupont.budget.service.event.FornecedorEvent;

/**
 * Bean responsável pelo tratamento de eventos assíncronos da aplicação
 * 
 * @author joel
 *
 */
@Stateless
public class EventDispatcherServiceBean implements EventDispatcherService {

	@Inject
	private Logger logger;
	
	@Inject
    private Event<FornecedorEvent> eventManager;
 
	/*
	 * (non-Javadoc)
	 * @see com.dupont.budget.service.EventDispatcherService#publish(com.dupont.budget.service.event.FornecedorEvent)
	 */
	@Override
    @Asynchronous
    public void publish(FornecedorEvent event) {
        logger.debug("Enviando evento de cadastro de fornecedores");
        eventManager.fire(event);
    }
}
