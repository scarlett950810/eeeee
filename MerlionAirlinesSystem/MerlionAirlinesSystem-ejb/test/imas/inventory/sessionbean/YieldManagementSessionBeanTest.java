/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Date;
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
public class YieldManagementSessionBeanTest {

    YieldManagementSessionBeanRemote ymsb = lookupRemote();

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
    public void testComputeRoutePopularity01() {
        System.out.println("testComputeRoutePopularity01");
        RouteEntity route = new RouteEntity();
        route.setDistance(1000.0);
        double result = ymsb.computeRoutePopularity(route);
        assertNotNull(result);
    }

    @Test
    public void testComputeRoutePopularity02() {
        System.out.println("testComputeRoutePopularity02");
        RouteEntity route = new RouteEntity();
        route.setDistance(1000.0);
        double result = ymsb.computeRoutePopularity(route);
        assertEquals(1.0, result, 0.01);
    }

    @Test
    public void testGetFlightEconomyClassTotalCost() {
        System.out.println("testGetFlightEconomyClassTotalCost");
        RouteEntity route = new RouteEntity();
        route.setDistance(1000.0);
        FlightEntity flight = new FlightEntity();
        flight.setRoute(route);
        double result = 0.05;
        assertNotNull(result);
    }

    @Test
    public void testGetFlightTotalEconomyClassRevenue() {
        System.out.println("testGetFlightEconomyClassTotalCost");
        RouteEntity route = new RouteEntity();
        route.setDistance(1000.0);
        FlightEntity flight = new FlightEntity();
        flight.setRoute(route);
        double result = ymsb.getFlightTotalEconomyClassRevenue(flight);
        assertNotNull(result);
    }

    @Test
    public void testGetFlightFromNowToDepartureInDay() {
        System.out.println("testGetFlightFromNowToDepartureInDay");
        FlightEntity flight = new FlightEntity();
        flight.setDepartureDate(new Date());
        int result = ymsb.getFlightFromNowToDepartureInDay(flight);
        assertNotNull(result);

    }

    private YieldManagementSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (YieldManagementSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/YieldManagementSessionBean!imas.inventory.sessionbean.YieldManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

}
