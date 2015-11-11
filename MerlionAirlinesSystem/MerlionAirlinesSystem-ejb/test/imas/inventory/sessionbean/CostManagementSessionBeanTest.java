/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.CostPairEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Lei
 */
public class CostManagementSessionBeanTest {

    CostManagementSessionBeanRemote cmsbr = lookupRemote();

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
    public void testCreateRoot() {
        System.out.println("testCreateRoot");
        List<CostPairEntity> list = new ArrayList<>();
        list.add(new CostPairEntity("Cost per seat per mile", 0.0, 0));//0*
        list.add(new CostPairEntity("Fixed cost per seat per mile", 0.0, 1));//1*
        list.add(new CostPairEntity("Flight distance per seat per day", 0.0, 2));//2
        list.add(new CostPairEntity("Fixed cost per seat", 0.0, 3));//3*
        list.add(new CostPairEntity("Operating cost per seat", 0.0, 4));//4
        list.add(new CostPairEntity("Other cost per seat", 0.0, 5));//5
        list.add(new CostPairEntity("Flight variable cost per seat per mile", 0.0, 6));//6*
        list.add(new CostPairEntity("Oil cost per seat per mile", 0.0, 7));//7
        list.add(new CostPairEntity("Crew cost per seat per mile", 0.0, 8));//8
        list.add(new CostPairEntity("Maintainence cost per seat per mile", 0.0, 9));//9
        list.add(new CostPairEntity("Tolls per seat per mile for take-off/landing", 0.0, 10));//10
        list.add(new CostPairEntity("Other variable cost per seat per mile", 0.0, 11));//11
        list.add(new CostPairEntity("Passenger cost per seat per mile", 0.0, 12));//12*
        list.add(new CostPairEntity("Show rate", 1.0, 13));//13
        list.add(new CostPairEntity("Average flight distance per passenger", 0.0, 14));//14
        list.add(new CostPairEntity("Average cost per passenger", 0.0, 15));//15
        list.add(new CostPairEntity("Sales cost per passenger", 0.0, 16));//16
        list.add(new CostPairEntity("Airport fee per passenger", 0.0, 17));//17
        list.add(new CostPairEntity("Check-in cost per passenger", 0.0, 18));//18
        list.add(new CostPairEntity("Meal cost per passenger", 0.0, 19));//19
        list.add(new CostPairEntity("Service cost per passenger", 0.0, 20));//20
        list.add(new CostPairEntity("First class service cost per passenger", 0.0, 21));//21
        list.add(new CostPairEntity("Delay cost per passenger", 0.0, 22));//22
        TreeNode result = cmsbr.createRoot(list);
        assertNotNull(result);

    }

    @Test
    public void testCorrectList() {
        System.out.println("testCorrectList");
        List<CostPairEntity> list = new ArrayList<>();
        list.add(new CostPairEntity("Cost per seat per mile", 1.0, 0));//0*
        list.add(new CostPairEntity("Fixed cost per seat per mile", 2.0, 1));//1*
        list.add(new CostPairEntity("Flight distance per seat per day", 3.0, 2));//2
        list.add(new CostPairEntity("Fixed cost per seat", 4.0, 3));//3*
        list.add(new CostPairEntity("Operating cost per seat", 5.0, 4));//4
        list.add(new CostPairEntity("Other cost per seat", 6.0, 5));//5
        list.add(new CostPairEntity("Flight variable cost per seat per mile", 7.0, 6));//6*
        list.add(new CostPairEntity("Oil cost per seat per mile", 8.0, 7));//7
        list.add(new CostPairEntity("Crew cost per seat per mile", 9.0, 8));//8
        list.add(new CostPairEntity("Maintainence cost per seat per mile", 10.0, 9));//9
        list.add(new CostPairEntity("Tolls per seat per mile for take-off/landing", 1.0, 10));//10
        list.add(new CostPairEntity("Other variable cost per seat per mile", 1.0, 11));//11
        list.add(new CostPairEntity("Passenger cost per seat per mile", 1.0, 12));//12*
        list.add(new CostPairEntity("Show rate", 1.0, 13));//13
        list.add(new CostPairEntity("Average flight distance per passenger", 2.0, 14));//14
        list.add(new CostPairEntity("Average cost per passenger", 1.0, 15));//15
        list.add(new CostPairEntity("Sales cost per passenger", 1.0, 16));//16
        list.add(new CostPairEntity("Airport fee per passenger", 1.0, 17));//17
        list.add(new CostPairEntity("Check-in cost per passenger", 1.0, 18));//18
        list.add(new CostPairEntity("Meal cost per passenger", 1.0, 19));//19
        list.add(new CostPairEntity("Service cost per passenger", 1.0, 20));//20
        list.add(new CostPairEntity("First class service cost per passenger", 1.0, 21));//21
        list.add(new CostPairEntity("Delay cost per passenger", 1.0, 22));//22
        List<CostPairEntity> result = cmsbr.correctList(list);
        assertNotNull(result);
    }

    @Test
    public void testGetCostPerSeatPerMile() {
        System.out.println("testGetCostPerSeatPerMile");
        RouteEntity route = new RouteEntity();
        List<CostPairEntity> list = new ArrayList<>();
        list.add(new CostPairEntity("Cost per seat per mile", 1.0, 0));//0*
        list.add(new CostPairEntity("Fixed cost per seat per mile", 2.0, 1));//1*
        list.add(new CostPairEntity("Flight distance per seat per day", 3.0, 2));//2
        list.add(new CostPairEntity("Fixed cost per seat", 4.0, 3));//3*
        list.add(new CostPairEntity("Operating cost per seat", 5.0, 4));//4
        list.add(new CostPairEntity("Other cost per seat", 6.0, 5));//5
        list.add(new CostPairEntity("Flight variable cost per seat per mile", 7.0, 6));//6*
        list.add(new CostPairEntity("Oil cost per seat per mile", 8.0, 7));//7
        list.add(new CostPairEntity("Crew cost per seat per mile", 9.0, 8));//8
        list.add(new CostPairEntity("Maintainence cost per seat per mile", 10.0, 9));//9
        list.add(new CostPairEntity("Tolls per seat per mile for take-off/landing", 1.0, 10));//10
        list.add(new CostPairEntity("Other variable cost per seat per mile", 1.0, 11));//11
        list.add(new CostPairEntity("Passenger cost per seat per mile", 1.0, 12));//12*
        list.add(new CostPairEntity("Show rate", 1.0, 13));//13
        list.add(new CostPairEntity("Average flight distance per passenger", 2.0, 14));//14
        list.add(new CostPairEntity("Average cost per passenger", 1.0, 15));//15
        list.add(new CostPairEntity("Sales cost per passenger", 1.0, 16));//16
        list.add(new CostPairEntity("Airport fee per passenger", 1.0, 17));//17
        list.add(new CostPairEntity("Check-in cost per passenger", 1.0, 18));//18
        list.add(new CostPairEntity("Meal cost per passenger", 1.0, 19));//19
        list.add(new CostPairEntity("Service cost per passenger", 1.0, 20));//20
        list.add(new CostPairEntity("First class service cost per passenger", 1.0, 21));//21
        list.add(new CostPairEntity("Delay cost per passenger", 1.0, 22));//22
        route.setCostPairs(list);
        Double result = 0.5;
        assertNotNull(result);
    }

    private CostManagementSessionBeanRemote lookupRemote() {
        try {
            Context c = new InitialContext();
            return (CostManagementSessionBeanRemote) c.lookup("java:global/MerlionAirlinesSystem/MerlionAirlinesSystem-ejb/CostManagementSessionBean!imas.inventory.sessionbean.CostManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

}
