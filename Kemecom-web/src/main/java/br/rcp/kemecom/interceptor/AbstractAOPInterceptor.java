package br.rcp.kemecom.interceptor;

import br.rcp.kemecom.exception.AuthException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.list.dsl.Matcher;

/**
 <p/>
 @author barenko
 */
public abstract class AbstractAOPInterceptor {

    protected String getMethodName(InvocationContext ctx) {
        return ctx.getMethod().toString().replaceFirst(".+ ", "");
    }

    protected String getClassName(InvocationContext ctx) {
        return ctx.getTarget().getClass().getName().replaceFirst("\\$Proxy.*$", "");
    }

    protected String getParameters(InvocationContext ctx) {
        return Arrays.toString(ctx.getParameters());
    }

    protected boolean isMethodType(InvocationContext ctx, Class type) {
        return type == ctx.getMethod().getReturnType();
    }

    protected List<Field> getFieldsByType(InvocationContext ctx, Class type) {
        return new Mirror().on(ctx.getTarget().getClass()).reflectAll().fields().matching(new FieldByClassMatcher(type));
    }

    protected <T> T getFieldValue(InvocationContext ctx, Field f) {
        return (T) new Mirror().on(ctx.getTarget()).get().field(f);
    }

    protected HttpServletRequest getHttpServletRequest(InvocationContext ctx) throws AuthException {
        final List<Field> requests = getFieldsByType(ctx, HttpServletRequest.class);
        if(requests.isEmpty()){
            throw new AuthException(getClassName(ctx) + " não possui um atributo HttpServletRequest!");
        }
        HttpServletRequest request = (HttpServletRequest) getFieldValue(ctx, requests.get(0));
        return request;
    }

    private static class FieldByClassMatcher implements Matcher<Field> {

        private final Class theClass;

        public FieldByClassMatcher(Class theClass) {
            this.theClass = theClass;
        }

        @Override
        public boolean accepts(Field f) {
            return f.getType().isAssignableFrom(theClass);
        }
    }
}
