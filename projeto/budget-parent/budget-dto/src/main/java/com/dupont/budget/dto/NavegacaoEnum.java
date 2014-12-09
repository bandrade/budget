package com.dupont.budget.dto;


public enum NavegacaoEnum {
	CRIAR_BUDGET("Criar Budget","criarBudget.xhtml");
	private String nomeTarefa; 
	private String pagina;
	NavegacaoEnum(String nomeTarefa,String pagina)
	{
		this.nomeTarefa = nomeTarefa;
		this.pagina = pagina;
	}
	public static String obterNavegacao(String nomeTarefa)
	{
		for(NavegacaoEnum navegacaoEnum :  NavegacaoEnum.values())
		{
			if (navegacaoEnum.nomeTarefa.equals(nomeTarefa)) {
				return navegacaoEnum.pagina;
			}
		}
		return null;
	}
	
}
