package com.dupont.budget.service.centrodecusto;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.ColaboradorDTO;
import com.dupont.budget.dto.PapelDTO;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.Papel;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.GenericService;
@Path("CentroDeCusto")
public class CentroDeCustoServiceImpl extends GenericService implements CentroDeCustoService{
	@Inject
	private DomainService domainService;

	@Override
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CentroDeCustoDTO[] obterCentrosDeCusto() {
		List<CentroDeCustoDTO> centrosDeCusto = new ArrayList<CentroDeCustoDTO>();
		List<CentroCusto> result = em.createNamedQuery("CentroCusto.findAll", CentroCusto.class).getResultList();
		for(CentroCusto cc : result)
		{
			CentroDeCustoDTO ccDto = new CentroDeCustoDTO();
			ccDto.setNome(cc.getNome());
			ccDto.setId(cc.getId());
			ccDto.setNumero(cc.getCodigo());
			ccDto.setArea(cc.getArea().getNome());
			List<PapelUsuario> papeis = domainService.findPapeisByCentroDeCusto(cc.getId());
			List<PapelDTO> papeisDTO = new ArrayList<PapelDTO>();

			for(PapelUsuario papel : papeis)
			{
				PapelDTO p = new PapelDTO();
				p.setNomePapel(papel.getPapel().getNome());
				ColaboradorDTO colaborador = new ColaboradorDTO(papel.getUsuario().getNome(),
								papel.getUsuario().getLogin(),papel.getUsuario().getEmail());

				p.setColaborador(colaborador);
				papeisDTO.add(p);
			}
			ccDto.setPapeis(papeisDTO);

			Papel papelLider = cc.getArea().getLider().getPapel();
			Usuario usuario = cc.getArea().getLider().getUsuario();
			PapelDTO pDto = new PapelDTO();
			pDto.setNomePapel(papelLider.getNome());
			ColaboradorDTO colaborador = new ColaboradorDTO(usuario.getNome(),
					usuario.getLogin(),usuario.getEmail());

			pDto.setColaborador(colaborador);
			ccDto.getPapeis().add(pDto);
			centrosDeCusto.add(ccDto);
		}

		return centrosDeCusto.toArray(new CentroDeCustoDTO[centrosDeCusto.size()]);
	}

}
