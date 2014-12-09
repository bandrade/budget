package com.dupont.budget.dto;

import java.io.Serializable;

public enum StatusTarefaEnum implements Serializable {

	Criada("Created"), 
	Pendente("Ready"), 
	Reservada("Reserved"), 
	EmAndamento("InProgress"), 
	Suspensa("Suspended"), 
	Completa("Completed"), 
	Falha("Failed"), 
	Erro("Error"), 
	Abandonada("Exited"), 
	Obsoleta("Obsolete");

	private String meaning;

	private StatusTarefaEnum(String meaning) {
		this.meaning = meaning;
	}

	/**
	 * @return Retorna o atributo meaning.
	 */
	public String getMeaning() {
		return meaning;
	}

	public String toString() {

		if (this.equals(StatusTarefaEnum.Criada)) {
			return "Criada";
		} else if (this.equals(StatusTarefaEnum.Pendente)) {
			return "Pendente";
		} else if (this.equals(StatusTarefaEnum.Reservada)) {
			return "Reservada";
		} else if (this.equals(StatusTarefaEnum.EmAndamento)) {
			return "Em Andamento";
		} else if (this.equals(StatusTarefaEnum.Suspensa)) {
			return "Suspensa";
		} else if (this.equals(StatusTarefaEnum.Completa)) {
			return "Completa";
		} else if (this.equals(StatusTarefaEnum.Falha)) {
			return "Falha";
		} else if (this.equals(StatusTarefaEnum.Erro)) {
			return "Erro";
		} else if (this.equals(StatusTarefaEnum.Abandonada)) {
			return "Abandonada";
		} else if (this.equals(StatusTarefaEnum.Obsoleta)) {
			return "Obsoleta";
		} else {
			return "Status Não Definido em TaskStatus";
		}
	}

	public static StatusTarefaEnum findByMeaning(String meaning) {
		for (StatusTarefaEnum v : values()) {
			if (v.meaning.equals(meaning)) {
				return v;
			}
		}
		return null;
	}

}

/*
 * public enum TaskStatusInternal { Created, Ready, Reserved, InProgress,
 * Suspended, Completed, Failed, Error, Exited, Obsolete }
 * 
 * private static Map<TaskStatusInternal, TaskStatus> mapping = new
 * HashMap<TaskStatusInternal, TaskStatus>();
 * 
 * static { mapping.put(TaskStatusInternal.Created, TaskStatus.Criada);
 * mapping.put(TaskStatusInternal.Ready, TaskStatus.Pronta);
 * mapping.put(TaskStatusInternal.Reserved, TaskStatus.Reservada);
 * mapping.put(TaskStatusInternal.InProgress, TaskStatus.EmAndamento);
 * mapping.put(TaskStatusInternal.Suspended, TaskStatus.Suspensa);
 * mapping.put(TaskStatusInternal.Completed, TaskStatus.Completa);
 * mapping.put(TaskStatusInternal.Failed, TaskStatus.Falha);
 * mapping.put(TaskStatusInternal.Error, TaskStatus.Erro);
 * mapping.put(TaskStatusInternal.Exited, TaskStatus.Abandonada);
 * mapping.put(TaskStatusInternal.Obsolete, TaskStatus.Obsoleta); }
 * 
 * private static Map<TaskStatus, TaskStatusInternal> reverseMapping = new
 * HashMap<TaskStatus, TaskStatusInternal>();
 * 
 * static { reverseMapping.put(TaskStatus.Criada, TaskStatusInternal.Created);
 * reverseMapping.put(TaskStatus.Pronta, TaskStatusInternal.Ready);
 * reverseMapping.put(TaskStatus.Reservada, TaskStatusInternal.Reserved);
 * reverseMapping.put(TaskStatus.EmAndamento, TaskStatusInternal.InProgress);
 * reverseMapping.put(TaskStatus.Suspensa, TaskStatusInternal.Suspended);
 * reverseMapping.put(TaskStatus.Completa, TaskStatusInternal.Completed);
 * reverseMapping.put(TaskStatus.Falha, TaskStatusInternal.Failed);
 * reverseMapping.put(TaskStatus.Erro, TaskStatusInternal.Error);
 * reverseMapping.put(TaskStatus.Abandonada, TaskStatusInternal.Exited);
 * reverseMapping.put(TaskStatus.Obsoleta, TaskStatusInternal.Obsolete); }
 * 
 * public static TaskStatusInternal getInternalTaskStatus( TaskStatus
 * externalStatus) { TaskStatusInternal internalStatus = null;
 * 
 * internalStatus = reverseMapping.get(externalStatus);
 * 
 * return internalStatus; }
 * 
 * public static TaskStatusInternal getInternalTaskStatusByInternalName( String
 * statusName) {
 * 
 * TaskStatusInternal internalStatus = null;
 * 
 * if (statusName.toLowerCase().equals("created")) { internalStatus =
 * TaskStatusInternal.Created; }
 * 
 * else if (statusName.toLowerCase().equals("ready")) { internalStatus =
 * TaskStatusInternal.Ready; }
 * 
 * else if (statusName.toLowerCase().equals("reserved")) { internalStatus =
 * TaskStatusInternal.Reserved; }
 * 
 * else if (statusName.toLowerCase().equals("inprogress")) { internalStatus =
 * TaskStatusInternal.InProgress; }
 * 
 * else if (statusName.toLowerCase().equals("suspended")) { internalStatus =
 * TaskStatusInternal.Suspended; }
 * 
 * else if (statusName.toLowerCase().equals("completed")) { internalStatus =
 * TaskStatusInternal.Completed; }
 * 
 * else if (statusName.toLowerCase().equals("failed")) { internalStatus =
 * TaskStatusInternal.Failed; }
 * 
 * else if (statusName.toLowerCase().equals("error")) { internalStatus =
 * TaskStatusInternal.Error; }
 * 
 * else if (statusName.toLowerCase().equals("exited")) { internalStatus =
 * TaskStatusInternal.Exited; }
 * 
 * else if (statusName.toLowerCase().equals("obsolete")) { internalStatus =
 * TaskStatusInternal.Obsolete; }
 * 
 * return internalStatus; }
 * 
 * public static TaskStatus getTaskStatusByInternalName(String statusName) {
 * TaskStatusInternal internalStatus =
 * getInternalTaskStatusByInternalName(statusName);
 * 
 * TaskStatus status = mapping.get(internalStatus);
 * 
 * return status; }
 */