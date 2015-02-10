package com.dupont.budget.model;

import com.dupont.budget.model.MesEnum;

public enum MesEnum {
	JANEIRO(1l,"Janeiro"),
	FEVEREIRO(2L,"Fevereiro"),
	MARCO(3L,"Março"),
	ABRIL(4L,"Abril"),
	MAIO(5L,"Maio"),
	JUNHO(6L,"Junho"),
	JULHO(7L,"Julho"),
	AGOSTO(8L,"Agosto"),
	SETEMBRO(9L,"Setembro"),
	OUTUBRO(10L,"Outubro"),
	NOVEMBRO(11L,"Novembro"),
	DEZEMBRO(12L,"Dezembro");

	 MesEnum(Long id,String mes)
	{
		this.id=id;
		this.mes=mes;
	}
	private long id ;
	private String mes;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public static MesEnum obterMes(String mes)
	{
		MesEnum mesForecast = null;
		if(mes.toUpperCase().equals("MARÇO"))
			mesForecast= MesEnum.valueOf("MARCO");
		else
			mesForecast= MesEnum.valueOf(mes.toUpperCase());
		return mesForecast;
	}

}
