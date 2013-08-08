/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.bson.types.ObjectId;

/**
 *
 * @author barenko
 */
@Entity("auth")
public class Token {

    @Id
    private ObjectId id;
    @NotNull
    @Indexed(unique = true, dropDups = true, name = "auth_unique_token")
    private String token;
    @NotNull
    private String ipAddress;
    @NotNull
    @Indexed(unique = true, dropDups = true, name = "auth_unique_email")
    private String email;
    private Date createdAt;
    private Date lastAccessedAt;

    public Token() {
        createdAt = new Date();
    }

    public Token(String token, String ipAddress, String email) {
        this.token = token;
        this.ipAddress = ipAddress;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
