/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.db.Address;
import br.rcp.kemecom.model.db.Message;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 <p/>
 @author barenko
 */
public interface UserService {

    //com @PUT nao funciona! Da o erro: Request media type is not application/x-www-form-urlencoded
    @POST
    @Path("/new")
    Message addUser(@FormParam("email") Email email, @FormParam("password") Password password);

    @GET
    @Path("/me")
    Message getMe();

    @GET
    @Path("/list")
    Message getUsers();

    @DELETE
    Message removeUser();

    @POST
    @Path("/reset/password")
    Message sendRememberPassword(@FormParam("email") Email email);

    @POST
    @Path("/password")
    Message updatePassword(@FormParam("currPassword") Password currentPassword, @FormParam("password") Password newPassword);

    @POST
    @Path("/address")
    Message updateAddress(@FormParam("address") Address address);
}
