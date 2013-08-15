package br.rcp.kemecom.exception;

/**
 Manipula todas as exceptions relacionadas a seguranca e acesso
 <p/>
 @author barenko
 */
public class AuthException extends ApplicationException {

    protected Integer httpCode = 401;

    public AuthException(String message, Object... params) {
        super(message, params);
    }

    public AuthException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message, Object... params) {
        super(cause, enableSuppression, writableStackTrace, message, params);
    }
}
