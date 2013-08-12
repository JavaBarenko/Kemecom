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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 <p/>
 @author barenko
 */
@Entity(value = "users", noClassnameStored = true)
public class User {

    public static final String CURRENT_USER = "CurrentUser";

    @Id
    private ObjectId id;

    @Version
    Long version;

    String name;

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

    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonIgnore
    public ObjectId getId() {
        return id;
    }

    @JsonProperty("id")
    public String getSerializedId() {
        return id == null ? null : id.toString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @JsonProperty("email")
    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("password")
    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public User withoutPassword() {
        User u = clone();
        u.setPassword(null);
        return u;
    }

    @Override
    protected User clone() {
        User u = new User();
        u.setAddress(address);
        u.setEmail(email);
        u.setName(name);
        u.setId(id);
        u.setPassword(password);
        return u;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if(id != null)
            sb.append("id:'").append(id.toString()).append("', ");
        if(email != null)
            sb.append("id:'").append(email.toString()).append("', ");
        if(address != null)
            sb.append("address:").append(address.toString()).append(", ");
        sb.append("}");
        return sb.toString();
    }
}
