/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model.db;

import com.google.code.morphia.annotations.Embedded;
import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 <p/>
 @author barenko
 */
@Embedded
public class Address implements Serializable {

    private String zipCode;

    private String street;

    private String number;

    private String city;

    private String state;

    private String neighborhood;

    public Address() {
    }

    public Address(String zipCode) {
        this.zipCode = zipCode;
    }

    @JsonProperty("zipcode")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @JsonProperty("street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("neighborhood")
    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborn) {
        this.neighborhood = neighborn;
    }
}
