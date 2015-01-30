package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.MesEnum;

@Named
@ConversationScoped
public class AtualizarForecastAction extends ForecastAction implements Serializable{

	@Override
	public void obterDadosForecast() {
		super.obterDadosForecast();
	}


	private void atualizarDespesas() throws Exception{
		for(DespesaForecast despesaF : despesasNoDetalhe)
		{
			if(despesaF.isAlterada())
			{
					forecastService.atualizarDespesaForecast(despesaF);
			}
		}
	}
	@Override
	public String concluir() {
		String idForecast=null;
		if(!validarDespesas())
		{
			return null;
		}

		if(despesasNoDetalhe !=null && despesasNoDetalhe.size()>0)
		{
			idForecast = String.valueOf(despesasNoDetalhe.get(0).getForecast().getId());
		}
		try
		{
			atualizarDespesas();
		}
		catch(Exception e)
		{
			facesUtils.addErrorMessage("Erro ao concluir a tarefa");
			logger.error("Erro ao concluir a tarefa ",e );
		}
		MesEnum mesEnum = MesEnum.valueOf(mes.toUpperCase());
		if(mesEnum.equals(MesEnum.JANEIRO))
		{
			params.put("valorTotalForecastAtual",String.valueOf(super.calcularTotalBudget()));
		}
		else
		{
			params.put("valorTotalForecastAtual",String.valueOf(super.calcularTotalPLM()));
		}
		params.put("valorTotalForecastAtualizado",String.valueOf(super.calcularTotalAno()));
		params.put("idForecastRet", idForecast);
		return super.concluir();
	}

	public Boolean validarDespesas()
	{
		for(DespesaForecast desp : despesasNoDetalhe)
		{
			if(desp.getDespesaMensalisada().isValorComprometidoMaiorQueForecast())
			{
				facesUtils.addErrorMessage("A despesa " +desp.getTipoDespesa().getNome() +" - "+desp.getAcao().getNome() + " possui um "
						+ "valor comprometido maior que o valor planejado");
				return false;
			}
		}
		return true;
	}


}
