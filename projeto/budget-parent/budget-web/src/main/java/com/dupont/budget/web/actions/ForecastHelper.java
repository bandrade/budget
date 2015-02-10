package com.dupont.budget.web.actions;

import java.util.List;

import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.MesEnum;

public class ForecastHelper {
	public Double calcularValorMensalisado(String mes,DespesaForecast despesa)
	{

		Double valor = 0d;
		if(despesa!=null)
		{
			if(exibirColuna(MesEnum.JANEIRO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getJaneiro());
			if(exibirColuna(MesEnum.FEVEREIRO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getFevereiro());
			if(exibirColuna(MesEnum.MARCO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getMarco());
			if(exibirColuna(MesEnum.ABRIL.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getAbril());
			if(exibirColuna(MesEnum.MAIO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getMaio());
			if(exibirColuna(MesEnum.JUNHO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getJunho());
			if(exibirColuna(MesEnum.JULHO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getJulho());
			if(exibirColuna(MesEnum.AGOSTO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getAgosto());
			if(exibirColuna(MesEnum.SETEMBRO.toString(), mes))	
				valor+= getDouble(despesa.getDespesaMensalisada().getSetembro());
			if(exibirColuna(MesEnum.OUTUBRO.toString(), mes))	
				valor+= getDouble(despesa.getDespesaMensalisada().getOutubro());
			if(exibirColuna(MesEnum.NOVEMBRO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getNovembro());
			if(exibirColuna(MesEnum.DEZEMBRO.toString(), mes))
				valor+= getDouble(despesa.getDespesaMensalisada().getDezembro());
		}
		return valor;
	}

	public Double calcularValorColuna(String month,List<DespesaForecast> despesasNoDetalhe)
	{
		MesEnum mesEnum = MesEnum.valueOf(month);
		Double valor = 0d;
		if(despesasNoDetalhe == null)
			return null;

		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			switch(mesEnum)
			{
				case JANEIRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getJaneiro());
					break;
				case FEVEREIRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getFevereiro());
					break;

				case MARCO:
					valor+=getDouble(despesa.getDespesaMensalisada().getMarco());
					break;

				case ABRIL:
					valor+=getDouble(despesa.getDespesaMensalisada().getAbril());
					break;

				case MAIO:
					valor+=getDouble(despesa.getDespesaMensalisada().getMaio());
					break;

				case JUNHO:
					valor+=getDouble(despesa.getDespesaMensalisada().getJunho());
					break;

				case JULHO:
					valor+=getDouble(despesa.getDespesaMensalisada().getJulho());
					break;

				case AGOSTO:
					valor+=getDouble(despesa.getDespesaMensalisada().getAgosto());
					break;

				case SETEMBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getSetembro());
					break;

				case OUTUBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getOutubro());
					break;

				case NOVEMBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getNovembro());
					break;

				case DEZEMBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDezembro());
					break;

			

			}
		}
		return valor;
	}


	public Boolean exibirColuna(String mounth,String mes)
	{
		if (mes != null) {
		MesEnum mesEnum = MesEnum.obterMes(mounth);
		MesEnum mesForecast = MesEnum.obterMes(mes.toUpperCase());

		//mes anterior nao deve ser exibido
		if(mesForecast.getId()> mesEnum.getId() )
			return false;

		if(mesForecast.getId() <= mesEnum.getId() )
			return true;
		}
		return false;


	}
	
	public Double getDouble(Double d)
	{
		return d !=null? d:0d ;
	}



}
