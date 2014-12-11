package com.dupont.budget.service.centrodecusto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.ColaboradorDTO;
import com.dupont.budget.dto.PapelDTO;
@Path("CentroDeCusto")
public class CentroDeCustoServiceImpl implements CentroDeCustoService{

	@Override
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CentroDeCustoDTO[] obterCentrosDeCusto() {
		List<CentroDeCustoDTO> centrosDeCusto = new ArrayList<CentroDeCustoDTO>();
		centrosDeCusto.add(getMock1());
		return centrosDeCusto.toArray(new CentroDeCustoDTO[centrosDeCusto.size()]);
	}
	
	private CentroDeCustoDTO getMock1()
	{
		String area="Distribuicao";
		String nomeCentroDeCusto="DCP Programa Colaboradores";
		String codigoCentroDeCusto="WR31601027";

		List<PapelDTO> papeis = new ArrayList<PapelDTO>();
		
		ColaboradorDTO colaboradorResponsavelCC= new ColaboradorDTO("Veronica Gaviolle","veronicag","bruno.balint@gmail.com");
		PapelDTO papelResponsavelCC = new PapelDTO("RESPONSAVEL_"+codigoCentroDeCusto, colaboradorResponsavelCC);

		PapelDTO papelLiderCC = new PapelDTO("GERENTE_"+codigoCentroDeCusto, colaboradorResponsavelCC);
		
		ColaboradorDTO colaboradoLiderArea= new ColaboradorDTO("Guido Visitin","guidov","bandrade@redhat.com");
		PapelDTO papelLiderArea = new PapelDTO("LIDER_"+area, colaboradoLiderArea);
		
		papeis.add(papelResponsavelCC);
		papeis.add(papelLiderCC);
		papeis.add(papelLiderArea);
		
		CentroDeCustoDTO centroDeCusto = new CentroDeCustoDTO(nomeCentroDeCusto,codigoCentroDeCusto,area,papeis);
		
		return centroDeCusto;
	}

}
