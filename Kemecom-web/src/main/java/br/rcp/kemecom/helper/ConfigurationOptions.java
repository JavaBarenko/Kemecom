/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.helper;

/**
 *
 * @author barenko
 */
public interface ConfigurationOptions {
    String MEMCACHED_HOSTS = "memcached.hosts";

    String FACEBOOK_APPID = "facebook.appId";
    String FACEBOOK_APPSECRET = "facebook.appSecret";
    String FACEBOOK_PERMISSIONS = "facebook.permissions";
    String FACEBOOK_JSON_STORE = "facebook.jsonStore";

    String DB_MONGO_HOST = "db.mongo.host";
    String DB_MONGO_PORT = "db.mongo.port";
    String DB_MONGO_DBNAME = "db.mongo.dbname";
    String DB_MONGO_USERNAME = "db.mongo.username";
    String DB_MONGO_PASSWORD = "db.mongo.password";

    String TEST_DB_MONGO_HOST = "test.db.mongo.host";
    String TEST_DB_MONGO_PORT = "test.db.mongo.port";
    String TEST_DB_MONGO_DBNAME = "test.db.mongo.dbname";
    String TEST_DB_MONGO_USERNAME = "test.db.mongo.username";
    String TEST_DB_MONGO_PASSWORD = "test.db.mongo.password";

    String EMAIL_SMTP_HOSTNAME = "email.smtp.hostname";
    String EMAIL_SMTP_PORT = "email.smtp.port";
    String EMAIL_SMTP_USERNAME = "email.smtp.username";
    String EMAIL_SMTP_PASSWORD = "email.smtp.password";
}
