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
        
        for (int i = 0; i < number; i++) {
            TicketEntity ticketEntity = new TicketEntity(bookingClass.getFlight(), bookingClass.getSeatClass(),
                    bookingClass.getName(), bookingClass.getPrice());
            entityManager.persist(ticketEntity);
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
    public List<TicketEntity> createTicketEntitiesWithoutPersisting(BookingClassEntity bookingClass, int number) {
        List<TicketEntity> tickets = new ArrayList<>();
        
        for (int i = 0; i < number; i++) {
            TicketEntity ticketEntity = new TicketEntity(bookingClass.getFlight(), bookingClass.getSeatClass(),
                    bookingClass.getName(), bookingClass.getPrice());
            tickets.add(ticketEntity);
        }
        
        return tickets;
    }

    @Override
    public void runYieldManagementsRulesOnFlight(FlightEntity flight) {
        Query queryForRules = entityManager.createQuery("SELECT r FROM YieldManagementRuleEntity r WHERE r.flight = :flight");
        queryForRules.setParameter("flight", flight);
        
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
                + "WHERE t.flight = : flight AND t.bookingClassName = :bookingClassName");
        queryForAllCurrentTicketsUnderBookingClass.setParameter("flight", bookingClassEntity.getFlight());
        queryForAllCurrentTicketsUnderBookingClass.setParameter("bookingClassName", bookingClassEntity.getName());
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
        
        for (FlightEntity flight: allUndeparturedFlights) {
            
            Query queryForBookingClasses = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
            queryForBookingClasses.setParameter("flight", flight);
            if (queryForBookingClasses.getResultList().size() > 0) {
                allAvailableFlights.add(flight);
            }
        }
        
        return allAvailableFlights;
    }

}
