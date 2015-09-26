/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.SeatEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class SeatsManagementSessionBean implements SeatsManagementSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<FlightEntity> getFlightsWithoutBookingClass() {
        List<FlightEntity> flightsWithoutBookingClass = new ArrayList();
        
        Query queryForAllFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> allFlights = (List<FlightEntity>) queryForAllFlights.getResultList();
//        System.out.println(allFlights);
        allFlights.stream().forEach((f) -> {
            Query queryForBookingClass = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
            queryForBookingClass.setParameter("flight", f);
            if (queryForBookingClass.getResultList().isEmpty()) {
                flightsWithoutBookingClass.add(f);
            }
        });
        return flightsWithoutBookingClass;
    }
    
    @Override
    public void generateFirstClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().FirstClassBookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generateBusinessClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().BusinessClassBookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generatePremiumEconomyClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().PremiumEconomyClassBookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generateEconomyClass1BookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().EconomyClass1BookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generateEconomyClass2BookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().EconomyClass2BookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generateEconomyClass3BookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().EconomyClass3BookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generateEconomyClass4BookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().EconomyClass4BookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generateEconomyClass5BookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().EconomyClass5BookingClassEntity(flight, price, quota));
    }
    
    @Override
    public void generateEconomyClassAgencyBookingClassEntity(FlightEntity flight, double price, int quota) {
        entityManager.persist(new BookingClassEntity().EconomyClassAgencyBookingClassEntity(flight, price, quota));
    }
    
    // to be optimized
    // to add smoothing constant, date, etc. in to input
    // to change output into normal distribution model (mean and variance)
    // current approach: for all departured flights, total departured tickets / total tickets
    @Override
    public double computeHistoricalShowRate() {
//        System.out.println("in session bean.compute historical show rate");
        int totalEconomyClassTickets = 0;
        int issuedEconomyClassTickets = 0;
        
        // get all flights that has been departured.
        Query queryForAllDeparturedFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.departured = :departured");
        queryForAllDeparturedFlights.setParameter("departured", true);
        List<FlightEntity> allDeparturedFlights = (List<FlightEntity>) queryForAllDeparturedFlights.getResultList();

        // get all economy seats on each departured flights
        for (FlightEntity f:allDeparturedFlights) {
            Query queryForTickets = entityManager.createQuery("SELECT t FROM TicketEntity t WHERE t.bookingClass.flight = :flight "
                    + "AND t.seat.seatClass = :seatClass ");
            queryForTickets.setParameter("flight", f);
            queryForTickets.setParameter("seatClass", "Economy Class");
            List <TicketEntity> tickets = (List <TicketEntity>) queryForTickets.getResultList();
//            System.out.println("tickets size = " + tickets.size());
            for (TicketEntity t:tickets) {
                totalEconomyClassTickets = totalEconomyClassTickets + 1;
                if (t.isIssued()) {
                    issuedEconomyClassTickets = issuedEconomyClassTickets + 1;
                }
            }
        }
               
        if (totalEconomyClassTickets > 0) {
            double showRate = 1.0*issuedEconomyClassTickets/totalEconomyClassTickets;
            return showRate;
        } else {
            // no historical records available
            return (double) 1;
        }
        
    }
    
    
    @Override
    public int getFirstClassCapacity(FlightEntity flight) {
//        System.out.println("getFirstClassCapacity");
//        System.out.println("flight = " + flight);
        
        AircraftEntity a  = flight.getAircraft();
        
//        System.out.println("a = " + a);
        
        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "First Class");
//        System.out.println(q.getResultList());
        return q.getResultList().size();
    }
    
    @Override
    public int getBusinessClassCapacity(FlightEntity flight) {
        AircraftEntity a  = flight.getAircraft();
        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "Business Class");
        return q.getResultList().size();
    }
    
    @Override
    public int getPremiumEconomyClassCapacity(FlightEntity flight) {
        AircraftEntity a  = flight.getAircraft();
        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "Premium Economy Class");
        return q.getResultList().size();
    }

    @Override
    public int getEconomyClassCapacity(FlightEntity flight) {
        AircraftEntity a  = flight.getAircraft();
        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "Economy Class");
        return q.getResultList().size();
    }

    @Override
    public void insertData() {
        System.out.println("insert data");
        Query q = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc");
        BookingClassEntity bc1 = (BookingClassEntity) q.getResultList().get(0);
        BookingClassEntity bc2 = (BookingClassEntity) q.getResultList().get(1);
        BookingClassEntity bc3 = (BookingClassEntity) q.getResultList().get(0);
        
        for (int i = 0; i < 10; i++) {
            TicketEntity t = new TicketEntity(bc1);
            t.setIssued(true);
            entityManager.persist(t);
        }
        for (int i = 0; i < 4; i++) {
            TicketEntity t = new TicketEntity(bc1);
            t.setIssued(false);
            entityManager.persist(t);
        }
        
        for (int i = 0; i < 10; i++) {
            TicketEntity t = new TicketEntity(bc2);
            t.setIssued(true);
            entityManager.persist(t);
        }
        for (int i = 0; i < 6; i++) {
            TicketEntity t = new TicketEntity(bc2);
            t.setIssued(true);
            entityManager.persist(t);
        }
        
        for (int i = 0; i < 40; i++) {
            TicketEntity t = new TicketEntity(bc2);
            t.setIssued(true);
            entityManager.persist(t);
        }
        for (int i = 0; i < 15; i++) {
            TicketEntity t = new TicketEntity(bc2);
            t.setIssued(false);
            entityManager.persist(t);
        }

        Query q2 = entityManager.createQuery("SELECT q FROM FlightEntity q");
        FlightEntity f = (FlightEntity) q2.getResultList().get(0);
        f.setDepartured(true);
        Query q3 = entityManager.createQuery("SELECT t FROM TicketEntity t");
        List<TicketEntity> tickets = q3.getResultList();
        int i = 0;
        for (TicketEntity t:tickets) {
            AircraftEntity aircraft = t.getBookingClass().getFlight().getAircraft();
            Query q1 = entityManager.createQuery("SELECT s FROM SeatEntity s where s.seatClass = :seatClass");
            q1.setParameter("seatClass" ,"Economy Class");
            SeatEntity seat = (SeatEntity) q1.getResultList().get(i);
            t.setSeat(seat);
            i = i + 1;
        }
    }
    
}

