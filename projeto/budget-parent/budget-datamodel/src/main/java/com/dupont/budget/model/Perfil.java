package com.dupont.budget.model;

/**
 * Perfil de segurança do usuário na aplicação.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public enum Perfil {
	USUARIO("Usuário"), ADMINISTRADOR("Administrador");
	
	private String string;

	private Perfil(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
}
