package com.dupont.budget.exception;

/**
 * Exceção de aplicação do sistema. Todas as exceções de negocio devem estendê-la.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public class ApplicationException extends Exception {

	private static final long serialVersionUID = -3329178241727087862L;

	public ApplicationException(String message) {
		super(message);
	}

}
