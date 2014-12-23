package com.dupont.budget.service.event;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Evento de envio de arquivos para o tratamento da aplicação.
 * 
 * @author joel
 *
 */
@SuppressWarnings("all")
public class FileUploadEvent extends AnnotationLiteral<Uploaded> implements Uploaded {

	private static final long serialVersionUID = -2454622597076066897L;

	private String path;
	
	private final Class<?> type;
	
	public FileUploadEvent(Class<?> type) {
		this.type = type;
	}

	/**
	 * Retorna o tipo da classe do evento.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<?> value() {
		return type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
