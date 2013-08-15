package br.rcp.kemecom.exception.mapper;

import br.rcp.kemecom.model.db.Message;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.vidageek.mirror.dsl.Mirror;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 Captura todas as exceptions nao tratadas e as envelopa em uma {@link Message} contendo a mensagem de erro.
 <p/>
 @author barenko
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Logger log = LoggerFactory.getLogger(getClass());
        if(log.isErrorEnabled()){
            log.error("Erro interceptado pelo " + getClass().getSimpleName(), exception);
        }

        Message msg = Message.error(messageFormatter(exception.getMessage()));

        Integer httpCode = getHttpCode(exception, log);

        if(log.isErrorEnabled()){
            log.info(String.format("Response: [%d] %s", httpCode, msg));
        }

        return Response.status(httpCode).entity(msg).build();
    }

    private String messageFormatter(String rawMessage) {
        String errorMsg = StringUtils.defaultIfEmpty(rawMessage, "Erro Interno no servidor. Tente mais tarde.");
        return errorMsg.replaceFirst(" \\('[^']+'\\)$", "");
    }

    private Integer getHttpCode(Exception exception, Logger log) {
        try{
            return (Integer) new Mirror().on(exception).invoke().method("getHttpCode").withoutArgs();
        }catch(Exception e){
            if(log.isTraceEnabled()){
                log.trace("Nao foi encontrado o metodo getHttpCode em " + exception.getClass().getName(), e);
            }
            return 200;
        }
    }
}
