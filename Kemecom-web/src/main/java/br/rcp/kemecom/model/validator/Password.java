/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.validator;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {

    String message() default "A senha deve conter no mínimo 5 caracteres";

    public abstract Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};
}
