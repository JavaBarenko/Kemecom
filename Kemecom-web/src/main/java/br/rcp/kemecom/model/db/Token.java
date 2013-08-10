/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.db;

import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.SecurityToken;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Version;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.bson.types.ObjectId;

/**
 <p/>
 @author barenko
 */
@Entity("auth")
public class Token {

    @Id
    private ObjectId id;

    @Version
    Long version;

    @NotNull
    private String ipAddress;

    @Indexed(unique = true, dropDups = true, name = "auth_unique_email")
    @br.rcp.kemecom.model.validator.Email
    private Email email;

    private Date createdAt;

    private Date lastAccessedAt;

    public Token() {
        createdAt = new Date();
        lastAccessedAt = new Date();
    }

    public Token(String ipAddress, Email email) {
        this();
        this.ipAddress = ipAddress;
        this.email = email;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(Date lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}