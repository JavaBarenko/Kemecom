/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.interceptor;

import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.model.SecurityToken;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.model.db.User;
import br.rcp.kemecom.service.AuthenticatorService;
import java.lang.reflect.Field;
import java.util.List;
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

        final List<Field> requests = getFieldsByType(ctx, HttpServletRequest.class);

        if(requests.isEmpty()){
            throw new AuthException(getClassName(ctx) + " não suporta autenticacão!");
        }

        HttpServletRequest request = (HttpServletRequest) getFieldValue(ctx, requests.get(0));
        if(request == null){
            throw new AuthException("A requisição http está nula!");
        }

        SecurityToken securityToken = new SecurityToken(request.getHeader(Token.SECURITY_TOKEN));

        User currentUser = authenticator.validateAuth(securityToken);

        request.setAttribute(User.CURRENT_USER, currentUser);

        return ctx.proceed();
    }
}
