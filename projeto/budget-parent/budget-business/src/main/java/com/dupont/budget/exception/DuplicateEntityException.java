package com.dupont.budget.exception;

/**
 * Exceção lançada caso tente-se criar uma entidade já existente.
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@javax.ejb.ApplicationException(rollback=true)
public class DuplicateEntityException extends ApplicationException {

	private static final long serialVersionUID = -2416857973151811220L;
	
	private Object entity;

	public DuplicateEntityException(Object entity) {
		super("Entidade " + ( entity == null ? "< NÃO INFORMADA > " :  entity.getClass().getName() ) + " já existe no meio persistente.");
		
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}
}
