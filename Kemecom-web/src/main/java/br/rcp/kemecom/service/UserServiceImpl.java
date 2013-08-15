package br.rcp.kemecom.service;

import br.rcp.kemecom.exception.ApplicationException;
import br.rcp.kemecom.exception.AuthException;
import br.rcp.kemecom.helper.EmailSender;
import br.rcp.kemecom.interceptor.Memorable;
import br.rcp.kemecom.interceptor.Forgettable;
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
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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

    public UserServiceImpl(Datastore ds, EmailSender es, HttpServletRequest req) {
        this.ds = ds;
        this.emailSender = es;
        this.request = req;
    }

    @Override
    @Secure
    @Memorable("me")
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
    @Forgettable("me")
    public Message updateUser(MultivaluedMap<String, String> formParams) {
        User u = getCurrentUser();
        u.setName(formParams.getFirst("name"));
        Address address = u.getAddress();
        if(address == null){
            address = new Address();
            u.setAddress(address);
        }
        address.setZipCode(formParams.getFirst("zipCode"));
        address.setNumber(formParams.getFirst("number"));
        address.setStreet(formParams.getFirst("street"));
        address.setCity(formParams.getFirst("city"));
        address.setNeighborhood(formParams.getFirst("neighborhood"));
        address.setState(formParams.getFirst("state"));
        ds.save(u);

        return Message.ok("Usuário atualizado com sucesso!", u.withoutPassword());
    }

    @Override
    @Secure
    @Forgettable("me")
    public Message removeUser() {
        ds.delete(getCurrentUser());
        return Message.ok("Usuário excluído com sucesso!");
    }

//    @Override
//    @Secure
//    @Cacheable
//    public Message getUsers() {
//        List<User> users = ds.createQuery(User.class).retrievedFields(false, "password").order("email").asList();
//        return Message.ok("", users);
//    }
    @Override
    @Secure
    public Message updatePassword(Password currentPassword, Password newPassword) {
        User u = getCurrentUser();

        if(!Password.equals(u.getPassword(), currentPassword)){
            throw new AuthException("A senha atual não confere!");
        }
        if(Password.equals(new Password(""), newPassword)){
            throw new ApplicationException("A senha deve ter no mínimo 5 caracteres!");
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
