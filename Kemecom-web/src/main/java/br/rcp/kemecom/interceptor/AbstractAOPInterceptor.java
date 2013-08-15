package br.rcp.kemecom.interceptor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import javax.interceptor.InvocationContext;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.list.dsl.Matcher;

/**
 <p/>
 @author barenko
 */
public abstract class AbstractAOPInterceptor {

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
