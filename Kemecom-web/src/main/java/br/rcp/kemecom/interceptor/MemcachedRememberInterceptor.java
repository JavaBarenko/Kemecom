package br.rcp.kemecom.interceptor;

import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.helper.Memcached;
import br.rcp.kemecom.model.db.Token;
import java.util.concurrent.Future;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

/**
 <p/>
 @author barenko
 */
@Memorable
@Interceptor
public class MemcachedRememberInterceptor extends AbstractAOPInterceptor {

    @Inject
    private Memcached memcached;

    @AroundInvoke
    public Object cached(InvocationContext ctx) throws Exception {
        String key = buildKey(ctx);

        Future future = memcached.get(key);

        Object theReturn = future.get();

        if(theReturn == null){
            theReturn = ctx.proceed();
            memcached.add(key, theReturn);
        }

        return theReturn;
    }

    protected String buildKey(InvocationContext ctx) throws AuthException {
        Memorable memorable = ctx.getMethod().getAnnotation(Memorable.class);

        HttpServletRequest request = getHttpServletRequest(ctx);
        String key = request.getHeader(Token.SECURITY_TOKEN) + memorable.value();
        return key;
    }
}
