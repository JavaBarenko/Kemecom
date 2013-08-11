/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.SecurityToken;
import br.rcp.kemecom.model.db.Message;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.model.db.User;
import com.google.code.morphia.Datastore;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.joda.time.Instant;
import org.joda.time.Minutes;

/**
 <p/>
 @author barenko
 */
@ApplicationScoped
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/ws/auth")
public class AuthenticatorServiceImpl implements AuthenticatorService {

    @Inject
    private Datastore ds;

    @Context
    private HttpServletRequest request;

    private Integer sessionDurationInMinutes = 30;

    public AuthenticatorServiceImpl() {
    }

    public AuthenticatorServiceImpl(Datastore ds, HttpServletRequest request) {
        this.ds = ds;
        this.request = request;
    }

    @Override
    public Message authenticate(@FormParam("email") Email email, @FormParam("password") Password password) {
        User u = ds.find(User.class, "email", email).get();

        if(u == null || !Password.equals(u.getPassword(), password)){
            throw new AuthException("E-Mail ou senha inválido(s)!");
        }

        invalidateCurrentTokens(email);

        String ipAddress = request.getRemoteAddr();
        Token tk = new Token(ipAddress, email);

        ds.save(tk);

        return Message.ok("Autenticado com sucesso!", tk.getId().toString());
    }

    private void invalidateCurrentTokens(Email email) {
        ds.findAndDelete(ds.createQuery(Token.class).field("email").equal(email));
    }

    @Override
    public User validateAuth(SecurityToken token) {
        if(token == null || !token.isValid()){
            throw new AuthException("Token inválido, efetue o login novamente!");
        }

        Token tk = ds.get(Token.class, token.getTokenId());

        if(tk == null){
            throw new AuthException("Token inválido, efetue o login novamente!");
        }

        if(isExpired(tk)){
            ds.delete(tk);
            throw new AuthException("Token expirado, efetue o login novamente!");
        }

        if(isADiferentIPAddress(tk)){
            throw new AuthException("Token inválido, efetue o login novamente!");
        }

        tk.setLastAccessedAt(new Date());
        ds.save(tk);

        User currentUser = ds.find(User.class, "email", tk.getEmail()).get();

        return currentUser;
    }

    @Override
    public Message logout(@FormParam("token") SecurityToken token) {
        ds.delete(Token.class, token.getTokenId());
        return Message.ok("Usuário deslogado com sucesso");
    }

    private boolean isExpired(Token tk) {
        return sessionDurationInMinutes < Minutes.minutesBetween(new Instant(tk.getLastAccessedAt()), Instant.now()).getMinutes();
    }

    private boolean isADiferentIPAddress(Token tk) {
        return !request.getRemoteAddr().equals(tk.getIpAddress());
    }
}
