/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.helper;

import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author barenko
 */
public interface EmailSender {

    HtmlEmail createEmail();
}
