package com.dupont.budget.web.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import com.dupont.budget.model.Fornecedor;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.EventDispatcherService;
import com.dupont.budget.service.event.FornecedorEvent;
import com.dupont.budget.web.util.FacesUtils;

/**
 * Controller das telas de manutenção da entidade {@link Fornecedor}
 * 
 * @author <a href="bandrade@redhat.com">Bruno Andrade</a>
 * @since 2014
 *
 */
@Model
@RolesAllowed(value = "ADMINISTRADOR")
public class FornecedorAction extends GenericAction<Fornecedor> {

	private static final long serialVersionUID = -9064126463852854590L;

	@Inject
	private DomainService service;

	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;
	
	@Inject
    private EventDispatcherService eventDispatcher;
	
	private UploadedFile file;

	@Named
	@Produces
	public Fornecedor getFornecedor() {
		return getEntidade();
	}
	
	@Override
	public String persist() {
		logger.debug("Arquivo recebido, iniciando processamento!");

		facesUtils.addInfoMessage("Arquivo recebido, e enviado para o processamento.");

		FornecedorEvent event = new FornecedorEvent();
		try {
			String name = file.getFileName();
			File tmp = File.createTempFile("dupont_", name.substring(name.lastIndexOf(".")));
			FileOutputStream fos = new FileOutputStream(tmp);
			fos.write(file.getContents());
			fos.close();
			event.setFilePath(tmp.getAbsolutePath());
		} catch (IOException e) {
			facesUtils.addErrorMessage("Não foi possível gravar o arquivo.");
		}
		eventDispatcher.publish(event);
		
		return "list";
	}
	
	/**
	 * Buscar as culturas a partir do filtro.
	 */
	public void find() {
		list = service.findByName(entidade);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected DomainService getService() {
		return service;
	}

	@Override
	protected FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}
