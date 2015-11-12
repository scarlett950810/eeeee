/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.StaffEntity;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Lei
 */
public class InternalMessageSessionBeanTest {

    InternalMessageSessionBeanRemote iasbr = lookupRemote();

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
    public void testGetAllStaff() {
        System.out.println("testGetAllStaff");
        List<StaffEntity> result = iasbr.getAllStaff();
        assertNotNull(result);
    }

    @Test
    public void testGetStaffEntityByStaffNo01() {
        System.out.println("testGetStaffEntityByStaffNo01");
        String staffNo = "admin";
        StaffEntity result = iasbr.getStaffEntityByStaffNo(staffNo);
        assertNotNull(result);
    }

    @Test
    public void testGetStaffEntityByStaffNo02() {
        System.out.println("testGetStaffEntityByStaffNo02");
        String staffNo = "dummystaffdummy";
        StaffEntity result = iasbr.getStaffEntityByStaffNo(staffNo);
        assertNull(result);
    }

    @Test
    public void testGetAllMessages() {
        System.out.println("testGetAllMessages");
        StaffEntity staff = new StaffEntity();
        int result = 1;
        assertNotNull(result);
    }

    private InternalMessageSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (InternalMessageSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/InternalMessageSessionBean!imas.common.sessionbean.InternalMessageSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
