/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.exception.BusinessException;
import br.rcp.kemecom.helper.EmailSender;
import br.rcp.kemecom.model.Address;
import br.rcp.kemecom.model.Message;
import br.rcp.kemecom.model.User;
import com.google.code.morphia.Datastore;
import com.mongodb.MongoException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author barenko
 */
@Path("/ws/user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Produces({MediaType.APPLICATION_JSON})
public class UserService {

    public static final String SESSION_SECURITY = "kemecom";
    private Logger log = LoggerFactory.getLogger(UserService.class);
    @Inject
    private Datastore ds;
    @Inject
    private EmailSender emailSender;
    @Context
    private HttpServletRequest request;

    public UserService() {
    }

    public UserService(Datastore ds, EmailSender es) {
        this.ds = ds;
        this.emailSender = es;
    }

    @GET
    @Path("/{id}")
    public User getUserById(@PathParam("id") String id) {
        if (log.isInfoEnabled()) {
            log.info("Obtendo usuario " + id + "...");
        }

        User u = ds.find(User.class).field("id").equal(id).get();

        if (log.isDebugEnabled()) {
            log.debug("Usuario " + id + " obtido: " + ToStringBuilder.reflectionToString(u));
        }

        return u;
    }

    @PUT
    public Message addUser(@FormParam("email") String email, @FormParam("password") String password) {
        if (log.isInfoEnabled()) {
            log.info("Criando usuario " + email + "...");
        }

        String userEmail = email.toLowerCase().trim();
        User u = new User(userEmail);
        u.setPassword(DigestUtils.sha512Hex(password.trim()));
        try {
            ds.save(u);
        } catch (MongoException.DuplicateKey dupKey) {
            if (log.isInfoEnabled()) {
                log.info("Nao pode criar novo usuario, pois o usuario " + userEmail + " ja existe");
            }
            return new Message(Message.Type.ERROR, "Não foi possível criar o novo usuário: E-Mail já cadastrado.", new User());
        }

        if (log.isInfoEnabled()) {
            log.info("Usuario " + email + " criado: " + ToStringBuilder.reflectionToString(u));
        }
        u.setPassword("");
        return new Message(Message.Type.SUCCESS, "Usuário criado com sucesso!", u);
    }

    @POST
    @Path("/{id}")
    public User updateUser(@PathParam("id") String id, @FormParam("email") String email, @FormParam("email") Address address) {
        if (log.isInfoEnabled()) {
            log.info("Atualizando usuario " + id + "...");
        }

        User u = getUserById(id);
        if (u != null) {
            u.setEmail(email);
            u.setAddress(address);
            ds.save(u);
            if (log.isInfoEnabled()) {
                log.info("Usuario " + id + " atualizado: " + ToStringBuilder.reflectionToString(u));
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Usuario " + id + " nao encontrado!");
            }
        }

        return u;
    }

    @DELETE
    @Path("/{id}")
    public User removeUser(@PathParam("id") String id) {
        if (log.isInfoEnabled()) {
            log.info("Excluindo usuario " + id + "...");
        }
        User u = ds.findAndDelete(ds.createQuery(User.class).field("id").equal(id));

        if (log.isInfoEnabled()) {
            log.info("Usuario " + id + " excluido: " + ToStringBuilder.reflectionToString(u));
        }

        return u;
    }

    @GET
    public List<User> getUsers(@CookieParam(SESSION_SECURITY) String security) {
        if (!isAuth(security)) {
            if (log.isInfoEnabled()) {
                log.info("Tentativa de acessar o servico getUsers sem estar logado");
            }
            return Collections.emptyList();
        }

        if (log.isInfoEnabled()) {
            log.info("Obtendo lista de usuarios ...");
        }
        return ds.find(User.class).order("email").asList();
    }

    @POST
    @Path("/{id}/password")
    public User updatePassword(@PathParam("id") String id, String currentPassword, String newPassword) {
        if (log.isInfoEnabled()) {
            log.info("Atualizando password do usuario " + id + " ...");
        }

        User u = getUserById(id);
        if (u.getPassword().equals(DigestUtils.sha512Hex(currentPassword))) {
            ds.update(u, ds.createUpdateOperations(User.class).set("password", DigestUtils.sha512Hex(newPassword)));
            if (log.isInfoEnabled()) {
                log.info("Password do usuario " + id + " atualizado com sucesso!");
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("A senha atual do usuario " + id + " nao confere!");
            }
            throw new BusinessException("A senha atual nao confere!");
        }
        return u;
    }

    @POST
    @Path("/login")
    public Response login(String email, String currentPassword) {
        if (log.isInfoEnabled()) {
            log.info("Efetuando login do usuario " + email + " ...");
        }
        User u = getUserById(email);

        if (u != null) {
            boolean canAccess = u.getPassword().equals(DigestUtils.sha512Hex(currentPassword));

            if (canAccess) {
                if (log.isInfoEnabled()) {
                    log.info("Usuario " + email + " logado com sucesso!");
                }
                return Response.ok(new Message(Message.Type.SUCCESS, "Bem vindo, " + u.getEmail() + "!")).cookie(createSessionCookie()).build();
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("A senha atual do usuario " + email + " nao confere!");
                }
                return Response.ok(new Message(Message.Type.ERROR, "E-Mail ou senha inválido(s)!")).build();
            }
        }
        if (log.isWarnEnabled()) {
            log.warn("Nao foi encontrado o usuario " + email + " na base de dados");
        }
        return Response.ok(new Message(Message.Type.ERROR, "E-Mail ou senha inválido(s)!")).build();
    }

    @POST
    @Path("/reset/password")
    public Message sendRememberPassword(@FormParam("email") String email) {
        if (log.isInfoEnabled()) {
            log.info("Resetando a senha do usuario " + email + "...");
        }

        User u = ds.find(User.class).field("email").equal(email).get();

        if (u != null) {
            String pwd = RandomStringUtils.randomAlphanumeric(10);

            try {
                emailSender.createEmail()
                        .setHtmlMsg(String.format("<html><h1>Kemecom</h1><br>Sua nova senha é %s</html>", pwd))
                        .setTextMsg(String.format("Kemecom: Sua nova senha é %s", pwd))
                        .addTo(u.getEmail(), u.getEmail())
                        .setFrom("admin@keme.com", "Kemecom")
                        .setSubject("[KEMECOM] Sua nova senha chegou")
                        .send();

                u.setPassword(DigestUtils.sha512Hex(pwd));
                ds.save(u);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Erro ao tentar resetar a senha do usuario " + email, e);
                }
                return new Message(Message.Type.ERROR, "Desculpe, não foi possível resetar sua senha. Tente mais tarde.", new User());
            }

            if (log.isInfoEnabled()) {
                log.info("A senha do usuario " + email + " foi resetada com sucesso");
            }
            return new Message(Message.Type.SUCCESS, "Senha resetada com sucesso! Em instantes você receberá um email com sua nova senha.");
        }

        if (log.isWarnEnabled()) {
            log.warn("O usuario " + email + " nao foi encontrado na base de dados");
        }
        return new Message(Message.Type.ERROR, "E-Mail não encontrado na nossa base de dados", new User());
    }

    private NewCookie createSessionCookie() {
        String value = UUID.randomUUID().toString();
        request.getSession().setAttribute(SESSION_SECURITY, value);
        //TODO: trocar domain para o domain de producao
        return new NewCookie(SESSION_SECURITY, value, "/", "http://localhost", "", 3600, true);
    }

    private boolean isAuth(String security) {
        if (StringUtils.isEmpty(security)) {
            return false;
        }
        String securitySession = (String) request.getSession().getAttribute(SESSION_SECURITY);
        if (StringUtils.isEmpty(securitySession)) {
            return false;
        }
        return security.equals(securitySession);
    }
}
