/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.db;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 <p/>
 @author barenko
 */
public class Message {

    public static final boolean SUCCESS = true;

    public static final boolean ERROR = false;

    private Boolean success;

    private String message;

    private Object object;

    public Message(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Message(Boolean success, String message, Object object) {
        this.success = success;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
