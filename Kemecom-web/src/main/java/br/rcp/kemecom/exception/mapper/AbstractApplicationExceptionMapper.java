/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception.mapper;

import br.rcp.kemecom.exception.ApplicationException;
import br.rcp.kemecom.model.db.Message;
import javax.ws.rs.core.Response;

/**
 <p/>
 @author barenko
 */
public abstract class AbstractApplicationExceptionMapper<T extends ApplicationException> extends AbstractExceptionMapper<T> {

    @Override
    public Response toResponse(T exception) {
        Message msg = new Message(Message.ERROR, exception.getMessage(), exception.getCallbackObject());
        return toResponse(exception, exception.getHttpCode(), msg);
    }
}
