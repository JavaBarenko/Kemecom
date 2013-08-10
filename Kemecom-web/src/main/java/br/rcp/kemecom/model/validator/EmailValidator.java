/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 <p/>
 @author barenko
 */
public class EmailValidator implements ConstraintValidator<Email, br.rcp.kemecom.model.Email> {

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(br.rcp.kemecom.model.Email value, ConstraintValidatorContext context) {
        return value != null && value.isValid();
    }
}
