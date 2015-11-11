/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import imas.planning.entity.AirportEntity;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Lei
 */
public class AccountManagementSessionBeanTest {

    AccountManagementSessionBeanRemote amsbl = lookupAccountManagementSessionBeanRemote();

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
    public void testAddStaff01() {
        System.out.println("testAddStaff01");
        String staffNo = "admin";
        String name = "administrator";
        String email = "newEmail@MerlionAirlines.com.sg";
        String contactNumber = "86809344";
        String address = "#27 Changi Road, Merlion Complex";
        String gender = "male";
        String businessUnit = "Administration";
        String division = "Human Resources";
        String position = "manager";
        String location = "Singapore";
        String base = null;
        String workingStatus = "available";
        List<String> aircraftTypeCapabilities = null;
        Boolean mileageLimit = null;
        Boolean isPilot = false;
        Boolean isCabinCrew = false;
        boolean result = amsbl.addStaff(staffNo, name, email, contactNumber, address, gender, businessUnit, division, position, location, base, workingStatus, aircraftTypeCapabilities, mileageLimit, isPilot, isCabinCrew);
        assertFalse(result);
    }

    @Test
    public void testAddStaff02() {
        System.out.println("testAddStaff02");
        String staffNo = "true06";
        String name = "Vincent Sun";
        String email = "vincent.Sun061@MerlionAirlines.com.sg";
        String contactNumber = "86843344";
        String address = "#27 Changi Road, Merlion Complex";
        String gender = "male";
        String businessUnit = "Sales and Marketing";
        String division = "Sales";
        String position = "staff";
        String location = "Singapore";
        String base = null;
        String workingStatus = "available";
        List<String> aircraftTypeCapabilities = null;
        Boolean mileageLimit = null;
        Boolean isPilot = false;
        Boolean isCabinCrew = false;
        boolean result = amsbl.addStaff(staffNo, name, email, contactNumber, address, gender, businessUnit, division, position, location, base, workingStatus, aircraftTypeCapabilities, mileageLimit, isPilot, isCabinCrew);
//        System.out.print(result);
        assertTrue(result);
    }

    /**
     * Test of checkEmailExistence method, of class
     * AccountManagementSessionBean.
     */
    @Test
    public void testCheckEmailExistence01() throws Exception {
        System.out.println("checkEmailExistence01");
        String email = "hahaha@merlion.com";
        boolean result = amsbl.checkEmailExistence(email);
        assertTrue(result);
    }

    @Test
    public void testCheckEmailExistence02() throws Exception {
        System.out.println("checkEmailExistence02");
        String email = "wrongemail";
        boolean result = amsbl.checkEmailExistence(email);
        assertFalse(result);
    }

    /**
     * // * Test of getStaff method, of class AccountManagementSessionBean. //
     */
    @Test
    public void testGetStaff01() throws Exception {
        System.out.println("testGetStaff01");
        String staffNo = "admin";
        StaffEntity result = amsbl.getStaff(staffNo);
        assertNotNull(result);
    }

    @Test
    public void testGetStaff02() throws Exception {
        System.out.println("testGetStaff02");
        String staffNo = "noStaff";
        StaffEntity result = amsbl.getStaff(staffNo);
        assertNull(result);
    }

    @Test
    public void testFetchBase01() throws Exception {
        System.out.println("testFetchBase01");
        String base = "SIN";
        AirportEntity result = amsbl.fetchBase(base);
        assertNotNull(result);
    }

    @Test
    public void testFetchBase02() throws Exception {
        System.out.println("testFetchBase02");
        String base = "ABC";
        AirportEntity result = amsbl.fetchBase(base);
        assertNull(result);
    }

    private AccountManagementSessionBeanRemote lookupAccountManagementSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (AccountManagementSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/AccountManagementSessionBean!imas.common.sessionbean.AccountManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}

//    /**
//     * Test of checkEmailExistence method, of class AccountManagementSessionBean.
//     */
//    @Test
//    public void testCheckEmailExistence() throws Exception {
//        System.out.println("checkEmailExistence");
//        String email = "";
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        AccountManagementSessionBeanLocal instance = (AccountManagementSessionBeanLocal)container.getContext().lookup("java:global/classes/AccountManagementSessionBean");
//        Boolean expResult = null;
//        Boolean result = instance.checkEmailExistence(email);
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
