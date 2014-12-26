package com.dupont.budget.exception;

/**
 * Exceção lançada caso tente-se criar uma entidade já existente.
 *
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@javax.ejb.ApplicationException(rollback = true)
public class ExistingNameRuntimeException extends ApplicationRuntimeException {

	private static final long serialVersionUID = -2416857973151811220L;

	private Object entity;

	public ExistingNameRuntimeException(Object entity) {
		super(String.format("Ja existe um %s cadastrado no sistema com os mesmos dados.", (entity == null ? "< NÃO INFORMADA > " : entity.getClass().getSimpleName())));
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}
}
