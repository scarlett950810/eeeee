/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AirportEntity;
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
 * @author wutong
 */
public class AirportSessionBeanTest {

    AirportSessionBeanRemote a = lookupRemote();

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
    public void testCheckAirport01() {
        System.out.println("testCheckAirport01");
        String code = "SIN";
        boolean result = a.checkAirport(code);
        assertFalse(result);
    }

    @Test
    public void testCheckAirport02() {
        System.out.println("testCheckAirport02");
        String code = "FAKE";
        boolean result = a.checkAirport(code);
        assertTrue(result);
    }

    @Test
    public void testDeleteAirport() {
        System.out.println("testDeleteAirport");
        String code = "SIN";
        boolean result = a.deleteAirport(code);
        assertFalse(result);
    }

    @Test
    public void testFetchAirport01() {
        System.out.println("testFetchAirport01");
        List<AirportEntity> al = a.fetchAirport();
        assertNotNull(al);
    }

    @Test
    public void testGetAirport01() {
        System.out.println("testGetAirport01");
        String code = "Dummy";
        AirportEntity result = a.getAirport(code);
        assertNull(result);
    }

    @Test
    public void testGetAirport02() {
        System.out.println("testGetAirport02");
        String code = "SIN";
        AirportEntity result = a.getAirport(code);
        assertNotNull(result);
    }

    @Test
    public void testCheckRelatedRoute01() {
        System.out.println("testCheckRelatedRoute01");
        AirportEntity airport1 = new AirportEntity();
        boolean result = a.checkRelatedRoute(airport1);
        assertTrue(result);

    }

    @Test
    public void testCheckRelatedAircraft() {
        System.out.println("testCheckRelatedAircraft");
        AirportEntity airport = new AirportEntity();
        boolean result = a.checkRelatedAircraft(airport);
        assertTrue(result);
    }

    private AirportSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (AirportSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/AirportSessionBean!imas.planning.sessionbean.AirportSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
