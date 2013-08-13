/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.helper;

import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.db.Message;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.service.AuthenticatorService;
import com.google.code.morphia.Datastore;
import facebook4j.Facebook;
import facebook4j.User;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 <p/>
 @author barenko
 */
@Path("ws/auth/facebook")
public class FacebookService {

    @Context
    private HttpServletRequest request;

    @Inject
    private Facebook facebook;

    @Inject
    private Datastore ds;

    @Inject
    private AuthenticatorService authenticatorService;

    @GET
    public Response signin() {
        request.getSession().setAttribute("facebook", facebook);

        try{
            String callbackTo = request.getRequestURL().append("/callback").toString();
            URI uri = new URI(facebook.getOAuthAuthorizationURL(callbackTo));
            return Response.temporaryRedirect(uri).build();
        }catch(URISyntaxException ex){
            throw new AuthException(ex, "Falha ao tentar logar via facebook");
        }
    }

    @GET
    @Path("/callback")
    public Response callback() {
        Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
        String oauthCode = request.getParameter("code");
        try{
            facebook.getOAuthAccessToken(oauthCode);
            Token tk = loginByFace(facebook.getMe());
//            return Message.ok("Autenticado com sucesso!", tk.getId().toString());
            URI uri = new URI(String.format("/pages/index.html#%s=%s", Token.SECURITY_TOKEN, tk.getId().toString()));
            return Response.temporaryRedirect(uri).build();
        }catch(Exception ex){
            throw new AuthException(ex, "Falha ao tentar logar via facebook");
        }
    }

    private Token loginByFace(User fbUser) {
        Email userEmail = new Email(fbUser.getEmail());
        br.rcp.kemecom.model.db.User user = ds.find(br.rcp.kemecom.model.db.User.class, "email", userEmail).get();
        if(user == null){
            user = new br.rcp.kemecom.model.db.User(userEmail);
            user.setName(fbUser.getName());
            user.setPassword(new Password(RandomStringUtils.randomAlphanumeric(10)));
            ds.save(user);
        }else{
            if(StringUtils.isEmpty(user.getName())){
                user.setName(fbUser.getName());
                ds.save(user);
            }
        }
        return authenticatorService.validateToken(user.getEmail());
    }
}
