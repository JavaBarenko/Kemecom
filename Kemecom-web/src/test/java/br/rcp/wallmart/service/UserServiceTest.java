/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.wallmart.service;

import static br.rcp.kemecom.helper.ConfigurationOptions.*;
import br.rcp.kemecom.service.UserServiceImpl;
import br.rcp.kemecom.helper.MongoDatastore;
import br.rcp.kemecom.helper.MongoDatastoreBuilder;
import br.rcp.kemecom.model.Email;
import br.rcp.kemecom.model.Password;
import br.rcp.kemecom.model.db.Address;
import br.rcp.kemecom.model.db.Message;
import br.rcp.kemecom.model.db.Token;
import br.rcp.kemecom.model.db.User;
import br.rcp.kemecom.service.UserService;
import br.rcp.wallmart.TestUtils;
import com.google.code.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 <p/>
 @author barenko
 */
public class UserServiceTest {

    private static Datastore ds;

    @Before
    public void setUp() {
        ds.getCollection(User.class).drop();
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        Configuration config = new PropertiesConfiguration("config.properties");

        MongoDatastore mongo = MongoDatastoreBuilder.mongo()
                .atHost(config.getString(TEST_DB_MONGO_HOST))
                .inPort(config.getInt(TEST_DB_MONGO_PORT))
                .forDbName(config.getString(TEST_DB_MONGO_DBNAME))
                .withUsername(config.getString(TEST_DB_MONGO_USERNAME))
                .withPassword(config.getString(TEST_DB_MONGO_PASSWORD))
                .build();

        ds = mongo.registryEntity(User.class).registryEntity(Token.class)
                //                .enableValidation()
                .getDataStore();
    }

    @AfterClass
    public static void afterClass() {
        ds.getMongo().close();
    }

//    @Test
//    public void listUsersMustBeEmpty() {
//        UserService us = new UserServiceImpl(ds, null, null);
//        Message m = us.getUsers();
//        assertTrue(m.isSuccessful());
//        assert (((List) m.getObject()).isEmpty());
//    }
    @Test
    public void addUserMustBeReturnTheUserWithoutPassword() {
        UserService us = new UserServiceImpl(ds, null, null);

        Message m = us.addUser(new Email("t@m.com"), new Password("pwd11"));

        assertTrue(m.isSuccessful());

        User u = m.getObject();

        assertNotNull(u.getEmail());
        assertEquals("t@m.com", u.getEmail().toString());
        assertNull(u.getAddress());
        assertNotNull(u.getId());
        assertNull(u.getPassword());
    }

//    @Test
//    public void listUsers() {
//        UserService us = new UserServiceImpl(ds, null, null);
//
//        Message m1 = us.addUser(new Email("t@m.com"), new Password("pwd11"));
//        User u1 = m1.getObject();
//        Message m2 = us.addUser(new Email("ou@m.com"), new Password("pwd22"));
//        User u2 = m2.getObject();
//
//        Message ml = us.getUsers();
//        List<User> list = ml.getObject();
//
//        assertNotNull(list);
//        assertEquals(2, list.size());
//
//        List<String> emails = Arrays.asList(u1.getEmail().toString(), u2.getEmail().toString());
//        assertTrue(emails.contains(list.get(0).getEmail().toString()));
//        assertTrue(emails.contains(list.get(1).getEmail().toString()));
//    }
    @Test
    public void updateUser() {
        User me = new User(new Email("t@m.com"));
        me.setPassword(new Password("12345"));
        ds.save(me);

        UserService us = new UserServiceImpl(ds, null, TestUtils.fakeRequest(me));

        MultivaluedMap<String, String> formParams = new MultivaluedMapImpl<String, String>();
        formParams.add("name", "Test");
        formParams.add("zipCode", "12345678");
        formParams.add("number", "1");
        formParams.add("street", "rua a");
        formParams.add("city", "osasco");
        formParams.add("neighborhood", "quitauna");
        formParams.add("state", "SP");
        Message m = us.updateUser(formParams);

        assertTrue(m.isSuccessful());
        User u = m.getObject();
        assertNotNull(u);
        assertEquals("Test", u.getName());
        Address addr = u.getAddress();
        assertNotNull(addr);
        assertEquals("12345678", addr.getZipCode());
        assertEquals("1", addr.getNumber());
        assertEquals("rua a", addr.getStreet());
        assertEquals("osasco", addr.getCity());
        assertEquals("quitauna", addr.getNeighborhood());
        assertEquals("SP", addr.getState());

        User meUpdated = ds.get(User.class, me.getId());
        assertNotNull(meUpdated);
        assertEquals(me.getEmail(), meUpdated.getEmail());
        assertEquals(me.getPassword().toString(), meUpdated.getPassword().toString());
        assertEquals(me.getName(), meUpdated.getName());

        Address addrUpdated = meUpdated.getAddress();
        assertNotNull(addrUpdated);
        assertEquals(addr.getCity(), addrUpdated.getCity());
        assertEquals(addr.getNeighborhood(), addrUpdated.getNeighborhood());
        assertEquals(addr.getNumber(), addrUpdated.getNumber());
        assertEquals(addr.getState(), addrUpdated.getState());
        assertEquals(addr.getStreet(), addrUpdated.getStreet());
        assertEquals(addr.getZipCode(), addrUpdated.getZipCode());
    }

    @Test
    public void removeUser() {
        User me = new User(new Email("t@m.com"));
        me.setPassword(new Password("12345"));
        ds.save(me);

        UserService us = new UserServiceImpl(ds, null, TestUtils.fakeRequest(me));

        Message m = us.removeUser();

        assertTrue(m.isSuccessful());
        assertNull(m.getObject());
    }
}
