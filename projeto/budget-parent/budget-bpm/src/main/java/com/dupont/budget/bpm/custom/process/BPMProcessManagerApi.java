package com.dupont.budget.bpm.custom.process;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.SolicitacaoPagamentoDTO;

public interface BPMProcessManagerApi {

	long startBudgetProcess(CentroDeCustoDTO[] ceDtos,AreaDTO[] area, String ano) throws Exception;
	long startSolicitacaoPagamentoProcess(SolicitacaoPagamentoDTO [] solicitacoes) throws Exception;
	void abortProcess(long processInstanceId);
	public Object getProcessVariable(long processInstanceId, String variable)
			throws BPMException;
	public boolean isProcessAlreadyStarted(String ano) throws BPMException;
	public long startForecastProcess(CentroDeCustoDTO[] ceDtos, String ano, String mes) throws Exception;
	public boolean isProcessForecastAlreadyStarted(String ano,String mes) throws BPMException;
}
