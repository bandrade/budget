package com.dupont.budget.bpm.custom.process;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.dto.CentroDeCustoDTO;

public interface BPMProcessManagerApi {
	long startBudgetProcess(CentroDeCustoDTO[] ceDtos, String ano) throws Exception;
	void abortProcess(long processInstanceId);
	public Object getProcessVariable(long processInstanceId, String variable)
			throws BPMException;
	public boolean isProcessAlreadyStarted(String ano) throws BPMException;;
}
