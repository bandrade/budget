package com.dupont.budget.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.dupont.budget.web.validators.ComboValidator")
public class ComboValidator implements Validator {

        @Override
        public void validate(FacesContext arg0, UIComponent arg1, Object value)
                        throws ValidatorException {
                if (value == null || value.toString().equals("0")) {
                        FacesMessage msg = new FacesMessage("Campo " + arg1.getId()
                                        + " é obrigatório!", "Selecione um valor para o campo "
                                        + arg1.getId());
                        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        throw new ValidatorException(msg);

                }

        }

}