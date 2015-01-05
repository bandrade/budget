package com.dupont.budget.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.SolicitacaoPagamentoDTO;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.service.SolicitacaoPagamentoService;
import com.dupont.budget.service.bpms.BPMSProcessService;

/**
 * Implementacao do servico que controla a solicitação de pagamento.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Stateless
public class SolicitacaoPagamentoServiceBean implements SolicitacaoPagamentoService {

	@Inject
	private BPMSProcessService processService;

	@PersistenceContext(unitName="budget-pu")
	protected EntityManager em;

	@Override
	public void startSolicitacaoPagamento( SolicitacaoPagamento solicitacaoPagamento) {
		
		// Salva a entidade no banco
		// Necessario salvar antes para que os IDs estejam populados
		em.persist(solicitacaoPagamento);

		// Instanciar os DTOs que serão os parametros da chamada ao processo  
		List<SolicitacaoPagamentoDTO> _solicitacoes = new ArrayList<SolicitacaoPagamentoDTO>();
		
		for (DespesaSolicitacaoPagamento despesa : solicitacaoPagamento.getDespesas()) {
			
			SolicitacaoPagamentoDTO _solicitacao = new SolicitacaoPagamentoDTO();
			_solicitacao.setIdDespesa(despesa.getId());
			_solicitacao.setIdSolicitacao(solicitacaoPagamento.getId());
			_solicitacao.setNumeroNota(solicitacaoPagamento.getNumeroNotaFiscal());
			
			AreaDTO _area = new AreaDTO();
			_area.setId(despesa.getAcao().getId());
			_area.setNome(despesa.getAcao().getNome());
			
			_solicitacoes.add(_solicitacao);			
		}
		
		// Inicia o processo
		long processInstanceId = 0;
		try {
			processInstanceId = processService.iniciarProcessoSolicitacaoPagamento(null);
		} catch (Exception e) {
		}

		solicitacaoPagamento.setProcessInstanceId(processInstanceId);
		
	}

}
