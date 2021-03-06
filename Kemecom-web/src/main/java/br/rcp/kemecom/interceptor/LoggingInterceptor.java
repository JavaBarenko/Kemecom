package br.rcp.kemecom.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 <p/>
 @author barenko
 */
@Logable
@Interceptor
public class LoggingInterceptor extends AbstractAOPInterceptor {

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {

        Logger logger = LoggerFactory.getLogger(getClassName(ctx));

        if(logger.isInfoEnabled()){
            String params = "";
            if(ctx.getParameters().length > 0){
                params = " com os argumentos: " + getParameters(ctx);
            }
            logger.info("Chamada ao metodo " + ctx.getMethod() + params + " ...");
        }

        Object theReturn = ctx.proceed();

        if(logger.isInfoEnabled()){
            String returnLog = "Chamada ao metodo " + ctx.getMethod() + " retornou";
            if(isMethodType(ctx, Void.class)){
                logger.info(returnLog);
            }else{
                logger.info(returnLog + ": " + ToStringBuilder.reflectionToString(theReturn));
            }
        }
        return theReturn;
    }
}
