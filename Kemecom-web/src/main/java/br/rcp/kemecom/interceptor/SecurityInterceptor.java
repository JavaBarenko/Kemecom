package br.rcp.kemecom.interceptor;

import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.model.SecurityToken;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.model.db.User;
import br.rcp.kemecom.service.AuthenticatorService;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

/**
 <p/>
 @author barenko
 */
@Secure
@Interceptor
public class SecurityInterceptor extends AbstractAOPInterceptor {

    @Inject
    AuthenticatorService authenticator;

    @AroundInvoke
    public Object auth(InvocationContext ctx) throws Exception {

        HttpServletRequest request = getHttpServletRequest(ctx);

        if(request == null){
            throw new AuthException("A requisição http está nula!");
        }

        SecurityToken securityToken = new SecurityToken(request.getHeader(Token.SECURITY_TOKEN));

        User currentUser = authenticator.validateAuth(securityToken);

        request.setAttribute(User.CURRENT_USER, currentUser);

        return ctx.proceed();
    }
}
