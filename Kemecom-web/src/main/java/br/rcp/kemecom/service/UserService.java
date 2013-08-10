/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.SecurityToken;
import br.rcp.kemecom.model.db.Address;
import br.rcp.kemecom.model.db.Message;
import br.rcp.kemecom.model.db.User;
import java.util.List;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.bson.types.ObjectId;

/**
 <p/>
 @author barenko
 */
public interface UserService {

    static final String SECURITY_TOKEN = "kemecom";

    //com @PUT nao funciona! Da o erro: Request media type is not application/x-www-form-urlencoded
    @POST
    Message addUser(@FormParam(value = "email") Email email, @FormParam(value = "password") Password password);

    @GET
    @Path(value = "/{id}")
    User getUserById(@PathParam(value = "id") ObjectId id);

    @GET
    List<User> getUsers(@CookieParam(value = SECURITY_TOKEN) SecurityToken security);

    @DELETE
    @Path(value = "/{id}")
    User removeUser(@PathParam(value = "id") ObjectId id);

    @POST
    @Path(value = "/reset/password")
    Message sendRememberPassword(@FormParam(value = "email") Email email);

    @POST
    @Path(value = "/{id}/password")
    Message updatePassword(@PathParam(value = "id") ObjectId id, Password currentPassword, Password newPassword);

    @POST
    @Path(value = "/{id}")
    User updateUser(@PathParam(value = "id") ObjectId id, @FormParam(value = "email") Email email, @FormParam(value = "address") Address address);
}
