/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.exception;

/**
 *
 * @author barenko
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(String message, Object ... params) {
        super(String.format(message, params));
    }

    public BusinessException(Throwable cause, String message, Object ... params) {
        super(String.format(message, params), cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message, Object ... params) {
        super(String.format(message, params), cause, enableSuppression, writableStackTrace);
    }
}
