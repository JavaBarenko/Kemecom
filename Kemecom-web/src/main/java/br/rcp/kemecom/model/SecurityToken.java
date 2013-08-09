/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

import org.bson.types.ObjectId;

/**
 *
 * @author barenko
 */
public class SecurityToken {

    private ObjectId tokenId;

    public SecurityToken(String tokenId) {
        try {
            this.tokenId = new ObjectId(tokenId);
        } catch (Exception e) {
            this.tokenId = null;
        }
    }

    public SecurityToken(ObjectId tokenId) {
        this.tokenId = tokenId;
    }

    public ObjectId getTokenId() {
        return tokenId;
    }

    @Override
    public String toString() {
        return String.valueOf(tokenId);
    }
}
