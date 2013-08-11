/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.SecurityToken;
import br.rcp.kemecom.model.db.Message;
import br.rcp.kemecom.model.db.User;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

/**
 <p/>
 @author barenko
 */
public interface AuthenticatorService {

    @POST
    Message authenticate(@FormParam(value = "email") Email email, @FormParam(value = "password") Password password);

    @GET
    User validateAuth(SecurityToken token);

    @DELETE
    Message logout(SecurityToken token);
}
