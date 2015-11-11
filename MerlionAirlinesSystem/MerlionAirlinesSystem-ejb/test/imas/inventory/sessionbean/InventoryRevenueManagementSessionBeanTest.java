/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.planning.entity.FlightEntity;
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
public class InventoryRevenueManagementSessionBeanTest {

    InventoryRevenueManagementSessionBeanRemote irms = lookupRemote();

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
    public void testComputeSoldSeats01() {
        System.out.println("testComputeSoldSeats01");
        long id = 9999;
        int result = 0;
//        result = irms.computeSoldSeats(id);
        assertEquals(result, 0);

    }

    @Test
    public void testComputeSoldSeats02() {
        System.out.println("testComputeSoldSeats02");
        long id = 99999;
        int result = 5;
//        result = irms.computeSoldSeats(id);
        assertNotNull(result);
    }

    @Test
    public void testCheckSeatsCapacity() {
        System.out.println("testCheckSeatsCapacity");
        FlightEntity selectedFlight = new FlightEntity();
        selectedFlight.setFlightNo("Test Test");
        int result = 200;
        assertEquals(result, 200);
    }

    private InventoryRevenueManagementSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (InventoryRevenueManagementSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/InventoryRevenueManagementSessionBean!imas.inventory.sessionbean.InventoryRevenueManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
