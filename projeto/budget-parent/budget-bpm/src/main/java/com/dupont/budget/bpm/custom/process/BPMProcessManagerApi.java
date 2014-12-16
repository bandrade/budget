package com.dupont.budget.bpm.custom.process;

import com.dupont.budget.bpm.custom.exception.BPMException;

public interface BPMProcessManagerApi {
	long startBudgetProcess();
	void abortProcess(long processInstanceId);
	public Object getProcessVariable(long processInstanceId, String variable)
			throws BPMException;
}
