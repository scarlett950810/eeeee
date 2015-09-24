/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.AircraftGroupSessionBeanLocal;
import imas.planning.sessionbean.AircraftSessionBeanLocal;
import imas.planning.sessionbean.AirportSessionBeanLocal;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Lei
 */
@Stateless
public class PostFlightReportSessionBean implements PostFlightReportSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    @EJB
    private AircraftGroupSessionBeanLocal aircraftGroupSessionBean;
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;
    @EJB
    private RouteSessionBeanLocal routeSessionBean;

    @Override
    public void init() {

        AircraftTypeEntity aircraftType1 = new AircraftTypeEntity("A380", (double) 10, 50, (double) 100000, (double) 200, (double) 3000, (double) 4400, (double) 20, "Gas", (double) 20);
        AircraftTypeEntity aircraftType2 = new AircraftTypeEntity("A880", (double) 20, 80, (double) 180000, (double) 200, (double) 3800, (double) 6400, (double) 28, "Gas", (double) 20);
        em.persist(aircraftType1);
        em.persist(aircraftType2);

        System.out.println("safe 1");

        AirportEntity a1 = new AirportEntity(false, "Shijiazhuang", "ZD Airport", "SJZ", "China");
        AirportEntity a2 = new AirportEntity(true, "Guangzhou", "Baiyun Airport", "CAN", "China");
        AirportEntity a3 = new AirportEntity(true, "Beijing", "BJ International Airport", "BJIA", "China");
        airportSessionBean.addAirport(a1);
        airportSessionBean.addAirport(a2);
        airportSessionBean.addAirport(a3);

        System.out.println("safe 2");

        aircraftGroupSessionBean.addAircraftGroup("A380");
        aircraftGroupSessionBean.addAircraftGroup("A880");

        System.out.println("safe 3");

        Query queryForAircraftType = em.createQuery("select at from AircraftTypeEntity at");
        AircraftTypeEntity at1 = (AircraftTypeEntity) queryForAircraftType.getResultList().get(0);
        Query queryForAircraftGroup = em.createQuery("select at from AircraftGroupEntity at");
        AircraftGroupEntity ag1 = (AircraftGroupEntity) queryForAircraftGroup.getResultList().get(0);

        System.out.println("safe 4");

        aircraftSessionBean.addAircraft("AAA", at1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 20, (double) 0, "All is well", a2, a1, ag1, 4, 5, 4, 6, 6, 10, 7, 50);
        aircraftSessionBean.addAircraft("AAB", at1, (double) 20000000, (double) 1000000, (double) 19000000, (double) 18, (double) 0, "All is well", a3, a2, ag1, 4, 5, 4, 6, 6, 30, 7, 30);
        aircraftSessionBean.addAircraft("AAC", at1, (double) 40000000, (double) 1000000, (double) 39000000, (double) 30, (double) 0, "All is well", a3, a2, ag1, 0, 0, 4, 6, 6, 30, 7, 50);

        System.out.println("safe 5");

        routeSessionBean.addRoute(a1, a3);
        routeSessionBean.addRoute(a2, a3);
        routeSessionBean.addRoute(a3, a2);

        System.out.println("safe 6");

        Query queryForRoute = em.createQuery("select at from RouteEntity at");
        RouteEntity r1 = (RouteEntity) queryForRoute.getResultList().get(0);
        RouteEntity r2 = (RouteEntity) queryForRoute.getResultList().get(1);
        Query queryForAircraft = em.createQuery("select at from AircraftEntity at");
        AircraftEntity aircraft1 = (AircraftEntity) queryForAircraft.getResultList().get(0);
        FlightEntity fe1 = new FlightEntity("FlightNo1", (double) 800000, 3.5, aircraft1, r1);
        FlightEntity fe2 = new FlightEntity("FlightNo2", (double) 800000, 3.5, aircraft1, r2);
        em.persist(fe1);
        em.persist(fe2);

        BookingClassEntity bc1 = new BookingClassEntity(fe1, "First Class", "First Class", 3000, 20);
        BookingClassEntity bc2 = new BookingClassEntity(fe1, "Business Class", "Business Class", 2000, 24);
        BookingClassEntity bc3 = new BookingClassEntity(fe1, "Premium Economy Class", "Premium Economy Class", 1000, 60);
        BookingClassEntity bc4 = new BookingClassEntity(fe1, "Economy Class", "Economy Class 1", 900, 200);
        BookingClassEntity bc5 = new BookingClassEntity(fe1, "Economy Class", "Economy Class 2", 600, 200);
        BookingClassEntity bc6 = new BookingClassEntity(fe1, "Economy Class", "Economy Class 3", 500, 200);
        BookingClassEntity bc7 = new BookingClassEntity().BusinessClassBookingClassEntity(fe2, 2200, 24);
        em.persist(bc1);
        em.persist(bc2);
        em.persist(bc3);
        em.persist(bc4);
        em.persist(bc5);
        em.persist(bc6);
        em.persist(bc7);
    }

    @Override
    public void updateReport(FlightEntity selectedFlight, Integer num) {
        if (num != 0) {
            em.merge(selectedFlight);
        }

    }

    @Override
    public List<FlightEntity> getList() {
        Query query;
        query = em.createQuery("SELECT f FROM FlightEntity f ");
        List<FlightEntity> list = query.getResultList();
        return list;
    }
}
