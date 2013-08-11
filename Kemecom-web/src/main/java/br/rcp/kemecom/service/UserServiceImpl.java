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
import br.rcp.kemecom.model.db.Address;
import br.rcp.kemecom.model.db.Message;
import br.rcp.kemecom.model.db.User;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.ValidationException;
import com.mongodb.MongoException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.RandomStringUtils;

/**
 <p/>
 @author barenko
 */
@Logable
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Produces(MediaType.APPLICATION_JSON)
@Path("/ws/user")
public class UserServiceImpl implements UserService {

    @Inject
    private Datastore ds;

    @Inject
    private EmailSender emailSender;

    //Utilizado no Interceptor de Login para poder validar o token!
    @Context
    private HttpServletRequest request;

    public UserServiceImpl() {
    }

    public UserServiceImpl(Datastore ds, EmailSender es) {
        this.ds = ds;
        this.emailSender = es;
    }

    @Override
    @Secure
    public Message getMe() {
        User u = getCurrentUser().withoutPassword();
        return Message.ok("", u);
    }

    @Override
    public Message addUser(@FormParam("email") Email email, @FormParam("password") Password password) {
        User u = new User(email);
        u.setPassword(password);
        try{
            ds.save(u);
        }catch(ValidationException v){
            throw new ApplicationException(v).withAjaxCallbackObject(new User(u.getEmail()));
        }catch(MongoException.DuplicateKey dupKey){
            throw new ApplicationException(dupKey, "Não foi possível criar o novo usuário: E-Mail já cadastrado.").withAjaxCallbackObject(new User(u.getEmail()));
        }

        return Message.ok("Usuário criado com sucesso!", u.withoutPassword());
    }

    @Override
    @Secure
    public Message updateAddress(@FormParam("address") Address address) {
        User u = getCurrentUser();
        u.setAddress(address);
        ds.save(u);

        return Message.ok("Endereço atualizado com sucesso!", u.withoutPassword());
    }

    @Override
    @Secure
    public Message removeUser() {
        ds.delete(getCurrentUser());
        return Message.ok("Usuário excluído com sucesso!");
    }

    @Override
    @Secure
    public Message getUsers() {
        List<User> users = ds.createQuery(User.class).retrievedFields(false, "password").order("email").asList();
        return Message.ok("", users);
    }

    @Override
    @Secure
    public Message updatePassword(Password currentPassword, Password newPassword) {
        User u = getCurrentUser();

        if(!Password.equals(u.getPassword(), currentPassword)){
            throw new AuthException("A senha atual não confere!");
        }

        ds.update(u, ds.createUpdateOperations(User.class).set("password", newPassword));

        return Message.ok("Senha alterada com sucesso!");
    }

    @Override
    public Message sendRememberPassword(@FormParam("email") Email email) {
        User u = userExistsAssertion(ds.find(User.class, "email", email).get());

        String pwd = RandomStringUtils.randomAlphanumeric(10);

        try{
            emailSender.createEmail()
                    .setHtmlMsg(String.format("<html><h1>Kemecom</h1><br>Sua nova senha é %s</html>", pwd))
                    .setTextMsg(String.format("Kemecom: Sua nova senha é %s", pwd))
                    .addTo(u.getEmail().toString(), u.getEmail().toString())
                    .setFrom("admin@keme.com", "Kemecom")
                    .setSubject("[KEMECOM] Sua nova senha chegou")
                    .send();

            u.setPassword(new Password(pwd));
            ds.save(u);
        }catch(Exception e){
            throw new ApplicationException(e, "Desculpe, não foi possível resetar sua senha. Tente mais tarde.").withAjaxCallbackObject(new User());
        }

        return Message.ok("Senha resetada com sucesso! Em instantes você receberá um email com sua nova senha.");
    }

    private User userExistsAssertion(User u) throws ApplicationException {
        if(u == null){
            throw new ApplicationException("O usuário não foi encontrado na base de dados").withHttpCode(200).withAjaxCallbackObject(new User());
        }
        return u;
    }

    private User getCurrentUser() {
        return (User) request.getAttribute(User.CURRENT_USER);
    }
}
