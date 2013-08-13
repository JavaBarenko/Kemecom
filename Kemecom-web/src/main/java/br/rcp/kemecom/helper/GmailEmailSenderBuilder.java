/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.helper;

/**
 *
 * @author barenko
 */
public class GmailEmailSenderBuilder {
        public static GmailEmailSenderBuilder sender() {
        return new GmailEmailSenderBuilder();
    }
    private String smtp;
    private int port;
    private String password;
    private String username;

    private GmailEmailSenderBuilder() {
    }

    public GmailEmailSenderBuilder useSmtp(String smtp) {
        this.smtp = smtp;
        return this;
    }

    public GmailEmailSenderBuilder inPort(int port) {
        this.port = port;
        return this;
    }

    public GmailEmailSenderBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public GmailEmailSenderBuilder withPassword(String password) {
        if (password != null)
            this.password = password;
        return this;
    }

    public GmailEmailSender build() {
        return new GmailEmailSender(smtp, port, username, password);
    }
}
