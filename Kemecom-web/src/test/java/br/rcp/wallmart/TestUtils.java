/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.wallmart;

import br.rcp.kemecom.helper.EmailSender;
import br.rcp.kemecom.model.db.User;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.mail.HtmlEmail;
import static org.mockito.Mockito.*;

/**
 <p/>
 @author barenko
 */
public class TestUtils {

    public static HttpServletRequest fakeRequest(User user) {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getAttribute(User.CURRENT_USER)).thenReturn(user);
        return req;
    }

    public static EmailSender fakeEmailSender() {
        EmailSender sender = mock(EmailSender.class);
        HtmlEmail email = mock(HtmlEmail.class);
        when(sender.createEmail()).thenReturn(email);
        return sender;
    }
}
