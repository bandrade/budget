package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named
@ConversationScoped
public class AtualizarForecastAction extends ForecastAction implements Serializable{

	@Override
	public void obterDadosForecast() {
		super.obterDadosForecast();
	}

}
