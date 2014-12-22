package com.dupont.budget.web.actions;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.dupont.budget.service.BudgetService;
import com.dupont.budget.service.bpms.BPMSProcessService;
import com.dupont.budget.web.util.FacesUtils;


@ConversationScoped
@Named
public class AuditarBudgetAction implements Serializable {
	@Inject
	private Logger logger;

	@Inject
	private FacesUtils facesUtils;

	private Long idTarefa;

	private Long idInstanciaProcesso;

	@Inject
	private Conversation conversation;

	private String ano;

	@Inject
    protected BPMSProcessService bpmsProcesso;

	@Inject
	protected BudgetService budgetService;

	@PostConstruct
	private void init()
	{
		if(conversation.isTransient())
				conversation.begin();

	}

	public void obterDadosBudget()
	{
		try {
			ano = (String)bpmsProcesso.obterVariavelProcesso(idInstanciaProcesso, "ano");
		} catch (Exception e) {
			facesUtils.addErrorMessage("Erro ao obter os dados da tarefa");
			logger.error("erro ao obter as variaveis de processo",e);
		}
	}


	public String getAno() {
		return ano;
	}


	public void setAno(String ano) {
		this.ano = ano;
	}

	public Long getIdTarefa() {
		return idTarefa;
	}

	public void setIdTarefa(Long idTarefa) {
		this.idTarefa = idTarefa;
	}

	public Long getIdInstanciaProcesso() {
		return idInstanciaProcesso;
	}

	public void setIdInstanciaProcesso(Long idInstanciaProcesso) {
		this.idInstanciaProcesso = idInstanciaProcesso;
	}



}
