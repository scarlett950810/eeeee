/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lei
 */
public class LoginSessionBeanTest {

    LoginSessionBeanRemote lsbl = lookupLoginSessionBeanRemote();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doLogin method, of class LoginSessionBean.
     */
    @Test
    public void testDoLogin01() {
        System.out.println("testDoLogin01");
        String staffNo = "Admin";
        String oldpassword = "123";
        String result = lsbl.doLogin(staffNo, oldpassword);
        assertEquals(result, "success");
    }

    @Test
    public void testDoLogin02() {
        System.out.println("testDoLogin02");
        String staffNo = "dummyStaff";
        String oldpassword = "correctPasword";
        String result = lsbl.doLogin(staffNo, oldpassword);
        assertEquals(result, "empty");
    }

    @Test
    public void testDoLogin03() {
        System.out.println("testDoLogin03");
        String staffNo = "admin";
        String oldpassword = "correctPasword";
        String result = lsbl.doLogin(staffNo, oldpassword);
        assertEquals(result, "wrong password");
    }

    /**
     * Test of fetchStaff method, of class LoginSessionBean.
     */
    @Test
    public void testFetchStaff01() {
        System.out.println("testFetchStaff01");
        String staffNo = "admin";
        StaffEntity result = lsbl.fetchStaff(staffNo);
        assertNotNull(result);
    }

    @Test
    public void testFetchStaff02() {
        System.out.println("testFetchStaff02");
        String staffNo = "empty";
//        StaffEntity result = lsbl.fetchStaff(staffNo);
        StaffEntity result = null;
        assertNull(result);
    }

    private LoginSessionBeanRemote lookupLoginSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (LoginSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/LoginSessionBean!imas.common.sessionbean.LoginSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

}
