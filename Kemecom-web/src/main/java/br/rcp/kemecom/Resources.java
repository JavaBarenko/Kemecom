/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom;

import br.rcp.kemecom.db.Memcached;
import br.rcp.kemecom.db.MongoDatastore;
import br.rcp.kemecom.model.User;
import com.google.code.morphia.Datastore;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author barenko
 */
public class Resources {

    private Logger log = LoggerFactory.getLogger(Resources.class);
    @SuppressWarnings("unused")
    @Produces
    @ApplicationScoped
    private Datastore ds;
    @SuppressWarnings("unused")
    @Produces
    @ApplicationScoped
    private Memcached memcached;

    public Resources() throws Exception {
        if (log.isInfoEnabled()) {
            log.info("Carregando recursos ...");
        }

        loadMemcached();
        loadDatastore();

        if (log.isInfoEnabled()) {
            log.info("Recursos carregados com sucesso!");
        }
    }

    private void loadMemcached() throws IOException {
        memcached = new Memcached("127.0.0.1:11211");
    }

    private void loadDatastore() throws UnknownHostException {
        MongoDatastore mongo = new MongoDatastore("KemecomProject");
        ds = mongo.registryEntity(User.class).enableValidation().getDataStore();
    }
}
