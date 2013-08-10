/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception.mapper;

import com.google.code.morphia.query.ValidationException;
import javax.ws.rs.ext.Provider;

/**
 <p/>
 @author barenko
 */
@Provider
public class ValidationExceptionMapper extends AbstractGenericExceptionMapper<ValidationException> {
}
