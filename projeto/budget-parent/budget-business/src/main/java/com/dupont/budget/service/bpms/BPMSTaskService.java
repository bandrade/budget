package com.dupont.budget.service.bpms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.dto.ProcessType;
import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;
@Local
public interface BPMSTaskService {
	List<TarefaDTO> obterTarefas(String user) throws Exception;
	void aprovarTarefa(String user, Long taskId,Map<String, Object> params) throws Exception;
	TarefaDTO obterTarefaPorId(Long id) throws BPMException;
	Map<String,Object> obterConteudoTarefa(long taskId) throws BPMException;
	public List<TarefaDTO> obterTarefasAdm() throws Exception;
	public List<TarefaDTO> obterTarefasProcesso(Area area,CentroCusto centroCusto, ProcessType processType) throws BPMException;
	List<TarefaDTO> obterTarefasProcessoSolicitacaoPagamento(Date de, Date ate,
			Area areaSelecionada, CentroCusto centroCustoSelecionado,
			ProcessType solicitacaoPagamento) throws BPMException;

}
