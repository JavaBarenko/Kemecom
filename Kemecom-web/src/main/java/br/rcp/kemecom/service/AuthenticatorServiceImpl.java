package br.rcp.kemecom.service;

import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.helper.ConfigurationOptions;
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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.configuration.Configuration;
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
//TODO colocar para configuracao

    private Integer sessionDurationInMinutes = 30;

    @Inject
    private Configuration config;

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

        Token tk = validateToken(email);

        return Message.ok("Autenticado com sucesso!", tk.getId().toString());
    }

    private void invalidateCurrentTokens(Email email) {
        ds.findAndDelete(ds.createQuery(Token.class).field("email").equal(email));
    }

    @Override
    public Token validateToken(Email email) {
        invalidateCurrentTokens(email);

        String ipAddress = request.getRemoteAddr();
        Token tk = new Token(ipAddress, email);

        ds.save(tk);
        return tk;
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
    public Message isLogged(@HeaderParam(Token.SECURITY_TOKEN) SecurityToken token) {
        return Message.ok("Usuário logado", validateAuth(getToken(token)).withoutPassword());
    }

    @Override
    public Message logout(@HeaderParam(Token.SECURITY_TOKEN) SecurityToken token) {
        ds.delete(Token.class, getToken(token).getTokenId());
        request.getSession().invalidate();
        return Message.ok("Usuário deslogado com sucesso");
    }

    private boolean isExpired(Token tk) {
        int sessionDuration = config.getInt(ConfigurationOptions.SESSION_DURATION_IN_MINUTES);
        int sessionLive = Minutes.minutesBetween(new Instant(tk.getLastAccessedAt()), Instant.now()).getMinutes();
        return sessionDuration < sessionLive;
    }

    private boolean isADiferentIPAddress(Token tk) {
        return !request.getRemoteAddr().equals(tk.getIpAddress());
    }

    /*
     FIXME Nao sei porque, mas o @HeaderParam nao consegue recuperar o token..
     entao, enquanto ele nao acha, obtenho da propria request.

     Criei esse método para encapsular o workaround deste problema.
     Assim que funcionar, este metodo pode ser excluido e utilizar a atribuicao direta.
     */
    @Deprecated
    private SecurityToken getToken(SecurityToken token) {
        return (token != null && token.isValid())
                ? token : new SecurityToken(request.getHeader(Token.SECURITY_TOKEN));
    }
}
