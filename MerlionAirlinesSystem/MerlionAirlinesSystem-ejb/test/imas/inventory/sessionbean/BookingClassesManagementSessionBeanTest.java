/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
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
public class BookingClassesManagementSessionBeanTest {

    BookingClassesManagementSessionBeanRemote bcmsbr = lookupRemote();
   

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
    public void testGetSeatClassCapacity01() {
        System.out.println("testGetSeatClassCapacity01");
        FlightEntity flight = new FlightEntity();
        String seatClass = "First Class";
        int result = 0;
        assertNotNull(result);
    }

    @Test
    public void testGetFlightCandidateToOpenForBooking01() {
        System.out.println("testGetFlightCandidateToOpenForBooking01");
        List<FlightEntity> result = bcmsbr.getFlightCandidateToOpenForBooking();
        assertNotNull(result);
    }

    private BookingClassesManagementSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (BookingClassesManagementSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/BookingClassesManagementSessionBean!imas.inventory.sessionbean.BookingClassesManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
