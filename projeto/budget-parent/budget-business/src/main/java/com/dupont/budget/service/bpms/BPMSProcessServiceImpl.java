package com.dupont.budget.service.bpms;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.bpm.custom.process.BPMProcessManagerApiImpl;
import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;
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
			List<CentroCusto> centrosDeCusto = ccService.findByArea(area.getId());
			if(centrosDeCusto !=null && centrosDeCusto.size() >0)
			{
				AreaDTO areaDto =  new AreaDTO();
				areaDto.setId(area.getId());
				areaDto.setNome(area.getNome());
				areasListDto.add(areaDto);
			}
		}
		AreaDTO [] areaArray = areasListDto.toArray(new AreaDTO[areasListDto.size()]);
		return processApi.startBudgetProcess(ceDtos,areaArray,ano);
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
