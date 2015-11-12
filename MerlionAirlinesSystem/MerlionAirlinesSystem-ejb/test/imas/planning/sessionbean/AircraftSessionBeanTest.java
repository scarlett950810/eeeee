/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import java.util.ArrayList;
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
public class AircraftSessionBeanTest {

    AircraftSessionBeanRemote a = lookupRemote();

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
    public void testGetAircraftGroups() {
        System.out.println("testGetAircraftGroups");
        List<AircraftGroupEntity> result = a.getAircraftGroups();
        assertNotNull(result);
    }

    @Test
    public void testGetAirports() {
        System.out.println("testGetAirports");
        List<AirportEntity> result = a.getAirports();
        assertNotNull(result);
    }

    @Test
    public void testGetSeatClasses() {
        System.out.println("testGetSeatClasses");
        List<String> al = a.getSeatClasses();
        assertNotNull(al);
    }

    @Test
    public void testGetAircraftTypes() {
        System.out.println("testGetAircraftTypes");
        List<AircraftTypeEntity> result = a.getAircraftTypes();
        assertNotNull(result);
    }

    @Test
    public void testCheckAircraftExistense01() {
        System.out.println("testCheckAircraftExistense01");
        String tailId = "0010";
        boolean result = a.checkAircraftExistense(tailId);
        assertTrue(result);
    }

    @Test
    public void testCheckAircraftExistense02() {
        System.out.println("testCheckAircraftExistense02");
        String tailId = "dummy fake";
        boolean result = a.checkAircraftExistense(tailId);
        assertFalse(result);
    }

    @Test
    public void testGetAircrafts() {
        System.out.println("testGetAircrafts");
        List<AircraftEntity> result = a.getAircrafts();
        assertNotNull(result);
    }

    @Test
    public void testDeleteAircraft01() {
        AircraftEntity aircraft = new AircraftEntity();
        FlightEntity flight = new FlightEntity();
        flight.setFlightNo("AAAA");
        List<FlightEntity> list = new ArrayList<FlightEntity>();
        list.add(flight);
        aircraft.setFlights(list);
        boolean result = a.deleteAircraft(aircraft);
        assertFalse(result);

    }

    private AircraftSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (AircraftSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/AircraftSessionBean!imas.planning.sessionbean.AircraftSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
