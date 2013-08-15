package br.rcp.kemecom.helper;

import org.apache.commons.mail.HtmlEmail;

/**
 <p/>
 @author barenko
 */
public interface EmailSender {

    HtmlEmail createEmail();
}
