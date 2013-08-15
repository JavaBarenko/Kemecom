package br.rcp.kemecom.model;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 <p/>
 @author barenko
 */
public class Email implements Serializable {

    private String email;

    public Email() {
    }

    public Email(String email) {
        this.email = email == null ? "" : email.trim();
    }

    @JsonIgnore
    public boolean isValid() {
        return this.email != null && this.email.matches("(?i)\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
    }

    @Override
    @JsonProperty("email")
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !obj.getClass().isAssignableFrom(Email.class))
            return false;
        Email o = (Email) obj;
        if(email == null && o.email == null)
            return true;
        return email == null ? o.email.equals(email) : email.equals(o.email);
    }
}
