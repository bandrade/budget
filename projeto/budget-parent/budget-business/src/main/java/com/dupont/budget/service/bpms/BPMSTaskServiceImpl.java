package com.dupont.budget.service.bpms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.NotImplementedException;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.TaskSummary;
import org.slf4j.Logger;

import com.dupont.budget.bpm.custom.exception.BPMException;
import com.dupont.budget.bpm.custom.process.BPMProcessManagerApi;
import com.dupont.budget.bpm.custom.task.BPMTaskManagerApi;
import com.dupont.budget.dto.ProcessType;
import com.dupont.budget.dto.SolicitacaoPagamentoDTO;
import com.dupont.budget.dto.StatusTarefaEnum;
import com.dupont.budget.dto.TarefaDTO;
import com.dupont.budget.model.Area;
import com.dupont.budget.model.CentroCusto;
import com.dupont.budget.model.DespesaSolicitacaoPagamento;
import com.dupont.budget.model.PapelUsuario;
import com.dupont.budget.model.Usuario;
import com.dupont.budget.service.DomainService;
import com.dupont.budget.service.GenericService;
import com.dupont.budget.service.centrodecusto.CentroDeCustoService;

@Stateless
public class BPMSTaskServiceImpl extends GenericService implements BPMSTaskService {

	@Inject
	private BPMTaskManagerApi taskApi;

	@Inject
	private BPMProcessManagerApi processApi;

	@Inject
	private DomainService domainService;

	@Inject
	private CentroDeCustoService ccService;

	@Inject
	private Logger logger;

	public List<TarefaDTO> obterTarefas(String user) throws Exception {
		Usuario usuario = domainService.getUsuarioByLogin(user);

		List<TaskSummary> tasks = taskApi.retrieveTaskList(user,usuario.getPapeisAsString());
		List<TarefaDTO> tarefas = new ArrayList<TarefaDTO>();
		for (TaskSummary task : tasks) {
			TarefaDTO tarefaDTO = parseTaskToTarefa(task);
			tarefas.add(tarefaDTO);
		}
		return tarefas;
	}


	public List<TarefaDTO> obterTarefasAdm() throws Exception {
		List<TaskSummary> tasks = taskApi.retrieveTaskListAdm();
		List<TarefaDTO> tarefas = new ArrayList<TarefaDTO>();
		for (TaskSummary task : tasks) {
			TarefaDTO tarefaDTO = parseTaskToTarefa(task);
			tarefas.add(tarefaDTO);
		}
		return tarefas;
	}


	public List<TarefaDTO> obterTarefasAdmCompletas() throws Exception {
		List<TaskSummary> tasks = taskApi.retrieveTaskListAdmCompletas();
		List<TarefaDTO> tarefas = new ArrayList<TarefaDTO>();
		for (TaskSummary task : tasks) {
			TarefaDTO tarefaDTO = parseTaskToTarefa(task);
			tarefas.add(tarefaDTO);
		}
		return tarefas;
	}

	public void aprovarTarefa(String usuario, Long taskId, Map<String, Object> params ) throws Exception {

		taskApi.aproveTask(usuario, taskId, params);
	}

	@Override
	public TarefaDTO obterTarefaPorId(Long taskId) throws BPMException {
		//TODO IMPLEMENTAR
		throw new NotImplementedException("Metodo nao implementado");
	}


	private TarefaDTO parseTaskToTarefa(TaskSummary task) throws BPMException
	{
		Map<String, Object> conteudoTarefa = taskApi.getTaskContent(task.getId());
		TarefaDTO tarefaDTO = new TarefaDTO();
		tarefaDTO.setActivationTime(task.getActivationTime());
		tarefaDTO.setActualOwner(task.getActualOwner() != null ? task
				.getActualOwner().getId() : null);
		tarefaDTO.setCreatedBy(task.getCreatedBy() != null ? task
				.getCreatedBy().getId() : null);
		tarefaDTO.setCreatedOn(task.getCreatedOn());
		String subject = String.valueOf(conteudoTarefa.get("Subject"));
		Object data = processApi.getProcessVariable(task.getProcessInstanceId(), "prazo");
		if(data !=null)
		{
			tarefaDTO.setPrazo((Date) data);
		}
		else
		{
			data = processApi.getProcessVariable(task.getProcessInstanceId(), "_prazo");
			if(data !=null)
			{
				tarefaDTO.setPrazo((Date) data);
			}

		}

		tarefaDTO.setPotentialOwners(new ArrayList<String>());
		tarefaDTO.setDescription(subject ==null? "":subject);
		tarefaDTO.setExpirationTime(task.getExpirationTime());
		tarefaDTO.setId(task.getId());
		tarefaDTO.setName(task.getName());
		tarefaDTO.setPriority(task.getPriority());
		tarefaDTO.setProcessInstanceId(task.getProcessInstanceId());
		tarefaDTO.setProcessId(task.getProcessId());
		tarefaDTO.setProcessSessionId(task.getProcessSessionId());
		tarefaDTO.setSkipable(task.isSkipable());
		tarefaDTO.setStatus(StatusTarefaEnum.findByMeaning(task.getStatus()
				.name()));

		return tarefaDTO;

	}

	public Map<String,Object> obterConteudoTarefa(long taskId) throws BPMException
	{
		return taskApi.getTaskContent(taskId);

	}


	public List<TarefaDTO> obterTarefasProcesso(Area area,CentroCusto centroCusto, ProcessType processType) throws BPMException {
		List<String> groups =new ArrayList<>();

	    if(centroCusto !=null)
	    {
		    for(PapelUsuario papel : centroCusto.getResponsaveis())
		    {
		    	groups.add(papel.getPapel().getNome());
		    }
	    }
	    else if(area!=null)
	    {

	    	groups.add(area.getLider().getPapel().getNome());
	    	try {
				List<CentroCusto> listaCC = ccService.findByArea(area.getId());
				for(CentroCusto cc : listaCC)
				{
					for(PapelUsuario papel : cc.getResponsaveis())
				    {
				    	groups.add(papel.getPapel().getNome());
				    }
				}

			} catch (Exception e) {
				logger.error("Erro ao efetuar consultas de cc ",e );

			}
	    }
	    else
	    {

	    	List<Area> areas = domainService.findAll(Area.class);
	    	for(Area _area : areas)
	    	{
	    		groups.add(_area.getLider().getPapel().getNome());
		    	try {
					List<CentroCusto> listaCC = ccService.findByArea(_area.getId());
					for(CentroCusto cc : listaCC)
					{
						for(PapelUsuario papel : cc.getResponsaveis())
					    {
					    	groups.add(papel.getPapel().getNome());
					    }
					}

				} catch (Exception e) {
					logger.error("Erro ao efetuar consultas de cc ",e );

				}
	    	}

	    }

		List<TaskSummary> tasks = taskApi.retrieveTaskListFromGroups(groups,processType);
		List<TarefaDTO> tarefas = new ArrayList<TarefaDTO>();
		for (TaskSummary task : tasks) {
			TarefaDTO tarefaDTO = parseTaskToTarefa(task);
			obterUsuarioTarefa(task,tarefaDTO);
			tarefas.add(tarefaDTO);
		}

		return tarefas;
	}


	public void obterUsuarioTarefa(TaskSummary task,TarefaDTO tarefaDTO) throws BPMException
	{
		for(OrganizationalEntity group : taskApi.getTask(task.getId()).getPeopleAssignments().getPotentialOwners())
		{
			try
			{
				List<Object[]> lista = em.createNamedQuery("Usuario.findUsuarioByPapelCC").setParameter("nome_papel", group.getId()).getResultList();
				if(lista !=null && lista.size()>0)
				{
					Object[] result = lista.get(0);
					tarefaDTO.setLogin(String.valueOf(result[0]));
					tarefaDTO.setNomeUsuario(String.valueOf(result[1]));
					tarefaDTO.setNomeCentroCusto(String.valueOf(result[2]));
					tarefaDTO.setCodigoCentroCusto(String.valueOf(result[3]));
					tarefaDTO.setNomeArea(String.valueOf(result[4]));
				}
				else
				{
					 lista = em.createNamedQuery("Usuario.findUsuarioByPapelArea").setParameter("nome_papel", group.getId()).getResultList();
					 if(lista !=null && lista.size()>0)
					 {
						 Object[] result = lista.get(0);
						 tarefaDTO.setLogin(String.valueOf(result[0]));
						 tarefaDTO.setNomeUsuario(String.valueOf(result[1]));
						 tarefaDTO.setNomeArea(String.valueOf(result[2]));
					 }
				}
			}
			catch(NoResultException e)
			{
				logger.error("PAPEL " + group.getId()+" nao possui usuario atribuido");
			}

		}
	}
	public List<TarefaDTO> obterTarefasProcessoSolicitacaoPagamento(Date de, Date ate,
			Area area, CentroCusto centroCusto,
			ProcessType processType) throws BPMException
		{
			List<String> groups =new ArrayList<>();
			boolean validarCC =false;
			boolean validarArea= false;
			if(centroCusto !=null)
		    {
				validarCC = true;
				groups.add(centroCusto.getArea().getResponsavelNotas().getPapel().getNome());

			}
			else if(area !=null)
			{
				validarArea = true;
				groups.add(area.getResponsavelNotas().getPapel().getNome());
			}
			else
			{
				List<Area> areas = domainService.findAll(Area.class);
				for(Area _area: areas)
				{
					groups.add(_area.getResponsavelNotas().getPapel().getNome());
				}
			}

			List<TaskSummary> tasks = taskApi.retrieveTaskListFromGroups(groups,processType);
			List<TarefaDTO> tarefas = new ArrayList<TarefaDTO>();

			for(TaskSummary task: tasks)
			{

				SolicitacaoPagamentoDTO dto = (SolicitacaoPagamentoDTO)   taskApi.getTaskContent(task.getId()).get("solicitacaoAtual");
				DespesaSolicitacaoPagamento despesa = new DespesaSolicitacaoPagamento();
				despesa.setId(dto.getIdDespesa());
				despesa = domainService.findById(despesa);

				if(validarArea && !(despesa.getCentroCusto().getArea().equals(area)))
				{
					continue;
				}

				if(validarCC && !(despesa.getCentroCusto().equals(centroCusto)))
				{
					continue;

				}
				if(de !=null && !(de.before(despesa.getSolicitacaoPagamento().getCriacao())))
				{
					continue;
				}
				if(ate!=null  && !(ate.after(despesa.getSolicitacaoPagamento().getCriacao())))
				{
					continue;
				}

				TarefaDTO tarefaDTO = parseTaskToTarefa(task);
				tarefaDTO.setNomeSolicitante(despesa.getSolicitacaoPagamento().getUsuarioCriador().getNome());
				tarefaDTO.setNumeroNota(despesa.getSolicitacaoPagamento().getNumeroNotaFiscal());
				tarefaDTO.setNomeFornecedor(despesa.getSolicitacaoPagamento().getFornecedor().getNome());


				for(PapelUsuario p : despesa.getCentroCusto().getResponsaveis())
				{
					if(p.getPapel().getNome().startsWith("RESPONSAVEL"))
					{
						tarefaDTO.setNomeUsuario(p.getUsuario().getNome());
						tarefaDTO.setLogin(p.getUsuario().getLogin());
						break;
					}
				}

				tarefaDTO.setNomeCentroCusto(despesa.getCentroCusto().getNome());
				tarefaDTO.setNomeResponsavelNota(despesa.getCentroCusto().getArea().getResponsavelNotas().getUsuario().getNome());
				tarefaDTO.setCodigoCentroCusto(despesa.getCentroCusto().getCodigo());
				tarefaDTO.setNomeArea(despesa.getCentroCusto().getArea().getNome());
				tarefas.add(tarefaDTO);
			}

			return tarefas;
		}

}
