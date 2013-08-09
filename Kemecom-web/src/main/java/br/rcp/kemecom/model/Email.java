/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

/**
 *
 * @author barenko
 */
public class Email {

    private String email;

    public Email(String tokenId) {
        this.email = tokenId;
    }

    @Override
    public String toString() {
        return email != null ? email.toLowerCase().trim() : "";
    }
}
