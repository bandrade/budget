package com.dupont.budget.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.SolicitacaoPagamentoDTO;
import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.OrigemSolicitacao;
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
	
	@Inject
	private Logger logger;

	@Override
	public void startSolicitacaoPagamento( SolicitacaoPagamento solicitacaoPagamento) throws DuplicateEntityException {
		
		// valida solicitacao pagamento com numero da nota fiscal igual
		List <SolicitacaoPagamento> exists = em.createNamedQuery(SolicitacaoPagamento.FIND_BY_NUMERO_NOTA, SolicitacaoPagamento.class)
												.setParameter("numeroNotaFiscal", solicitacaoPagamento.getNumeroNotaFiscal())
												.getResultList();
		
		if( exists != null && !exists.isEmpty())
			throw new DuplicateEntityException(solicitacaoPagamento);
		
		// Salva a entidade no banco
		// Necessario salvar antes para que os IDs estejam populados
		em.persist(solicitacaoPagamento);
		
		callSolicitacaoPagamentoProcess(solicitacaoPagamento);		
	}


	@Override
	public SolicitacaoPagamento findSolicitacaoPagamento(Long id) {
		
		return em.find(SolicitacaoPagamento.class, id);
	}

	@Override
	public void updateSolicitacaoPagamento( SolicitacaoPagamento solicitacaoPagamento) {
		
		solicitacaoPagamento = em.merge(solicitacaoPagamento);
		
		//callSolicitacaoPagamentoProcess(solicitacaoPagamento);
	}

	private void callSolicitacaoPagamentoProcess( SolicitacaoPagamento solicitacaoPagamento) {
		
		if( solicitacaoPagamento.getOrigem() == OrigemSolicitacao.SAP )
			return;
		
		// Instanciar os DTOs que serão os parametros da chamada ao processo  
		List<SolicitacaoPagamentoDTO> _solicitacoes = new ArrayList<SolicitacaoPagamentoDTO>();
		
		for (DespesaSolicitacaoPagamento despesa : solicitacaoPagamento.getDespesas()) {
			
			SolicitacaoPagamentoDTO _solicitacao = new SolicitacaoPagamentoDTO();
			_solicitacao.setIdDespesa(despesa.getId());
			_solicitacao.setIdSolicitacao(solicitacaoPagamento.getId());
			_solicitacao.setNumeroNota(solicitacaoPagamento.getNumeroNotaFiscal());
			
			AreaDTO _area = new AreaDTO();
			_area.setId(despesa.getCentroCusto().getArea().getId());
			_area.setNome(despesa.getCentroCusto().getArea().getNome());
			
			_solicitacao.setArea(_area);
			
			_solicitacoes.add(_solicitacao);			
		}
		
		// Inicia o processo
		long processInstanceId = 0;
		try {
			processInstanceId = processService.iniciarProcessoSolicitacaoPagamento(_solicitacoes.toArray(new SolicitacaoPagamentoDTO[_solicitacoes.size()]));
		} catch (Exception e) {
			logger.error("Erro ao iniciar processo BPM",e);
		}

		solicitacaoPagamento.setProcessInstanceId(processInstanceId);
	}
}
