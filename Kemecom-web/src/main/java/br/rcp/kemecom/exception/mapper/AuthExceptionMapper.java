/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception.mapper;

import br.rcp.kemecom.exception.AuthException;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author barenko
 */
@Provider
public class AuthExceptionMapper extends AbstractApplicationExceptionMapper<AuthException> {
}
