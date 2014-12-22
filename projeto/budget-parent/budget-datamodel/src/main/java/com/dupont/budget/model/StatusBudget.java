package com.dupont.budget.model;

/**
 * Perfil de segurança do usuário na aplicação.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public enum StatusBudget {
	INICIADO("INICIADO"), SUBMETIDO("SUBMETIDO"),FINALIZADO("FINALIZADO") ;

	private String string;

	private StatusBudget(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return this.string;
	}
}
