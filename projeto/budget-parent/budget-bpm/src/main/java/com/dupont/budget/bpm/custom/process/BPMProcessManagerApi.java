package com.dupont.budget.bpm.custom.process;

public interface BPMProcessManagerApi {
	long startBudgetProcess();
	void abortProcess(long processInstanceId);
}
