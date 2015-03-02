package com.dupont.budget.bpm.custom.process;

import java.util.Date;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.SolicitacaoPagamentoDTO;

public interface BPMProcessManagerApi {

	long startBudgetProcess(CentroDeCustoDTO[] ceDtos,AreaDTO[] area, String ano,Date prazo,String emails) throws Exception;
	long startSolicitacaoPagamentoProcess(SolicitacaoPagamentoDTO [] solicitacoes) throws Exception;
	void abortProcess(long processInstanceId);
	public Object getProcessVariable(long processInstanceId, String variable)
			throws BPMException;
	public boolean isProcessAlreadyStarted(String ano) throws BPMException;
	public long startForecastProcess(CentroDeCustoDTO[] ceDtos, String ano, String mes, Date prazo,String email,
			String tipoToleranciaNegativa,
			Double valorToleranciaNegativa,String tipoToleranciaPositiva,Double valorToleranciaPositiva) throws Exception;
	public boolean isProcessForecastAlreadyStarted(String ano) throws BPMException;
	public boolean isProcessInstanceRunning(long processInstanceId);

}
