/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.db;

import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Version;
import org.bson.types.ObjectId;

/**
 <p/>
 @author barenko
 */
@Entity("users")
public class User {

    @Id
    private ObjectId id;

    @Version
    Long version;

    @Indexed(name = "unique_email", dropDups = true, unique = true)
    @br.rcp.kemecom.model.validator.Email
    private Email email;

    @br.rcp.kemecom.model.validator.Password
    private Password password;

    @Embedded
    private Address address;

    public User() {
    }

    public User(Email email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public User withoutPassword() {
        this.password = null;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        final User other = (User) obj;
        if(this.id != other.id && (this.id == null || !this.id.equals(other.id))){
            return false;
        }
        if((this.email == null) ? (other.email != null) : !this.email.equals(other.email)){
            return false;
        }
        return true;
    }
}
