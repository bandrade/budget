package com.dupont.budget.web.converters;

import java.io.Serializable;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.dupont.budget.model.AbstractEntity;

@FacesConverter("com.dupont.EntityConverter")
public class EntityConverter implements Converter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext ctx, UIComponent component,
			String value) {
		if (value != null) {
			return this.getAttributesFrom(component).get(value);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext ctx, UIComponent component,
			Object value) {

		if (value != null && !"".equals(value)) {

			AbstractEntity<?> entity = (AbstractEntity<?>) value;

			// adiciona item como atributo do componente
			this.addAttribute(component, entity);

			Serializable codigo = entity.getId();
			if (codigo != null) {
				return String.valueOf(codigo);
			} else {
				return null;
			}
		}

		return (String) value;
	}

	protected void addAttribute(UIComponent component, AbstractEntity<?> o) {
		if (o.getId() != null) {
			this.getAttributesFrom(component).put(o.getId().toString(), o);
		}
	}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}


}
