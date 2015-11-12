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
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wutong
 */
public class AircraftTypeSessionBeanTest {

    AircraftTypeSessionBeanRemote a = lookupRemote();

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
    public void testCheckAircraftType01() {
        System.out.println("testCheckAircraftType01");
        String type = "A380";
        boolean result = a.checkAircraftType(type);
        assertFalse(result);
    }

    @Test
    public void testCheckAircraftType02() {
        System.out.println("testCheckAircraftType02");
        String type = "dummydummy";
        boolean result = a.checkAircraftType(type);
        assertTrue(result);
    }

    @Test
    public void testDeleteAircraftType01() {
        System.out.println("testDeleteAircraftType01");
        String type = "A380";
        boolean result = a.deleteAircraftType(type);
        assertFalse(result);
    }

    @Test
    public void testGetAllAircraftTypes() {
        System.out.println("testGetAllAircraftTypes");
        List<AircraftTypeEntity> result = a.getAllAircraftTypes();
        assertNotNull(result);
    }

    @Test
    public void testGetAircraftsFromAircraftType() {
        System.out.println("testGetAircraftsFromAircraftType");
        List<AircraftTypeEntity> result = a.getAllAircraftTypes();
        assertNotNull(result);
    }

    private AircraftTypeSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (AircraftTypeSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/AircraftTypeSessionBean!imas.planning.sessionbean.AircraftTypeSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
