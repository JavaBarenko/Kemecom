/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom;

import br.rcp.kemecom.helper.EmailSender;
import br.rcp.kemecom.helper.GmailEmailSender;
import br.rcp.kemecom.helper.Memcached;
import br.rcp.kemecom.helper.MongoDatastore;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.model.db.User;
import com.google.code.morphia.Datastore;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 <p/>
 @author barenko
 */
public class Resources {

    private Logger log = LoggerFactory.getLogger(Resources.class);

    @Produces
    @ApplicationScoped
    private Datastore ds;

    @Produces
    @ApplicationScoped
    private Memcached memcached;

    @Produces
    @ApplicationScoped
    private EmailSender emailSender;

    public Resources() throws Exception {
        if(log.isInfoEnabled()){
            log.info("Carregando recursos ...");
        }

        loadMemcached();
        loadDatastore();
        loadEmailSender();

        if(log.isInfoEnabled()){
            log.info("Recursos carregados com sucesso!");
        }
    }

    private void loadMemcached() throws IOException {
        memcached = new Memcached("127.0.0.1:11211");
    }

    private void loadDatastore() throws UnknownHostException {
        MongoDatastore mongo = new MongoDatastore("KemecomProject");
        ds = mongo.registryEntity(User.class).registryEntity(Token.class).enableValidation().getDataStore();
    }

    private void loadEmailSender() {
        emailSender = new GmailEmailSender("smtp.googlemail.com", 465, "barenko@gmail.com", "4qrq3odq35qh9");
    }
}
