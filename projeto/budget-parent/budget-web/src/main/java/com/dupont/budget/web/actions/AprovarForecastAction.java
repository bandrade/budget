package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named
@ConversationScoped
public class AprovarForecastAction extends ForecastAction implements Serializable{
	
	private String aprovacao;
	private static final String APROVACAO_SIM="S";
	private static final String APROVACAO_NAO="N";
	private Double valorAprovacao;
	
	public void tipoAprovacaoAlterada(){
		valorAprovacao = null;
	}
	
	@Override
	public String concluir() {
		if(APROVACAO_NAO.equals(aprovacao) &&  valorAprovacao!=null && valorAprovacao<=0d)
		{
			facesUtils.addErrorMessage("O valor do Forecast a ser aprovado deve ser positivo");
			return null;
		}
		params.put("aprovado",APROVACAO_SIM.equals(aprovacao));
		params.put("valorAprovado", valorAprovacao);
		
		return super.concluir();
	}
	
	public String getAprovacao() {
		return aprovacao;
	}
	public void setAprovacao(String aprovacao) {
		this.aprovacao = aprovacao;
	}
	public Double getValorAprovacao() {
		return valorAprovacao;
	}
	public void setValorAprovacao(Double valorAprovacao) {
		this.valorAprovacao = valorAprovacao;
	}
	
	

}
