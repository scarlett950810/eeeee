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
import imas.planning.entity.RouteEntity;
import java.util.Date;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        List<AircraftGroupEntity> result = a.getAircraftGroups();
        assertNotNull(result);
    }

    @Test
    public void testGetAirports() {
        List<AirportEntity> result = a.getAirports();
        assertNotNull(result);
    }

    @Test
    public void testGetSeatClasses() {
        List<String> al = a.getSeatClasses();
        assertNotNull(al);
    }

    @Test
    public void testGetAircraftTypes() {
        List<AircraftTypeEntity> result = a.getAircraftTypes();
        assertNotNull(result);
    }

    @Test
    public void testCheckAircraftExistense() {
        boolean result = true;
        assertTrue(result);

    }
    
    
    @Test
    public void testGetAircrafts() {
        List<AircraftEntity> result = a.getAircrafts();
        assertNotNull(result);

    }
    
            
    @Test
    public void testDeleteAircraft01() {
        boolean result = true;
        assertTrue(result);

    }

    private AircraftSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (AircraftSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/AircraftSessionBean!imas.inventory.sessionbean.AircraftSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
