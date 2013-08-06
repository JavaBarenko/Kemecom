/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.service;

import br.rcp.kemecom.exception.BusinessException;
import br.rcp.kemecom.model.User;
import com.google.code.morphia.Datastore;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author barenko
 */
@Path("/ws/user")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);
    @Inject
    Datastore ds;

    public UserService() {
    }

    public UserService(Datastore ds) {
        this.ds = ds;
    }

    @GET
    @Path("/{username}")
    public User getUserByUsername(@PathParam("username") String username) {
        if (log.isInfoEnabled()) {
            log.info("Obtendo usuario " + username + "...");
        }

        User u = ds.find(User.class).field("username").equal(username).get();

        if (log.isDebugEnabled()) {
            log.debug("Usuario " + username + " obtido: " + ToStringBuilder.reflectionToString(u));
        }

        return u;
    }

    @PUT
    @Path("/{username}")
    public User addUser(@PathParam("username") String username, String email, String password) {
        if (log.isInfoEnabled()) {
            log.info("Criando usuario " + username + "...");
        }

        User u = new User(username, email);
        u.setPassword(DigestUtils.sha512Hex(username));
        ds.save(u);

        if (log.isInfoEnabled()) {
            log.info("Usuario " + username + " criado: " + ToStringBuilder.reflectionToString(u));
        }

        return u;
    }

    @POST
    @Path("/{username}")
    public User updateUser(@PathParam("username") String username, String email) {
        if (log.isInfoEnabled()) {
            log.info("Atualizando usuario " + username + "...");
        }

        User u = getUserByUsername(username);
        if (u != null) {
            u.setEmail(email);
            ds.save(u);
            if (log.isInfoEnabled()) {
                log.info("Usuario " + username + " atualizado: " + ToStringBuilder.reflectionToString(u));
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Usuario " + username + " nao encontrado!");
            }
        }

        return u;
    }

    @DELETE
    @Path("/{username}")
    public User removeUser(@PathParam("username") String username) {
        if (log.isInfoEnabled()) {
            log.info("Excluindo usuario " + username + "...");
        }
        User u = ds.findAndDelete(ds.createQuery(User.class).field("username").equal(username));

        if (log.isInfoEnabled()) {
            log.info("Usuario " + username + " excluido: " + ToStringBuilder.reflectionToString(u));
        }

        return u;
    }

    @GET
    public List<User> getUsers() {
        if (log.isInfoEnabled()) {
            log.info("Obtendo lista de usuarios ...");
        }
        return ds.find(User.class).order("username").limit(100).asList();
    }

    @POST
    @Path("/{username}/password")
    public User updatePassword(@PathParam("username") String username, String currentPassword, String newPassword) {
        if (log.isInfoEnabled()) {
            log.info("Atualizando password do usuario " + username + " ...");
        }

        User u = getUserByUsername(username);
        if (u.getPassword().equals(DigestUtils.sha512Hex(currentPassword))) {
            ds.update(u, ds.createUpdateOperations(User.class).set("password", DigestUtils.sha512Hex(newPassword)));
            if (log.isInfoEnabled()) {
                log.info("Password do usuario " + username + " atualizado com sucesso!");
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("A senha atual do usuario " + username + " nao confere!");
            }
            throw new BusinessException("A senha atual nao confere!");
        }
        return u;
    }

    @PUT
    @Path("/{username}/password")
    public boolean validatePassword(@PathParam("username") String username, String currentPassword) {
        if (log.isInfoEnabled()) {
            log.info("Validando password do usuario " + username + " ...");
        }
        User u = getUserByUsername(username);

        boolean canAccess = u.getPassword().equals(DigestUtils.sha512Hex(currentPassword));

        if (canAccess) {
            if (log.isInfoEnabled()) {
                log.info("Password do usuario " + username + " validado com sucesso!");
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("A senha atual do usuario " + username + " nao confere!");
            }
        }

        return canAccess;
    }
}
