/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.distribution.entity.TicketEntity;
import imas.distribution.sessionbean.DistributionSessionBeanLocal;
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
    
        @EJB
    private DistributionSessionBeanLocal distributionSessionBean;


    // get flights with aircraft but without booking classes.
    @Override
    public List<FlightEntity> getFlightsWithoutBookingClass() {
        List<FlightEntity> flightsWithoutBookingClass = new ArrayList();

        Query queryForAllFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> allFlights = (List<FlightEntity>) queryForAllFlights.getResultList();

        allFlights.stream().forEach((f) -> {
            if (f.getAircraft() != null) {

                Query queryForBookingClass = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
                queryForBookingClass.setParameter("flight", f);
                if (queryForBookingClass.getResultList().isEmpty()) {
                    flightsWithoutBookingClass.add(f);

                }

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
        System.out.println("Computing the latest historical show rate:");
        int totalEconomyClassTickets = 0;
        int issuedEconomyClassTickets = 0;

        // get all flights that has been departured.
        Query queryForAllDeparturedFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.departured = :departured");
        queryForAllDeparturedFlights.setParameter("departured", true);
        List<FlightEntity> allDeparturedFlights = (List<FlightEntity>) queryForAllDeparturedFlights.getResultList();

        // get all economy seats on each departured flights
        for (FlightEntity f : allDeparturedFlights) {
            Query queryForTickets = entityManager.createQuery("SELECT t FROM TicketEntity t WHERE t.bookingClass.flight = :flight "
                    + "AND t.seat.seatClass = :seatClass ");
            queryForTickets.setParameter("flight", f);
            queryForTickets.setParameter("seatClass", "Economy Class");
            List<TicketEntity> tickets = (List<TicketEntity>) queryForTickets.getResultList();
//            System.out.println("tickets size = " + tickets.size());
            for (TicketEntity t : tickets) {
                totalEconomyClassTickets = totalEconomyClassTickets + 1;
                if (t.isIssued()) {
                    issuedEconomyClassTickets = issuedEconomyClassTickets + 1;
                }
            }
        }

        if (totalEconomyClassTickets > 0) {
            System.out.println("Up to now,");
            System.out.println("in total " + totalEconomyClassTickets + " tickets are sold;");
            double showRate = 1.0 * issuedEconomyClassTickets / totalEconomyClassTickets;
            System.out.println("in total " + issuedEconomyClassTickets + " of them showed up.");
            System.out.println("Latest show rate = " + showRate);
            return showRate;
        } else {
            // no historical records available
            System.out.println("No historical records available.");
            System.out.println("Returns default showrate = 1.");
            return (double) 1;
        }

    }

    @Override
    public int getFirstClassCapacity(FlightEntity flight) {
        AircraftEntity a = flight.getAircraft();

        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "First Class");

        return q.getResultList().size();
    }

    @Override
    public int getBusinessClassCapacity(FlightEntity flight) {

        AircraftEntity a = flight.getAircraft();
        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "Business Class");
        return q.getResultList().size();
    }

    @Override
    public int getPremiumEconomyClassCapacity(FlightEntity flight) {
        AircraftEntity a = flight.getAircraft();
        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "Premium Economy Class");
        return q.getResultList().size();
    }

    @Override
    public int getEconomyClassCapacity(FlightEntity flight) {
        AircraftEntity a = flight.getAircraft();
        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
        q.setParameter("aircraft", a);
        q.setParameter("seatClass", "Economy Class");
        return q.getResultList().size();
    }

    @Override
    public void insertData() {
        System.out.println("insert testing data");
        Query queryForFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.departured = :departured");
        queryForFlights.setParameter("departured", false);
        List<FlightEntity> undeparturedFlights = queryForFlights.getResultList();
        for (FlightEntity flight : undeparturedFlights) {
            flight.setDepartured(true);
            Query queryForBCs = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
            queryForBCs.setParameter("flight", flight);
            BookingClassEntity bc1 = (BookingClassEntity) queryForBCs.getResultList().get(0);
            BookingClassEntity bc2 = (BookingClassEntity) queryForBCs.getResultList().get(1);
            BookingClassEntity bc3 = (BookingClassEntity) queryForBCs.getResultList().get(2);
            BookingClassEntity bc4 = (BookingClassEntity) queryForBCs.getResultList().get(3);
            BookingClassEntity bc5 = (BookingClassEntity) queryForBCs.getResultList().get(4);
            BookingClassEntity bc6 = (BookingClassEntity) queryForBCs.getResultList().get(5);
            BookingClassEntity bc7 = (BookingClassEntity) queryForBCs.getResultList().get(6);
            BookingClassEntity bc8 = (BookingClassEntity) queryForBCs.getResultList().get(7);
            BookingClassEntity bc9 = (BookingClassEntity) queryForBCs.getResultList().get(8);
            distributionSessionBean.makeBooking(bc1, (int) 0.9 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc2, (int) 0.95 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc3, (int) 0.8 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc4, (int) 0.8 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc5, (int) 0.9 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc6, (int) 0.95 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc7, (int) 0.95 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc8, (int) 0.9 * distributionSessionBean.getQuotaLeft(bc1));
            distributionSessionBean.makeBooking(bc9, (int) 0.7 * distributionSessionBean.getQuotaLeft(bc1));
            
        }

        Query queryForTickets = entityManager.createQuery("SELECT t FROM TicketEntity t");
        List<TicketEntity> alltickets = queryForTickets.getResultList();
        for (int i = 0; i < alltickets.size(); i++) {
            if (i % 19 == 0) {
                TicketEntity t = alltickets.get(i);
                t.setIssued(true);
            }
        }

    }
    
}
