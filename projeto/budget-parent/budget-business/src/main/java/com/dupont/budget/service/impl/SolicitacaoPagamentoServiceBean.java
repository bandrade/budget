package com.dupont.budget.service.impl;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import com.dupont.budget.dto.AreaDTO;
import com.dupont.budget.dto.CentroDeCustoDTO;
import com.dupont.budget.dto.ColaboradorDTO;
import com.dupont.budget.dto.EmailDTO;
import com.dupont.budget.dto.EmailSolicitacaoPagamentoDTO;
import com.dupont.budget.dto.SolicitacaoPagamentoDTO;
import com.dupont.budget.exception.DuplicateEntityException;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.OrigemSolicitacao;
import com.dupont.budget.model.SolicitacaoPagamento;
import com.dupont.budget.service.SolicitacaoPagamentoService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.service.jms.DupontMailSender;

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

	@Inject
	private DupontMailSender mailSender;
	
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


	@Override
	public void enviarRelatorioDespesasExternas() {
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date tomorrowWithZeroTime=null;
		try {
			tomorrowWithZeroTime = formatter.parse(formatter.format(new Date()));
		} catch (ParseException e1) {
			logger.error("ERRO AO FAZER PARSE DATA",e1);
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(tomorrowWithZeroTime);
		c1.add(Calendar.DAY_OF_YEAR,1);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(tomorrowWithZeroTime);
		c2.add(Calendar.DAY_OF_YEAR,-8);
		
		Map<ColaboradorDTO,Set<CentroDeCustoDTO>> mapResponsabilidade = new HashMap<ColaboradorDTO,Set<CentroDeCustoDTO>>();
		List<Object []>  resultado = (List<Object[]>)	em.createNamedQuery("Colaborador.ObterResponsaveisCentroCusto").getResultList();
		for(Object [] item : resultado)
		{
			 ColaboradorDTO colaborador = new ColaboradorDTO((String)item[3],(String)item[1],(String)item[2],Long.valueOf(item[0]+""));
			 List<Object []>  resultadoCC = (List<Object[]>) em.createNamedQuery("CentroCusto.ObterResponsabilidadeUsuario")
			 			.setParameter("usuarioId",colaborador.getId() )
			 			.getResultList();
			 Set<CentroDeCustoDTO> centroDeCustoDTOs = new HashSet<>();
			 for(Object [] cc : resultadoCC)
			 {
				 CentroDeCustoDTO centroDeCustoDTO = new CentroDeCustoDTO();
				 centroDeCustoDTO.setId(Long.valueOf(cc[0]+""));
				 centroDeCustoDTO.setNome(cc[1].toString());
				 centroDeCustoDTO.setNumero(cc[2].toString());
				 centroDeCustoDTOs.add(centroDeCustoDTO);
			 }
			 mapResponsabilidade.put(colaborador, centroDeCustoDTOs);
		}
		
		for(ColaboradorDTO colaborador : mapResponsabilidade.keySet())
		{
			StringBuilder email = new StringBuilder();
			email.append("Abaixo segue a lista de cover sheets que foram inseridas nos centros de custo sob sua responsabilidade por")
				 .append(" colaboradores que n&atilde;o pertecem a este CC.:");
			
			boolean possuiNota=false;
			
			
			for(CentroDeCustoDTO ccDto : mapResponsabilidade.get(colaborador))
			{
				try
				{
					List<Object[]> _notas =  em.createNamedQuery("SolicitacaoPagamento.ObterNotasOutrosUsuarios")
					.setParameter("centro_custo_id", ccDto.getId())
					.setParameter("data_7_dias_atras",c2.getTime())
					.setParameter("data_hoje",c1.getTime())
					.getResultList();
					if(_notas!=null && _notas.size() >0)
					{
						possuiNota=true;
						email.append("<br/><br/><b>Nome Centro Custo:</b>" +ccDto.getNumero());
						adicionarNotaAoEmail(email,_notas);
						
					}
				}
				catch(NoResultException e)
				{
					continue;
				}
				
			}
			if(possuiNota)
			{
				enviarEmail(colaborador,email,c2.getTime(),c1.getTime());
			}
			email = new StringBuilder();
		}
		
	}
	public void adicionarNotaAoEmail(StringBuilder buffer, List<Object[]> lista)
	{
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Locale locale = new Locale("pt", "BR");
		buffer.append("<br/><br/><table border=1>");
		buffer.append("<tr style='background-color:gray'><th>Nota Fiscal</th>");
		buffer.append("<th>Data</th>");
		buffer.append("<th>Fornecedor</th>");
		buffer.append("<th>Colaborador</th>");
		buffer.append("<th>Tipo de Despesa</th>");
		buffer.append("<th>A&ccedil;&atilde;o </th>");
		buffer.append("<th>Valor</th>");
		buffer.append("</tr>");
		
		for(Object [] object : lista)
		{
			EmailSolicitacaoPagamentoDTO item =new EmailSolicitacaoPagamentoDTO((String)object[0],String.valueOf(formatter.format(object[1])),(String)object[2],(String)object[3],(String)object[4],(String)object[5],
					NumberFormat.getCurrencyInstance(locale).format(Double.valueOf(object[6].toString())));
			buffer.append("<tr>");
			buffer.append("<td>"+item.getNotaFiscal() +"</td>");
			buffer.append("<td>"+item.getDataCriacao() +"</td>");
			buffer.append("<td>"+item.getFornecedor() +"</td>");
			buffer.append("<td>"+item.getUsuario() +"</td>");
			buffer.append("<td>"+item.getTipoDespesa() +"</td>");
			buffer.append("<td>"+item.getAcao() +"</td>");
			buffer.append("<td>"+item.getValor() +"</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
	}
	
	public void enviarEmail(ColaboradorDTO colaboradorDTO,StringBuilder buffer,Date from ,Date to)
	{

		Calendar c1 = Calendar.getInstance();
		c1.setTime(from);
		c1.add(Calendar.DAY_OF_YEAR,1);
		String _from = c1.get(Calendar.DAY_OF_MONTH) +"/"+ (c1.get(Calendar.MONTH)+1);  
	
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(to);
		c2.add(Calendar.DAY_OF_YEAR,-1);
		String _to = c2.get(Calendar.DAY_OF_MONTH) +"/"+ (c2.get(Calendar.MONTH)+1);
			
		EmailDTO emailDTO = new EmailDTO("dupontbpm@gmail.com",colaboradorDTO.getEmail(),"Budget - Cover Sheets "+_from+" - "+_to,buffer.toString());
		mailSender.sendMail(emailDTO);
	}
}
