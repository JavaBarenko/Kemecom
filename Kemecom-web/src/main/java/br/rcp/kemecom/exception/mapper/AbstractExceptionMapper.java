/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception.mapper;

import br.rcp.kemecom.exception.ApplicationException;
import br.rcp.kemecom.model.db.Message;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author barenko
 */
public class AbstractExceptionMapper<T extends ApplicationException> implements ExceptionMapper<T> {

    @Override
    public Response toResponse(T exception) {
        Logger log = LoggerFactory.getLogger(getClass());
        if (log != null && log.isErrorEnabled()) {
            log.error("Erro interceptado pelo ExceptionMapper", exception);
        }

        Message msg = new Message(Message.Type.ERROR, exception.getMessage(), exception.getCallbackObject());
        Response r = null;
        try {
            r = Response.status(exception.getHttpCode()).entity(msg).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (log != null && log.isInfoEnabled()) {
            log.info("Response: " + r.getEntity());
        }
        return r;
    }
}
