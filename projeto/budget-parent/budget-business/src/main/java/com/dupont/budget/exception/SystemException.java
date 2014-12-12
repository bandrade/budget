package com.dupont.budget.exception;

/**
 * Exceção de sistema do sistema. Todas as exceções de sistema, infraestrutura, etc, devem estendê-la.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = -3601267299705312420L;

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(String message) {
		super(message);
	}

	
}
