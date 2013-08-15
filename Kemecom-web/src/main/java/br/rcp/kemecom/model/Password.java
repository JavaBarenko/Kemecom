package br.rcp.kemecom.model;

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
        return p1.equals(p2);
    }

    private String sha512Hex;

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
        String oSha = ((Password) other).sha512Hex;
        if(sha512Hex == null && oSha == null)
            return true;
        return sha512Hex == null ? oSha.equals(sha512Hex) : sha512Hex.equals(oSha);
    }

    @JsonIgnore
    public boolean isValid() {
        return sha512Hex != null;
    }

    private boolean isValid(String pwd) {
        return pwd != null && pwd.length() >= 5;
    }
}
