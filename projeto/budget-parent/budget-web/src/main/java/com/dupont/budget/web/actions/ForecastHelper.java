package com.dupont.budget.web.actions;

import java.math.BigDecimal;
import java.util.List;

import com.dupont.budget.model.DespesaForecast;
import com.dupont.budget.model.MesEnum;

public class ForecastHelper {
	public BigDecimal calcularValorMensalisado(String mes,DespesaForecast despesa)
	{

		BigDecimal valor = new BigDecimal(0d);
		if(despesa!=null)
		{
			if(exibirColuna(MesEnum.JANEIRO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getJaneiro()));
			if(exibirColuna(MesEnum.FEVEREIRO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getFevereiro()));
			if(exibirColuna(MesEnum.MARCO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getMarco()));
			if(exibirColuna(MesEnum.ABRIL.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getAbril()));
			if(exibirColuna(MesEnum.MAIO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getMaio()));
			if(exibirColuna(MesEnum.JUNHO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getJunho()));
			if(exibirColuna(MesEnum.JULHO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getJulho()));
			if(exibirColuna(MesEnum.AGOSTO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getAgosto()));
			if(exibirColuna(MesEnum.SETEMBRO.toString(), mes))
				valor= valor.add(getBigDecimal(despesa.getDespesaMensalisada().getSetembro()));
			if(exibirColuna(MesEnum.OUTUBRO.toString(), mes))
				valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getOutubro()));
			if(exibirColuna(MesEnum.NOVEMBRO.toString(), mes))
				valor= valor.add(getBigDecimal(despesa.getDespesaMensalisada().getNovembro()));
			if(exibirColuna(MesEnum.DEZEMBRO.toString(), mes))
				valor= valor.add(getBigDecimal(despesa.getDespesaMensalisada().getDezembro()));
		}
		return valor;
	}

	public BigDecimal calcularValorColuna(String month,List<DespesaForecast> despesasNoDetalhe)
	{
		MesEnum mesEnum = MesEnum.valueOf(month);
		BigDecimal valor = new BigDecimal(0d);
		if(despesasNoDetalhe == null)
			return null;

		for(DespesaForecast despesa : despesasNoDetalhe)
		{
			switch(mesEnum)
			{
				case JANEIRO:
					valor = valor.add(getBigDecimal(despesa.getDespesaMensalisada().getJaneiro()));
					break;
				case FEVEREIRO:
					valor = valor.add(getBigDecimal(despesa.getDespesaMensalisada().getFevereiro()));
					break;

				case MARCO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getMarco()));
					break;

				case ABRIL:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getAbril()));
					break;

				case MAIO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getMaio()));
					break;

				case JUNHO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getJunho()));
					break;

				case JULHO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getJulho()));
					break;

				case AGOSTO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getAgosto()));
					break;

				case SETEMBRO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getSetembro()));
					break;

				case OUTUBRO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getOutubro()));
					break;

				case NOVEMBRO:
					valor= valor.add(getBigDecimal(despesa.getDespesaMensalisada().getNovembro()));
					break;

				case DEZEMBRO:
					valor =valor.add(getBigDecimal(despesa.getDespesaMensalisada().getDezembro()));
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

	public BigDecimal getBigDecimal(BigDecimal d)
	{
		return d !=null? d: new BigDecimal(0d) ;
	}




}
