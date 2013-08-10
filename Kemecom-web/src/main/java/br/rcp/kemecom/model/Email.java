/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

/**
 <p/>
 @author barenko
 */
public class Email {

    private String email;

    public Email(String email) {
        this.email = email == null ? "" : email.trim();
    }

    public boolean isValid() {
        return this.email.matches("(?i)\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
    }

    @Override
    public String toString() {
        return email;
    }
}
