/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

import com.google.code.morphia.annotations.Transient;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 <p/>
 @author barenko
 */
public class Password {

    public static boolean equals(Password p1, Password p2) {
        if(p1 == p2){
            return true;
        }
        if(p1 == null || p2 == null){
            return false;
        }
        return p1.sha512Hex.equals(p2.sha512Hex);
    }

    private String sha512Hex;

    //TODO remover esse campo de teste depois....
    @Transient
    private String password;

    public Password() {
    }

    public Password(String password) {
        setPassword(password);
    }

    public void setPassword(String password) {
        if(isValid(password))
            this.sha512Hex = DigestUtils.sha512Hex(password);
    }

    @Override
    @JsonProperty("sha512Hex")
    public String toString() {
        return sha512Hex == null ? "" : sha512Hex.trim();
    }

    @Override
    public boolean equals(Object other) {
        if(other == this)
            return true;
        if(other == null || !other.getClass().isAssignableFrom(Password.class))
            return false;
        return sha512Hex.equals(((Password) other).sha512Hex);
    }

    @JsonIgnore
    public boolean isValid() {
        return sha512Hex != null;
    }

    private boolean isValid(String pwd) {
        return pwd != null && pwd.length() >= 5;
    }
}
