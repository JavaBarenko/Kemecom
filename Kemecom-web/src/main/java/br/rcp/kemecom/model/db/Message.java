/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.db;

/**
 <p/>
 @author barenko
 */
public class Message {

    public static enum Type {

        SUCCESS(true), ERROR(false);

        Type(boolean b) {
            this.value = b;
        }

        private boolean value;

        public boolean getValue() {
            return value;
        }
    }

    public static Message ok(String message) {
        return new Message(Type.SUCCESS, message);
    }

    public static Message ok(String message, Object obj) {
        return new Message(Type.SUCCESS, message, obj);
    }

    public static Message error(String message) {
        return new Message(Type.ERROR, message);
    }

    public static Message error(String message, Object obj) {
        return new Message(Type.ERROR, message, obj);
    }

    private Boolean success;

    private String message;

    private Object object;

    public Message(Type type, String message) {
        this(type, message, null);
    }

    public Message(Type type, String message, Object object) {
        this.success = type.getValue();
        this.message = message;
        this.object = object;
    }

    public Message() {
    }

    public Boolean isSuccessful() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
