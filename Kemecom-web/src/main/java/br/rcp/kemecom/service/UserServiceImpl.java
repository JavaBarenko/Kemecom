/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.exception.ApplicationException;
import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.helper.EmailSender;
import br.rcp.kemecom.interceptor.Logable;
import br.rcp.kemecom.interceptor.Secure;
import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.SecurityToken;
import br.rcp.kemecom.model.db.Address;
import br.rcp.kemecom.model.db.Message;
import br.rcp.kemecom.model.db.User;
import com.google.code.morphia.Datastore;
import com.mongodb.MongoException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author barenko
 */
@Logable
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Produces({MediaType.APPLICATION_JSON})
@Path("/ws/user")
public class UserServiceImpl implements UserService {

    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Inject
    private Datastore ds;
    @Inject
    private EmailSender emailSender;
    @Context
    private HttpServletRequest request;

    public UserServiceImpl() {
    }

    public UserServiceImpl(Datastore ds, EmailSender es) {
        this.ds = ds;
        this.emailSender = es;
    }

    @Override
    public User getUserById(@PathParam("id") ObjectId id) {
        User u = userExistsAssertion(ds.get(User.class, id));
        u.setPassword("");
        return u;
    }

    @Override
    public Message addUser(@FormParam("email") Email email, @FormParam("password") Password password) {
        User u = new User(email.toString());
        u.setPassword(password.toSha512Hex());
        try {
            ds.save(u);
        } catch (MongoException.DuplicateKey dupKey) {
            return new Message(Message.ERROR, "Não foi possível criar o novo usuário: E-Mail já cadastrado.", new User());
        }

        u.setPassword("");
        return new Message(Message.SUCCESS, "Usuário criado com sucesso!", u);
    }

    @Override
    @Secure
    public User updateUser(@PathParam("id") ObjectId id, @FormParam("email") Email email, @FormParam("email") Address address) {
        User u = userExistsAssertion(ds.get(User.class, id));

        u.setEmail(email.toString());
        u.setAddress(address);
        ds.save(u);

        u.setPassword("");
        return u;
    }

    @Override
    @Secure
    public User removeUser(@PathParam("id") ObjectId id) {
        User u = userExistsAssertion(ds.get(User.class, id));
        ds.delete(u);
        u.setPassword("");
        return u;
    }

    @Override
    @Secure
    public List<User> getUsers(@CookieParam(SECURITY_TOKEN) SecurityToken security) {
        List<User> users = ds.createQuery(User.class).retrievedFields(false, "password").order("email").asList();
        return users;
    }

    @Override
    @Secure
    public Response updatePassword(@PathParam("id") ObjectId id, Password currentPassword, Password newPassword) {
        User u = userExistsAssertion(ds.get(User.class, id));

        if (!u.getPassword().equals(currentPassword.toSha512Hex())) {
            throw new AuthException("A senha atual não confere!");
        }

        ds.update(u, ds.createUpdateOperations(User.class).set("password", newPassword.toSha512Hex()));

        return Response.ok().build();
    }

    @Override
    public Response sendRememberPassword(@FormParam("email") Email email) {
        User u = userExistsAssertion(ds.find(User.class, "email", email.toString()).get());

        Password pwd = Password.generateRandom(10);

        try {
            emailSender.createEmail()
                    .setHtmlMsg(String.format("<html><h1>Kemecom</h1><br>Sua nova senha é %s</html>", pwd))
                    .setTextMsg(String.format("Kemecom: Sua nova senha é %s", pwd))
                    .addTo(u.getEmail(), u.getEmail())
                    .setFrom("admin@keme.com", "Kemecom")
                    .setSubject("[KEMECOM] Sua nova senha chegou")
                    .send();

            u.setPassword(pwd.toSha512Hex());
            ds.save(u);
        } catch (Exception e) {
            throw new ApplicationException("Desculpe, não foi possível resetar sua senha. Tente mais tarde.").withAjaxCallbackObject(new User());
        }

        return Response.ok().build();
    }

    private User userExistsAssertion(User u) throws ApplicationException {
        if (u == null) {
            throw new ApplicationException("O usuário não foi encontrado na base de dados").withHttpCode(200).withAjaxCallbackObject(new User());
        }
        return u;
    }
}
