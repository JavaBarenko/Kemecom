/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.helper;

import br.rcp.kemecom.model.Token;
import br.rcp.kemecom.model.User;
import br.rcp.kemecom.service.UserService;
import com.google.code.morphia.Datastore;
import java.util.UUID;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author barenko
 */
@Path("/ws/auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
public class AuthenticatorService {

    private Logger log = LoggerFactory.getLogger(AuthenticatorService.class);
    @Inject
    private Datastore ds;
    @Context
    private HttpServletRequest request;

    public AuthenticatorService() {
    }

    public AuthenticatorService(Datastore ds) {
        this.ds = ds;
    }

    @POST
    public Token authenticate(@FormParam("email") String email, @FormParam("password") String password) {
        User u = ds.find(User.class).field("email").equal(email).get();
        if (u != null) {
            invalidateCurrentTokens(email);

            String ipAddress =
                    Token tk = new Token(UUID.randomUUID(), ipAddress, email);
            ds.save(tk);
        }
    }

    private void invalidateCurrentTokens(String email) {
        ds.findAndDelete(ds.createQuery(Token.class).field("email").equal(email));
    }
}
