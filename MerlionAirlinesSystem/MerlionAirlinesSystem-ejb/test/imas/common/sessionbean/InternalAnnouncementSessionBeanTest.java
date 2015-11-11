/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.common.sessionbean;

import imas.common.entity.InternalAnnouncementEntity;
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
public class InternalAnnouncementSessionBeanTest {

    InternalAnnouncementSessionBeanRemote iasbr = lookupRemote();

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

//    @Test
//    public void testSendInternalAnnouncements01() {
//        List<String> departments = null;
//        departments.add("Administration");
//        List<AirportEntity> bases = null;
//        String title = "Test";
//        String content = "This is test";
//        String result = iasbr.sendInternalAnnouncements(departments, bases, title, content);
//        assertEquals(result, "Message has been sent to all selected staff");
//    }

    @Test
    public void testGetAllAnnouncements01() {
        System.out.println("testGetAllAnnouncements01");
        String staffNo = "admin";
        List<InternalAnnouncementEntity> result = iasbr.getAllAnnouncements(staffNo);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllAnnouncements02() {
        System.out.println("testGetAllAnnouncements02");
        String staffNo = "noVaildStaff";
        List<InternalAnnouncementEntity> result = iasbr.getAllAnnouncements(staffNo);
        assertNull(result);
    }

    private InternalAnnouncementSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (InternalAnnouncementSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/InternalAnnouncementSessionBean!imas.common.sessionbean.InternalAnnouncementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
