package com.dupont.budget.service.bpms;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.bpm.custom.process.BPMProcessManagerApiImpl;
import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.ColaboradorDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.centrodecusto.CentroDeCustoService;
@Model
public class BPMSProcessServiceImpl implements BPMSProcessService{
	@Inject
	private BPMProcessManagerApiImpl processApi ;

	@Inject
	private CentroDeCustoService ccService;

	@Inject
	private DomainService domainService;

	public long iniciarProcessoBudget(String ano) throws Exception {
		CentroDeCustoDTO [] ceDtos = ccService.obterCentrosDeCusto();
		List<AreaDTO> areasListDto =  new ArrayList<>();
		List<Area> areas = domainService.findAll(Area.class);
		for(Area area : areas)
		{
			AreaDTO areaDto =  new AreaDTO();
			areaDto.setId(area.getId());
			areaDto.setNome(area.getNome());
			Usuario usuario = area.getLider().getUsuario();
			areaDto.setLider(new ColaboradorDTO( usuario.getNome(),usuario.getLogin(),usuario.getEmail()));
			areasListDto.add(areaDto);
		}

		return processApi.startBudgetProcess(ceDtos,ano);
	}

	@Override
	public Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception {
		return processApi.getProcessVariable(idProcesso, variavel);
	}

	@Override
	public boolean existeProcessoAtivo(String ano) {
		return processApi.isProcessAlreadyStarted(ano);
	}


}
