package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;


@Named
@ConversationScoped
public class AjustarForecastAction extends AtualizarForecastAction implements Serializable{
	private Double valorTolerancia;
	
	@Override
	public void obterDadosForecast() {
		super.obterDadosForecast();
		try {
			valorTolerancia =(Double) bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "valorTolerancia");
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter informações da tarefa");
			logger.error("Erro ao obter as informacoes da tarefa",e);
		}
		
	}
	public String concluir() {
	
		if(!valorTolerancia.equals(super.calcularTotalAno()) )
		{
			facesUtils.addErrorMessage("Valor total ano é diferente do valor de tolerância");
			return null;
		}
		
		return super.concluir();
	}


	public Double getValorTolerancia() {
		return valorTolerancia;
	}


	public void setValorTolerancia(Double valorTolerancia) {
		this.valorTolerancia = valorTolerancia;
	}
	
	

}
