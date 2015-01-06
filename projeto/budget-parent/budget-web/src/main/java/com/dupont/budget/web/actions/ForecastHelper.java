package com.dupont.budget.web.actions;

import java.util.List;

import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.MesEnum;

public class ForecastHelper {
	public Double calcularValorMensalisado(DespesaForecast despesa)
	{

		Double valor = 0d;
		valor+= getDouble(despesa.getDespesaMensalisada().getJaneiro());
		valor+= getDouble(despesa.getDespesaMensalisada().getFevereiro());
		valor+= getDouble(despesa.getDespesaMensalisada().getMarco());
		valor+= getDouble(despesa.getDespesaMensalisada().getAbril());
		valor+= getDouble(despesa.getDespesaMensalisada().getMaio());
		valor+= getDouble(despesa.getDespesaMensalisada().getJunho());
		valor+= getDouble(despesa.getDespesaMensalisada().getAgosto());
		valor+= getDouble(despesa.getDespesaMensalisada().getSetembro());
		valor+= getDouble(despesa.getDespesaMensalisada().getOutubro());
		valor+= getDouble(despesa.getDespesaMensalisada().getNovembro());
		valor+= getDouble(despesa.getDespesaMensalisada().getDezembro());
		return valor;
	}

	public Double calcularValorMensalisadoDespesa(DespesaForecast despesa)
	{

		Double valor = 0d;
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaJaneiro());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaFevereiro());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaMarco());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaAbril());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaMaio());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaJunho());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaAgosto());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaSetembro());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaOutubro());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaNovembro());
		valor+= getDouble(despesa.getDespesaMensalisada().getDespesaDezembro());
		return valor;
	}

	public Double calcularValorDespesas(String mounth,List<DespesaForecast> despesasNoDetalhe)
	{
		MesEnum mesEnum = MesEnum.valueOf(mounth);
		Double valor = 0d;
		if(despesasNoDetalhe == null)
			return null;

		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			switch(mesEnum)
			{
				case JANEIRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaJaneiro());
					break;
				case FEVEREIRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaFevereiro());
					break;

				case MARCO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaMarco());
					break;

				case ABRIL:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaAbril());
					break;

				case MAIO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaMaio());
					break;

				case JUNHO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaJunho());
					break;

				case JULHO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaJulho());
					break;

				case AGOSTO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaAgosto());
					break;

				case SETEMBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaSetembro());
					break;

				case OUTUBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaOutubro());
					break;

				case NOVEMBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaNovembro());
					break;

				case DEZEMBRO:
					valor+=getDouble(despesa.getDespesaMensalisada().getDespesaDezembro());
					break;

				default:
					valor+=calcularValorMensalisadoDespesa(despesa);
					break;

			}
		}
		return valor;
	}

	public Double calcularValorColuna(String mounth,List<DespesaForecast> despesasNoDetalhe)
	{
		MesEnum mesEnum = MesEnum.valueOf(mounth);
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

				default:
					valor+=calcularValorMensalisado(despesa);
					break;

			}
		}
		return valor;
	}


	public Boolean exibirColuna(String mounth,String mes)
	{
		MesEnum mesEnum = MesEnum.valueOf(mounth);
		MesEnum mesForecast = MesEnum.valueOf(mes.toUpperCase());

		//mes anterior nao deve ser exibido
		if(mesForecast.getId()> mesEnum.getId() )
			return false;

		if(mesForecast.getId() <= mesEnum.getId() )
			return true;

		return false;


	}
	public Boolean exibirDespesa(String mounth,String mes)
	{
		MesEnum mesEnum = MesEnum.valueOf(mounth);
		MesEnum mesForecast = MesEnum.valueOf(mes.toUpperCase());

		if(mesForecast.getId() == mesEnum.getId() )
			return true;
		return false;

	}
	public Double getDouble(Double d)
	{
		return d !=null? d:0d ;
	}



}
