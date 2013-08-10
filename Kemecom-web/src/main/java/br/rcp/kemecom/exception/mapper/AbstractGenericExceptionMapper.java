/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception.mapper;

import br.rcp.kemecom.model.db.Message;
import javax.ws.rs.core.Response;

/**
 <p/>
 @author barenko
 */
public abstract class AbstractGenericExceptionMapper<T extends Throwable> extends AbstractExceptionMapper<T> {

    @Override
    public Response toResponse(T exception) {
        Message msg = new Message(Message.ERROR, exception.getMessage());
        return toResponse(exception, 500, msg);
    }
}
