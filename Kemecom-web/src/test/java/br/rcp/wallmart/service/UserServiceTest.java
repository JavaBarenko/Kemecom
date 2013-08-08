/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.wallmart.service;

import br.rcp.kemecom.service.UserService;
import br.rcp.kemecom.helper.MongoDatastore;
import br.rcp.kemecom.model.User;
import com.google.code.morphia.Datastore;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author barenko
 */
public class UserServiceTest {

    private static Datastore ds;

    @Before
    public void setUp() {
        ds.getCollection(User.class).drop();
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        ds = new MongoDatastore("wallmartProjectTest").getDataStore();
    }

    @AfterClass
    public static void afterClass() {
        ds.getMongo().close();
    }
//    @Test
//    public void listUsersMustBeEmpty() {
//        UserService us = new UserService(ds);
//        assertTrue(us.getUsers().isEmpty());
//    }
//
//    @Test
//    public void addUser() {
//        UserService us = new UserService(ds);
//
//        User userAdded = us.addUser("un", "t@m.com", "pwd");
//
//        assertEquals("t@m.com", userAdded.getEmail());
//        assertEquals("un", userAdded.getUsername());
//        assertNotNull(userAdded.getId());
//        assertNotNull(userAdded.getPassword());
//
//        assertEquals(userAdded, us.getUserByUsername("un"));
//    }
//
//    @Test
//    public void listUsers() {
//        UserService us = new UserService(ds);
//
//        User user1 = us.addUser("un", "t@m.com", "pwd");
//        User user2 = us.addUser("ou", "ou@m.com", "pwd");
//
//        List<User> users = us.getUsers();
//
//        assertEquals(2, users.size());
//
//        assertTrue(users.contains(user1));
//        assertTrue(users.contains(user2));
//    }
//
//    @Test
//    public void getUserByUserName() {
//        UserService us = new UserService(ds);
//
//        User addedUser = us.addUser("un", "t@m.com", "pwd");
//        User findedUser = us.getUserByUsername("un");
//
//        assertEquals(addedUser, findedUser);
//    }
//
//    @Test
//    public void removeUser() {
//        UserService us = new UserService(ds);
//
//        us.addUser("un", "t@m.com", "pwd");
//        User removedUser = us.removeUser("un");
//
//        assertEquals("un", removedUser.getUsername());
//        assertNotNull(removedUser.getId());
//
//        assertNull(us.getUserByUsername("un"));
//    }
}
