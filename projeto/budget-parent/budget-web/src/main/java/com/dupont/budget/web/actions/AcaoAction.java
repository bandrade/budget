package com.dupont.budget.web.actions;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.dupont.budget.model.Acao;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.web.util.FacesUtils;


public class AcaoAction extends GenericAction<Acao>{
	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	public DomainService getService() {
		return service;
	}

	public void setService(DomainService service) {
		this.service = service;
	}


	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}



	public FacesUtils getFacesUtils() {
		return facesUtils;
	}



	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

	@Override
	public void find() {
		// TODO Auto-generated method stub

	}

}
