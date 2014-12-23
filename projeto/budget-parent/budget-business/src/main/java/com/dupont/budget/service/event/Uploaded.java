package com.dupont.budget.service.event;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.dupont.budget.model.AbstractEntity;

/**
 * Anotação do evento de upload de arquivos da aplicação.
 * 
 * @author joel
 *
 */
@Qualifier
@Target({ PARAMETER, FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Uploaded {
	/**
	 * Tipo da entidade que será persistido com base no upload.
	 * 
	 * @return o tipo da classe da entidade.
	 */
	Class<?> value() default AbstractEntity.class;
}
