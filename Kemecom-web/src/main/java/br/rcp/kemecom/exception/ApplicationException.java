/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception;

/**
 *
 * @author barenko
 */
public class ApplicationException extends RuntimeException {

    private Object callbackObject;
    protected Integer httpCode = 500;

    public ApplicationException(String message, Object... params) {
        super(String.format(message, params));
    }

    public ApplicationException(Throwable cause, String message, Object... params) {
        super(String.format(message, params), cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message, Object... params) {
        super(String.format(message, params), cause, enableSuppression, writableStackTrace);
    }

    public <T extends ApplicationException> T withAjaxCallbackObject(Object o) {
        this.callbackObject = o;
        return (T) this;
    }

    public Object getCallbackObject() {
        return callbackObject;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public <T extends ApplicationException> T withHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
        return (T) this;
    }
}
