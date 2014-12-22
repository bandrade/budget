package com.dupont.budget.service.event;

import java.io.Serializable;

/**
 * Evento de cadastro de fornecedores da aplicação.
 * 
 * @author joel
 *
 */
public class FornecedorEvent implements Serializable {

	private static final long serialVersionUID = -2454622597076066897L;

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
