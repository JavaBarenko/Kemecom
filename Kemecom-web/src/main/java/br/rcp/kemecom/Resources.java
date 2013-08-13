/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom;

import static br.rcp.kemecom.helper.ConfigurationOptions.*;
import br.rcp.kemecom.helper.EmailSender;
import br.rcp.kemecom.helper.GmailEmailSenderBuilder;
import br.rcp.kemecom.helper.Memcached;
import br.rcp.kemecom.helper.MongoDatastore;
import br.rcp.kemecom.helper.MongoDatastoreBuilder;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.model.db.User;
import com.google.code.morphia.Datastore;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * @author barenko
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
    @Produces
    @SessionScoped
    private Facebook facebook;

    @Produces
    @ApplicationScoped
    private Configuration config;

    public Resources() throws Exception {
        if (log.isInfoEnabled()) {
            log.info("Carregando recursos ...");
        }

        loadConfiguration();
        loadMemcached();
        loadDatastore();
        loadEmailSender();
        loadFacebookClient();

        if (log.isInfoEnabled()) {
            log.info("Recursos carregados com sucesso!");
        }
    }

    private void loadMemcached() throws IOException {
        memcached = new Memcached(config.getStringArray(MEMCACHED_HOSTS));
    }

    private void loadFacebookClient() throws IOException {
        ConfigurationBuilder confBuilder = new ConfigurationBuilder();
        facebook4j.conf.Configuration conf = confBuilder
                .setOAuthAppId(config.getString(FACEBOOK_APPID))
                .setOAuthAppSecret(config.getString(FACEBOOK_APPSECRET))
                .setOAuthPermissions(config.getString(FACEBOOK_PERMISSIONS))
                .setJSONStoreEnabled(config.getBoolean(FACEBOOK_JSON_STORE))
                .build();
        facebook = new FacebookFactory(conf).getInstance();
    }

    private void loadDatastore() throws UnknownHostException {
//        MongoDatastore mongo = new MongoDatastore("127.0.0.1", 27017, "KemecomProject", null, null);
        MongoDatastore mongo = MongoDatastoreBuilder.mongo()
                .atHost(config.getString(DB_MONGO_HOST))
                .inPort(config.getInt(DB_MONGO_PORT))
                .forDbName(config.getString(DB_MONGO_DBNAME))
                .withUsername(config.getString(DB_MONGO_USERNAME))
                .withPassword(config.getString(DB_MONGO_PASSWORD))
                .build();

        ds = mongo.registryEntity(User.class, Token.class)
                .enableValidation()
                .getDataStore();
    }

    private void loadEmailSender() {
        emailSender = GmailEmailSenderBuilder.sender()
                .useSmtp(config.getString(EMAIL_SMTP_HOSTNAME))
                .inPort(config.getInt(EMAIL_SMTP_PORT))
                .withUsername(config.getString(EMAIL_SMTP_USERNAME))
                .withPassword(config.getString(EMAIL_SMTP_PASSWORD))
                .build();
    }

    private void loadConfiguration() throws ConfigurationException {
        config = new PropertiesConfiguration("config.properties");
    }
}
