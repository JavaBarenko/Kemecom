/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception.mapper;

import br.rcp.kemecom.model.db.Message;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 <p/>
 @author barenko
 */
public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

    protected Response toResponse(T exception, Integer httpCode, Message message) {
        Logger log = LoggerFactory.getLogger(getClass());
        if(log.isErrorEnabled()){
            log.error("Erro interceptado pelo " + getClass().getSimpleName(), exception);
            log.info("Response: " + message);
        }
        return Response.status(httpCode).entity(message).build();
    }
}
