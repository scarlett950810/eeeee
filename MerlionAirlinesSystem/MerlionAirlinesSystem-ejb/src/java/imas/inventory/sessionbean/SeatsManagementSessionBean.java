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
import java.util.Calendar;
import java.util.Date;
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
    
    @EJB
    private YieldManagementSessionBeanLocal yieldManagementSessionBean;

    @EJB
    private CostManagementSessionBeanLocal costSessionBean;

    @PersistenceContext
    private EntityManager entityManager;

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
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bookingClassEntity = new BookingClassEntity().FirstClassBookingClassEntity(flightToManage, price, quota);
        bookingClassEntity.setFlight(flightToManage);
        entityManager.persist(bookingClassEntity);
        entityManager.flush();
        
    }

    @Override
    public void generateBusinessClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().BusinessClassBookingClassEntity(flightToManage, price, quota));
    }

    @Override
    public void generatePremiumEconomyClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().PremiumEconomyClassBookingClassEntity(flightToManage, price, quota));
    }

    @Override
    public void generateEconomyClass1BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().EconomyClass1BookingClassEntity(flightToManage, price, quota));
    }

    @Override
    public void generateEconomyClass2BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().EconomyClass2BookingClassEntity(flightToManage, price, quota));
    }

    @Override
    public void generateEconomyClass3BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().EconomyClass3BookingClassEntity(flightToManage, price, quota));
    }

    @Override
    public void generateEconomyClass4BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().EconomyClass4BookingClassEntity(flightToManage, price, quota));
    }

    @Override
    public void generateEconomyClass5BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().EconomyClass5BookingClassEntity(flightToManage, price, quota));
    }

    @Override
    public void generateEconomyClassAgencyBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        entityManager.persist(new BookingClassEntity().EconomyClassAgencyBookingClassEntity(flightToManage, price, quota));
    }

    // to be optimized
    // to add smoothing constant, date, etc. in to input
    // to change output into normal distribution model (mean and variance)
    // current approach: for all departured flights, total departured tickets / total tickets
    @Override
    public double computeHistoricalShowRate() {
//        System.out.println("Computing the latest historical show rate:");
        int totalEconomyClassTickets = 0;
        int issuedEconomyClassTickets = 0;

        // get all flights that has been departured.
        Query queryForAllDeparturedFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.departured = :departured");
        queryForAllDeparturedFlights.setParameter("departured", true);
        List<FlightEntity> allDeparturedFlights = (List<FlightEntity>) queryForAllDeparturedFlights.getResultList();

        // get all economy seats on each departured flights
        for (FlightEntity f : allDeparturedFlights) {
            Query queryForTickets = entityManager.createQuery("SELECT t FROM TicketEntity t WHERE t.flight = :flight "
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
//            System.out.println("Up to now,");
//            System.out.println("in total " + totalEconomyClassTickets + " tickets are sold;");
            double showRate = 1.0 * issuedEconomyClassTickets / totalEconomyClassTickets;
//            System.out.println("in total " + issuedEconomyClassTickets + " of them showed up.");
//            System.out.println("Latest show rate = " + showRate);
            return showRate;
        } else {
            // no historical records available
//            System.out.println("No historical records available.");
//            System.out.println("Returns default showrate = 1.");
            return (double) 1;
        }

    }

    @Override
    public int getFirstClassCapacity(FlightEntity flight) {
        AircraftEntity a = flight.getAircraft();

//        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q.setParameter("aircraft", a);
//        q.setParameter("seatClass", "First Class");
//        return q.getResultList().size();
        int count = 0;
        for (SeatEntity seat : a.getSeats()) {
            if (seat.isFirstClass()) {
                count++;
            }
        }

        return count;
    }

    @Override
    public int getBusinessClassCapacity(FlightEntity flight) {

        AircraftEntity a = flight.getAircraft();

//        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q.setParameter("aircraft", a);
//        q.setParameter("seatClass", "Business Class");
//        return q.getResultList().size();
        int count = 0;
        for (SeatEntity seat : a.getSeats()) {
            if (seat.isBusinessClass()) {
                count++;
            }
        }

        return count;
    }

    @Override
    public int getPremiumEconomyClassCapacity(FlightEntity flight) {
        AircraftEntity a = flight.getAircraft();

//        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q.setParameter("aircraft", a);
//        q.setParameter("seatClass", "Premium Economy Class");
//        return q.getResultList().size();
        int count = 0;
        for (SeatEntity seat : a.getSeats()) {
            if (seat.isPremiumEconomyClass()) {
                count++;
            }
        }

        return count;
    }

    @Override
    public int getEconomyClassCapacity(FlightEntity flight) {
        AircraftEntity a = flight.getAircraft();

//        Query q = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.aircraft = :aircraft AND s.seatClass = :seatClass");
//        q.setParameter("aircraft", a);
//        q.setParameter("seatClass", "Economy Class");
//        return q.getResultList().size();
        int count = 0;
        for (SeatEntity seat : a.getSeats()) {
            if (seat.isEconomyClass()) {
                count++;
            }
        }

        return count;
    }

    @Override
    public void automaticallyCreateBookingClass(FlightEntity flight) {

        Integer firstClassCapacity = getFirstClassCapacity(flight);
        Integer businessClassCapacity = getBusinessClassCapacity(flight);
        Integer premiumEconomyClassCapacity = getPremiumEconomyClassCapacity(flight);
        Integer economyClassCapacity = getEconomyClassCapacity(flight);
        Double latestShowRate = computeHistoricalShowRate();
        Integer economyClassComputedOverbookingLevel = (int) (economyClassCapacity / latestShowRate);

        // to change to calling session bean.
        Double costPerSeatPerMile = costSessionBean.getCostPerSeatPerMile();
        Double distance = flight.getRoute().getDistance();
        Double baseFare = costPerSeatPerMile * distance;

        generateFirstClassBookingClassEntity(flight, 15 * baseFare, firstClassCapacity);
        generateBusinessClassBookingClassEntity(flight, 6 * baseFare, businessClassCapacity);
        generatePremiumEconomyClassBookingClassEntity(flight, 4 * baseFare, premiumEconomyClassCapacity);

        // TODO: optimization of yield management.
        generateEconomyClass1BookingClassEntity(flight, 3 * baseFare, 0);
        generateEconomyClass2BookingClassEntity(flight, 2.5 * baseFare, (int) (0.3 * economyClassComputedOverbookingLevel));
        generateEconomyClass3BookingClassEntity(flight, 2 * baseFare, (int) (0.4 * economyClassComputedOverbookingLevel));
        generateEconomyClassAgencyBookingClassEntity(flight, 1.5 * baseFare, (int) (0.1 * economyClassComputedOverbookingLevel));
        generateEconomyClass4BookingClassEntity(flight, 1.1 * baseFare, (int) (0.2 * economyClassComputedOverbookingLevel));
        generateEconomyClass5BookingClassEntity(flight, 0.8 * baseFare, 0);

    }

    @Override
    public void autoPriceToDepartureAndUnpricedFlights(int monthToDeparture) {
            
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthToDeparture);
        Date endingDate = cal.getTime();
        Query q = entityManager.createQuery("SELECT a FROM FlightEntity a WHERE a.departured = :departured AND a.departureDate <= :endingDate");
        q.setParameter("departured", false);
        q.setParameter("endingDate", endingDate);
        List<FlightEntity> candidateFlights = new ArrayList<>();
        candidateFlights = q.getResultList();

//        System.out.println("candidateFlights = " + candidateFlights);
        for (FlightEntity flight:candidateFlights) {
//            System.out.println(flight.getBookingClasses());
            if (flight.getBookingClasses().isEmpty() && (flight.getAircraft() != null)) {
                System.out.println("created booking classes for " + flight);
//                System.out.println("before:");
//                System.out.println(flight.getBookingClasses());
                
                // auto create all 9 booking classes
                automaticallyCreateBookingClass(flight);
                // auto create yield management rules
                yieldManagementSessionBean.autoCreateRulesForFlight(flight);
//                entityManager.refresh(flight);
//                System.out.println("after:");
//                System.out.println(flight.getBookingClasses());
            }
        }        
    }

}
