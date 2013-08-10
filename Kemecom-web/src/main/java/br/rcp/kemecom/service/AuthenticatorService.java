/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

/**
 *
 * @author barenko
 */
public interface AuthenticatorService {

    @POST
    String authenticate(@FormParam(value = "email") Email email, @FormParam(value = "password") Password password);

    @GET
    boolean isAuth(String token);
}
