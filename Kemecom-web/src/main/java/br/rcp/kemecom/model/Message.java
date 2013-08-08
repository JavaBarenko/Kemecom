/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

/**
 *
 * @author barenko
 */
public class Message {

    public enum Type {

        SUCCESS, ERROR
    }
    private Type type;
    private String message;
    private Object object;

    public Message(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Message(Type type, String message, Object object) {
        this.type = type;
        this.message = message;
        this.object = object;
    }

    public Message() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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
