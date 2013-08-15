package br.rcp.kemecom.helper;

import java.net.UnknownHostException;

/**
 Builder do {@link MongoDatastore}.
 <p/>
 @author barenko
 */
public class MongoDatastoreBuilder {

    public static MongoDatastoreBuilder mongo() {
        return new MongoDatastoreBuilder();
    }

    private String host;

    private int port;

    private String dbName;

    private String username;

    private String password;

    private MongoDatastoreBuilder() {
    }

    public MongoDatastoreBuilder atHost(String host) {
        this.host = host;
        return this;
    }

    public MongoDatastoreBuilder inPort(int port) {
        this.port = port;
        return this;
    }

    public MongoDatastoreBuilder forDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public MongoDatastoreBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public MongoDatastoreBuilder withPassword(String password) {
        if(password != null)
            this.password = password;
        return this;
    }

    public MongoDatastore build() throws UnknownHostException {
        return new MongoDatastore(host, port, dbName, username, password);
    }
}
