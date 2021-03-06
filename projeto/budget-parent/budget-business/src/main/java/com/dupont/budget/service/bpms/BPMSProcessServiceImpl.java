package com.dupont.budget.service.bpms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.bpm.custom.process.BPMProcessManagerApi;
import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.SolicitacaoPagamentoDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.centrodecusto.CentroDeCustoService;

@Model
public class BPMSProcessServiceImpl implements BPMSProcessService{
	@Inject
	private BPMProcessManagerApi processApi ;

	@Inject
	private CentroDeCustoService ccService;

	@Inject
	private DomainService domainService;

	public long iniciarProcessoBudget(String ano,Date prazo) throws Exception {
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
				for(CentroCusto centroCusto : centrosDeCusto)
				{
					areaDto.getCentrosDeCusto().add(ccService.parseCentroCusto(centroCusto));
				}
				areasListDto.add(areaDto);
			}
		}
		
		AreaDTO [] areaArray = areasListDto.toArray(new AreaDTO[areasListDto.size()]);
		String emails = domainService.obterEmailsUsuarios();
		return processApi.startBudgetProcess(ceDtos,areaArray,ano,prazo,emails);
	}


	@Override
	public long iniciarProcessoForecast(String ano, String mes,Date prazo)
			throws Exception {
		CentroDeCustoDTO [] ceDtos = ccService.obterCentrosDeCusto();
		String emails = domainService.obterEmailsUsuarios();
		return processApi.startForecastProcess(ceDtos, ano, mes,prazo,emails);
	}

	@Override
	public Object obterVariavelProcesso(Long idProcesso,String variavel) throws Exception {
		return processApi.getProcessVariable(idProcesso, variavel);
	}

	@Override
	public boolean existeProcessoAtivo(String ano) throws BPMException {
		return processApi.isProcessAlreadyStarted(ano);
	}

	@Override
	public long iniciarProcessoSolicitacaoPagamento(SolicitacaoPagamentoDTO [] solicitacoes) throws Exception {

		return processApi.startSolicitacaoPagamentoProcess(solicitacoes);
	}


	@Override
	public boolean existeProcessoForecastAtivo(String ano)
			throws Exception {

		return processApi.isProcessForecastAlreadyStarted(ano);
	}




}
