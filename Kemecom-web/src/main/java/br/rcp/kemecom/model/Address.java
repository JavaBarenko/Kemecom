/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

import com.google.code.morphia.annotations.Embedded;

/**
 *
 * @author barenko
 */
@Embedded
public class Address {

    private String zipCode;
    private String street;
    private String number;
    private String city;
    private String state;
    private String neighborn;

    public Address() {
    }

    public Address(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNeighborn() {
        return neighborn;
    }

    public void setNeighborn(String neighborn) {
        this.neighborn = neighborn;
    }
}
