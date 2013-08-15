package br.rcp.kemecom.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 <p/>
 @author barenko
 */
public class PasswordValidator implements ConstraintValidator<Password, br.rcp.kemecom.model.Password> {

    private String errorMessage;

    @Override
    public void initialize(Password constraintAnnotation) {
        errorMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(br.rcp.kemecom.model.Password value, ConstraintValidatorContext context) {
        boolean isValid = value != null && value.isValid();

        context.buildConstraintViolationWithTemplate(errorMessage);

        return isValid;
    }
}
