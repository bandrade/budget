package com.dupont.budget.exception;

/**
 * Exceção de runtime do sistema. Todas as exceções de runtime devem estendê-la.
 * 
 * @author <a href="joel.santos@surittec.com.br">Joel Santos</a>
 * @since 2014
 *
 */
public class ApplicationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3329178241727087862L;

	public ApplicationRuntimeException(String message) {
		super(message);
	}

}
