/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.TicketEntity;
import imas.inventory.entity.BookingClassEntity;
import imas.inventory.entity.YieldManagementRuleEntity;
import imas.inventory.sessionbean.YieldManagementSessionBeanLocal;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
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
public class DistributionSessionBean implements DistributionSessionBeanLocal {
    @EJB
    private YieldManagementSessionBeanLocal yieldManagementSessionBean;

    @PersistenceContext
    private EntityManager entityManager;
  
    @Override
    public void makeBooking(BookingClassEntity bookingClass, int number) {
//        System.out.println("makeBooking");
//        System.out.println("bookingClass = " + bookingClass);
//        System.out.println("number = " + number);
        
        for (int i = 0; i < number; i++) {
//            System.out.println(i);
            TicketEntity ticketEntity = new TicketEntity(bookingClass);
            entityManager.persist(ticketEntity);
//            System.out.println(ticketEntity);
        }
        
        Query queryForRules = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r WHERE r.flight = :flight");
        queryForRules.setParameter("flight", bookingClass.getFlight());
        
        List<YieldManagementRuleEntity> rules = queryForRules.getResultList();
        
        for (YieldManagementRuleEntity rule: rules) {
            if (rule.isEnabled()) {
                if (rule.isRule1()) {
                    yieldManagementSessionBean.runYieldManagementRule1(rule);
                } else if (rule.isRule2()) {
                    yieldManagementSessionBean.runYieldManagementRule2(rule);
                } else if (rule.isRule3()) {
                    yieldManagementSessionBean.runYieldManagementRule3(rule);
                } else if (rule.isRule4()) {
                    yieldManagementSessionBean.runYieldManagementRule4(rule);
                }
            }                
        }
    }

    @Override
    public int getQuotaLeft(BookingClassEntity bookingClassEntity) {
        Query queryForAllCurrentTicketsUnderBookingClass = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass = :bookingClass");
        queryForAllCurrentTicketsUnderBookingClass.setParameter("bookingClass", bookingClassEntity);
        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderBookingClass.getResultList();
        int numberOfTicketsSold = tickets.size();
        return bookingClassEntity.getQuota() - numberOfTicketsSold;
    }

    @Override
    public List<FlightEntity> getAllAvailableFlights() {
        List<FlightEntity> allAvailableFlights = new ArrayList();
        
        Query queryForAllAvailableFlights = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.departured = :departured");
        queryForAllAvailableFlights.setParameter("departured", false);
        List<FlightEntity> allUndeparturedFlights = queryForAllAvailableFlights.getResultList();
//        
//        Query q = entityManager.createQuery("SELECT a FROM AircraftEntity a");
//        AircraftEntity a1 = (AircraftEntity) q.getResultList().get(0);
//        AircraftEntity a2 = (AircraftEntity) q.getResultList().get(0);
//        
//        int i = 0;
        
        for (FlightEntity flight: allUndeparturedFlights) {
//            System.out.println("here");
//            if (1 /2 == 0) {
//                flight.setAircraftFlight(a1);
//                flight.setFlightNo("ABFFC");
//            } else {
//                flight.setAircraftFlight(a2);
//                flight.setFlightNo("DDFFC");
//            }
//            entityManager.merge(flight);
//            i++;
//            
            
            
            Query queryForBookingClasses = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
            queryForBookingClasses.setParameter("flight", flight);
            if (queryForBookingClasses.getResultList().size() > 0) {
                allAvailableFlights.add(flight);
            }
        }
        
        return allAvailableFlights;
    }
    
}
