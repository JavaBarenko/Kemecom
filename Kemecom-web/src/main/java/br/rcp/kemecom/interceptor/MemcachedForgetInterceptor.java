package br.rcp.kemecom.interceptor;

import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.helper.Memcached;
import br.rcp.kemecom.model.db.Token;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

/**
 <p/>
 @author barenko
 */
@Forgettable
@Interceptor
public class MemcachedForgetInterceptor extends AbstractAOPInterceptor {

    @Inject
    private Memcached memcached;

    @AroundInvoke
    public Object cached(InvocationContext ctx) throws Exception {
        String key = buildKey(ctx);

        memcached.delete(key);

        return ctx.proceed();
    }

    protected String buildKey(InvocationContext ctx) throws AuthException {
        Forgettable forgettable = ctx.getMethod().getAnnotation(Forgettable.class);

        HttpServletRequest request = getHttpServletRequest(ctx);
        String key = request.getHeader(Token.SECURITY_TOKEN) + forgettable.value();
        return key;
    }
}
