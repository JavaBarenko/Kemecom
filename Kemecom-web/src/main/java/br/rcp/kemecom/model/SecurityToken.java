package br.rcp.kemecom.model;

import java.io.Serializable;
import org.bson.types.ObjectId;

/**
 <p/>
 @author barenko
 */
public class SecurityToken implements Serializable {

    private ObjectId tokenId;

    public SecurityToken() {
    }

    public SecurityToken(String tokenId) {
        try{
            this.tokenId = new ObjectId(tokenId);
        }catch(Exception e){
            this.tokenId = null;
        }
    }

    public SecurityToken(ObjectId tokenId) {
        this.tokenId = tokenId;
    }

    public boolean isValid() {
        return tokenId != null;
    }

    public ObjectId getTokenId() {
        return tokenId;
    }

    public void setTokenId(ObjectId tk) {
        tokenId = tk;
    }

    @Override
    public String toString() {
        return tokenId == null ? "" : tokenId.toString();
    }
}
