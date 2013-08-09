/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.db;

/**
 *
 * @author barenko
 */
public class Message {

    public static interface Type {

        static final String SUCCESS = "SUCCESS";
        static final String ERROR = "ERROR";
    }
    private String type;
    private String message;
    private Object object;

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Message(String type, String message, Object object) {
        this.type = type;
        this.message = message;
        this.object = object;
    }

    public Message() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public <T> T getObject() {
        return (T) object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
