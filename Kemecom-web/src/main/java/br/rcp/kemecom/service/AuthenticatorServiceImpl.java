/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.exception.ApplicationException;
import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.model.db.User;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
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
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.Instant;
import org.joda.time.Minutes;

/**
 *
 * @author barenko
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
    public String authenticate(@FormParam("email") Email email, @FormParam("password") Password password) {
        User u = ds.find(User.class).field("email").equal(email.toString()).get();

        if (u == null || !u.getPassword().equals(password.toSha512Hex())) {
            throw new AuthException("E-Mail ou senha inválido(s)!");
        }

        invalidateCurrentTokens(email);

        String ipAddress = request.getRemoteAddr();
        Token tk = new Token(ipAddress, email);

        ds.save(tk);

        return tk.getId().toString();
    }

    private void invalidateCurrentTokens(Email email) {
        ds.findAndDelete(ds.createQuery(Token.class).field("email").equal(email.toString()));
    }

    @Override
    public boolean isAuth(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new AuthException("O token está vazio!");
        }

        Token tk = ds.find(Token.class).field("id").equal(new ObjectId(token)).get();

        if (tk == null) {
            throw new AuthException("Token inválido, efetue o login novamente!");
        }

        if (sessionDurationInMinutes >= Minutes.minutesBetween(Instant.now(), new Instant(tk.getLastAccessedAt())).getMinutes()) {
            ds.delete(tk);
            throw new AuthException("Token expirado, efetue o login novamente!").withHttpCode(419);
        }

        if (!request.getRemoteAddr().equals(tk.getIpAddress())) {
            throw new AuthException("Token inválido, efetue o login novamente!");
        }

        tk.setLastAccessedAt(new Date());
        ds.save(tk);

        return true;
    }
}
