/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.inventory.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.util.ArrayList;
import java.util.List;
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

//        generateBookingClass(flight, "First Class", "First Class", , );
//        generateBookingClass(flight, "Business Class", "Business Class", , );
//        generateBookingClass(flight, "Premium Economy Class", "Premium Economy Class", , );
//        generateBookingClass(flight, "Economy Class", "Economy Class 1", , );
//        generateBookingClass(flight, "Economy Class", "Economy Class 2", , );
//        generateBookingClass(flight, "Economy Class", "Economy Class 3", , );
    
    @Override
    public List<FlightEntity> getFlightsWithoutBookingClass() {
        List<FlightEntity> flightsWithoutBookingClass = new ArrayList();
        
        Query queryForAllFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> allFlights = (List<FlightEntity>) queryForAllFlights.getResultList();
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
    public void generateBookingClass(FlightEntity flight, String seatClass, String bookingClassName, double price, int quota) {
        entityManager.persist(new BookingClassEntity(flight, seatClass, bookingClassName, price, quota));
    }
    
    // to be optimized
    // to add smoothing constant, date, etc. in to input
    // to change output into normal distribution model (mean and variance)
    @Override
    public double computeHistoricalNSR() {
        int totalEconomyClassTickets = 0;
        int issuedEconomyClassTickets = 0;
        
        // get all flights that has been departured.
        Query queryForAllDeparturedFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.hasDepartured = :hasDepartured");
        queryForAllDeparturedFlights.setParameter("hasDepartured", true);
        List<FlightEntity> allDeparturedFlights = (List<FlightEntity>) queryForAllDeparturedFlights.getResultList();
        
        // get all economy seats on each departured flights
        for (FlightEntity f:allDeparturedFlights) {
            Query queryForTickets = entityManager.createQuery("SELECT t FROM TicketEntity t WHERE t.flight = :flight "
                    + "AND t.seat.seatClass = :seatClass ");
            queryForTickets.setParameter("flight", f);
            queryForTickets.setParameter("seatClass", "Economy Class");
            List <TicketEntity> tickets = (List <TicketEntity>) queryForTickets.getResultList();
            
            for (TicketEntity t:tickets) {
                totalEconomyClassTickets = totalEconomyClassTickets + 1;
                if (t.isIssued()) {
                    issuedEconomyClassTickets = issuedEconomyClassTickets + 1;
                }
            }
        };
        
        if (totalEconomyClassTickets > 0) {
            double nsr = 1 - 1.0*issuedEconomyClassTickets/totalEconomyClassTickets;
            return nsr;
        } else {
            // no historical records available
            return 0;
        }
        
    }
  
}
