package com.dupont.budget.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author bandrade
 *
 */
public class TarefaDTO  implements Serializable{
	private static final long serialVersionUID = -6225511321669739605L;

	private long id;

	private long processInstanceId;

	private String name;

	private String subject;

	private String description;

	private String status;

	private int priority;

	private boolean skipable;

	private String actualOwner;

	private String createdBy;

	private Date createdOn;

	private Date activationTime;

	private Date expirationTime;

	private String processId;

	private int processSessionId;

	private Date prazo;

	private List<String> potentialOwners;

	private String nomeUsuario;

	private String login;

	private String nomeCentroCusto;

	private String codigoCentroCusto;

	private String nomeArea;

	private String numeroNota;

	private String nomeFornecedor;

	private String nomeSolicitante;

	private String nomeResponsavelNota;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public StatusTarefaEnum getStatus() {
		return StatusTarefaEnum.findByMeaning(status);
	}

	public void setStatus(StatusTarefaEnum status) {
		this.status = status.getMeaning();
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isSkipable() {
		return skipable;
	}

	public void setSkipable(boolean skipable) {
		this.skipable = skipable;
	}

	public String getActualOwner() {
		return actualOwner;
	}

	public void setActualOwner(String actualOwner) {
		this.actualOwner = actualOwner;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public int getProcessSessionId() {
		return processSessionId;
	}

	public void setProcessSessionId(int processSessionId) {
		this.processSessionId = processSessionId;
	}

	public List<String> getPotentialOwners() {
		return potentialOwners;
	}

	public void setPotentialOwners(List<String> potentialOwners) {
		this.potentialOwners = potentialOwners;
	}

	public Date getPrazo() {
		return prazo;
	}

	public void setPrazo(Date prazo) {
		this.prazo = prazo;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNomeCentroCusto() {
		return nomeCentroCusto;
	}

	public void setNomeCentroCusto(String nomeCentroCusto) {
		this.nomeCentroCusto = nomeCentroCusto;
	}

	public String getCodigoCentroCusto() {
		return codigoCentroCusto;
	}

	public void setCodigoCentroCusto(String codigoCentroCusto) {
		this.codigoCentroCusto = codigoCentroCusto;
	}

	public String getNomeArea() {
		return nomeArea;
	}

	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}

	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getNomeSolicitante() {
		return nomeSolicitante;
	}

	public void setNomeSolicitante(String nomeSolicitante) {
		this.nomeSolicitante = nomeSolicitante;
	}

	public String getNomeResponsavelNota() {
		return nomeResponsavelNota;
	}

	public void setNomeResponsavelNota(String nomeResponsavelNota) {
		this.nomeResponsavelNota = nomeResponsavelNota;
	}






}
