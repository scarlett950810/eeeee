/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.distribution.entity.TicketEntity;
import imas.inventory.entity.BookingClassRuleSetEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
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
        BookingClassEntity bc = new BookingClassEntity().FirstClassBookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);

        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createFirstClassBookingClassRule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateBusinessClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().BusinessClassBookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createBusinessClassBookingClassRule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generatePremiumEconomyClassBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().PremiumEconomyClassBookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createPremiumEconomyClassBookingClassRule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateEconomyClass1BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().EconomyClass1BookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createEconomyClassBookingClass1Rule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateEconomyClass2BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().EconomyClass2BookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createEconomyClassBookingClass2Rule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateEconomyClass3BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().EconomyClass3BookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
                
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createEconomyClassBookingClass3Rule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateEconomyClass4BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().EconomyClass4BookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
                
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createEconomyClassBookingClass4Rule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateEconomyClass5BookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().EconomyClass5BookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
                
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createEconomyClassBookingClass5Rule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateFirstClassAgencyBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().FirstClassAgencyBookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createFirstClassAgencyBookingClassRule(bc);
        entityManager.persist(bcrs);
    }
    
    @Override
    public void generateBusinessClassAgencyBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().BusinessClassAgencyBookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
                
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createBusinessClassAgencyBookingClassRule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generatePremiumEconomyClassAgencyBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().PremiumEconomyClassAgencyBookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createPremiumEconomyClassAgencyBookingClassRule(bc);
        entityManager.persist(bcrs);
    }

    @Override
    public void generateEconomyClassAgencyBookingClassEntity(FlightEntity flight, double price, int quota) {
        FlightEntity flightToManage = entityManager.find(FlightEntity.class, flight.getId());
        BookingClassEntity bc = new BookingClassEntity().EconomyClassAgencyBookingClassEntity(flightToManage, price, quota);
        entityManager.persist(bc);
        
        BookingClassRuleSetEntity bcrs = new BookingClassRuleSetEntity().createEconomyClassAgencyBookingClassRule(bc);
        entityManager.persist(bcrs);
    }

    // for all departured flights, total departured tickets / total tickets
    @Override
    public double computeHistoricalShowRate(RouteEntity route) {
//        System.out.println("Computing the latest historical show rate:");
        int totalEconomyClassTickets = 0;
        int issuedEconomyClassTickets = 0;

        // get all flights that has been departured.
        Query queryForAllDeparturedFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.route = :route AND f.departured = :departured");
        queryForAllDeparturedFlights.setParameter("route", route);
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
                if (t.getIssued()) {
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
        Double latestShowRate = computeHistoricalShowRate(flight.getRoute());
        costSessionBean.updateShowRate(flight.getRoute(), latestShowRate);
        Integer economyClassComputedOverbookingLevel = (int) (economyClassCapacity / latestShowRate);

        Double costPerSeatPerMile = costSessionBean.getCostPerSeatPerMile(flight.getRoute());
        Double distance = flight.getRoute().getDistance();
        Double baseFare = costPerSeatPerMile * distance;

        int firstClassAgencyBCQuota = (int) 0.1 * firstClassCapacity;
        int firstClassBCQuota = firstClassCapacity - firstClassAgencyBCQuota;
        int businessClassAgencyBCQuota = (int) 0.1 * businessClassCapacity;
        int businessClassBCQuota = businessClassCapacity - businessClassAgencyBCQuota;
        int premiumEconomyClassAgencyBCQuota = (int) premiumEconomyClassCapacity;
        int premiumEconomyClassBCQuota = premiumEconomyClassCapacity - premiumEconomyClassAgencyBCQuota;

        generateFirstClassBookingClassEntity(flight, 15 * baseFare, firstClassBCQuota);
        generateBusinessClassBookingClassEntity(flight, 6 * baseFare, businessClassBCQuota);
        generatePremiumEconomyClassBookingClassEntity(flight, 4 * baseFare, premiumEconomyClassBCQuota);
        generateEconomyClass1BookingClassEntity(flight, 3 * baseFare, 0);
        generateEconomyClass2BookingClassEntity(flight, 2.5 * baseFare, (int) (0.3 * economyClassComputedOverbookingLevel));
        generateEconomyClass3BookingClassEntity(flight, 2 * baseFare, (int) (0.4 * economyClassComputedOverbookingLevel));
        generateEconomyClass4BookingClassEntity(flight, 1.1 * baseFare, (int) (0.2 * economyClassComputedOverbookingLevel));
        generateEconomyClass5BookingClassEntity(flight, 0.8 * baseFare, 0);

        generateFirstClassAgencyBookingClassEntity(flight, 15 * baseFare, firstClassAgencyBCQuota);
        generateBusinessClassAgencyBookingClassEntity(flight, 6 * baseFare, businessClassAgencyBCQuota);
        generateFirstClassAgencyBookingClassEntity(flight, 4 * baseFare, premiumEconomyClassAgencyBCQuota);
        generateEconomyClassAgencyBookingClassEntity(flight, 1.5 * baseFare, (int) (0.1 * economyClassComputedOverbookingLevel));

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

        for (FlightEntity flight : candidateFlights) {
            if (flight.getBookingClasses().isEmpty() && (flight.getAircraft() != null)) {
                System.out.println("created booking classes for " + flight);
                // auto create all 9 booking classes and associated booking class rules
                automaticallyCreateBookingClass(flight);
                // auto create yield management rules
                yieldManagementSessionBean.autoCreateRulesForFlight(flight);
            }
        }
    }

}
