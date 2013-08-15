package br.rcp.kemecom.helper;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 <p/>
 @author barenko
 */
public class GmailEmailSender implements EmailSender {

    private Logger log = LoggerFactory.getLogger(GmailEmailSender.class);

    private String hostName;

    private final int smtpPort;

    private final String password;

    private final String username;

    public GmailEmailSender(String smtpHostName, int smtpPort, String username, String password) {
        if(log.isInfoEnabled()){
            log.info("Iniciando o emissor de emails no SMTP: " + smtpHostName + ":" + smtpPort + "...");
        }

        this.hostName = smtpHostName;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;

        if(log.isInfoEnabled()){
            log.info("Emissor de emails iniciado com sucesso!");
        }
    }

    @Override
    public HtmlEmail createEmail() {
        if(log.isDebugEnabled()){
            log.debug("criando email ...");
        }

        HtmlEmail email = new HtmlEmail();
        email.setHostName(hostName);
        email.setSmtpPort(smtpPort);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        return email;
    }
}
