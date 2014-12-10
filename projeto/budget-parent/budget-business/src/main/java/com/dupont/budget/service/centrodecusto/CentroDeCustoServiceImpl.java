package com.dupont.budget.service.centrodecusto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dupont.budget.dto.CentroDeCustoDTO;
@Path("CentroDeCusto")
public class CentroDeCustoServiceImpl implements CentroDeCustoService{

	@Override
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CentroDeCustoDTO[] obterCentrosDeCusto() {
		List<CentroDeCustoDTO> centrosDeCusto = new ArrayList<CentroDeCustoDTO>();
		CentroDeCustoDTO centroDeCustoDTO = new CentroDeCustoDTO("DCP Programa Colaboradores","WR31601027" ,"Distribuicao");
		centrosDeCusto.add(centroDeCustoDTO);
		centroDeCustoDTO = new CentroDeCustoDTO("DCP Marketing","WR31601025" ,"Distribuicao");
		centrosDeCusto.add(centroDeCustoDTO);
		return centrosDeCusto.toArray(new CentroDeCustoDTO[centrosDeCusto.size()]);
	}

}
