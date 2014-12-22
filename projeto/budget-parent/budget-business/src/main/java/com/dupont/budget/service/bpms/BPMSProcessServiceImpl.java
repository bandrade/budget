package com.dupont.budget.service.bpms;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.bpm.custom.process.BPMProcessManagerApiImpl;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.service.centrodecusto.CentroDeCustoService;
@Model
public class BPMSProcessServiceImpl implements BPMSProcessService{
	@Inject
	private BPMProcessManagerApiImpl processApi ;

	@Inject
	private CentroDeCustoService ccService;

	public long iniciarProcessoBudget(String ano) throws Exception {
		CentroDeCustoDTO [] ceDtos = ccService.obterCentrosDeCusto();

		return processApi.startBudgetProcess(ceDtos,ano);
	}

	@Override
	public Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception {
		return processApi.getProcessVariable(idProcesso, variavel);
	}


}
