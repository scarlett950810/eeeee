/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

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
public class UserProfileManagementSessionBeanTest {

    UserProfileManagementSessionBeanRemote usmsbr = lookupRemote();

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

    @Test
    public void testGetOldPassword01() {
        System.out.println("testGetOldPassword01");
        String staffNo = "admin";
        String oldPassword = "123";
        boolean result = usmsbr.getOldPassword(staffNo, oldPassword);
        assertTrue(result);
    }

//    @Test
//    public void testGetOldPassword02() {
//        System.out.println("testGetOldPassword02");
//        String staffNo = "dummystaff000";
//        String oldPassword = "correctPassword";
//        boolean result = usmsbr.getOldPassword(staffNo, oldPassword);
//        System.out.println(result);
//        assertNull(result);
//    }

    @Test
    public void testGetOldPassword02() {
        System.out.println("testGetOldPassword02");
        String staffNo = "admin";
        String oldPassword = "wrongPassword";
        boolean result = usmsbr.getOldPassword(staffNo, oldPassword);
        assertFalse(result);
    }

//    @Test
//    public void testGetWorkingMessage01() {
//        System.out.println("testGetWorkingMessage01");
//        String staffNo = "admin";
//        String result = usmsbr.getWorkingMessage(staffNo);
//        assertEquals(result, "You are going to start working here. Please make confirmation");
//    }
    @Test
    public void testGetWorkingMessage01() {
        System.out.println("testGetWorkingMessage01");
        String staffNo = "dummyAdmin";
        String result = usmsbr.getWorkingMessage(staffNo);
        assertNull(result);
    }

    private UserProfileManagementSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (UserProfileManagementSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/UserProfileManagementSessionBean!imas.common.sessionbean.UserProfileManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
