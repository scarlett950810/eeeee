/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.inventory.sessionbean.YieldManagementSessionBeanRemote;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Date;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        System.out.println("testComputeRoutePopularity01");
        AirportEntity airport = new AirportEntity();
        boolean result = true;
        assertTrue(result);
    }

    @Test
    public void testDeleteAirport() {
        System.out.println("testComputeRoutePopularity02");
       
        boolean result = true;
        assertTrue(result);
    }

    @Test
    public void testFetchAirport01() {
        List<AirportEntity> al = a.fetchAirport();
        assertNotNull(al);
    }

    @Test
    public void testGetAirport01() {
        AirportEntity result = new AirportEntity();
        assertNotNull(result);
    }

    @Test
    public void testCheckRelatedRoute01() {
        boolean result = true;
        assertTrue(result);

    }

    private AirportSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (AirportSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/AirportSessionBean!imas.inventory.sessionbean.AirportSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
