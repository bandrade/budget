package com.dupont.budget.model;

/**
 * Tipo do Centro de Custo.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
public enum TipoCentroCusto {
	LOCAL("Local"), GLOBAL("Global");
	
	private String string;
	
	private TipoCentroCusto(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
}
