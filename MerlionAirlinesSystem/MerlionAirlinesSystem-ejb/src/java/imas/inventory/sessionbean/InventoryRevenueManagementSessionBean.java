/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.FlightEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Howard
 */
@Stateless
public class InventoryRevenueManagementSessionBean implements InventoryRevenueManagementSessionBeanLocal {
    
    @EJB
    private BookingClassesManagementSessionBeanLocal bookingClassesManagementSessionBean;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Integer computeSoldSeats(Long bookingClassID) {
        BookingClassEntity bookingClass = em.find(BookingClassEntity.class, bookingClassID);
        Query query = em.createQuery("SELECT COUNT(t) FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.bookingClassName = :bookingClassName");
        query.setParameter("flight", bookingClass.getFlight());
        query.setParameter("bookingClassName", bookingClass.getName());
        
        Integer soldSeats = ((Long) query.getSingleResult()).intValue();
        System.out.print(soldSeats);
        return soldSeats;
    }

    @Override
    public void updateBookingClassQuota(Long bookingClassID, Integer quota) {
        Query query = em.createQuery("SELECT b FROM BookingClassEntity b WHERE b.id = :bookingClassID");
        query.setParameter("bookingClassID", bookingClassID);
        
        BookingClassEntity bookingClass = (BookingClassEntity) query.getSingleResult();
        bookingClass.setQuota(quota);
        System.out.print("update success");
    }

    @Override
    public void updateBookingClassPricing(Long bookingClassID, Double newPrice) {
        Query queryForBookingClass = em.createQuery("SELECT b FROM BookingClassEntity b WHERE b.id = :bookingClassID");
        queryForBookingClass.setParameter("bookingClassID", bookingClassID);
        
        BookingClassEntity bookingClass = (BookingClassEntity) queryForBookingClass.getSingleResult();
        bookingClass.setPrice(newPrice);
        System.out.print("pricing changed");
    }

    @Override
    public int checkSeatsCapacity(FlightEntity selectedFlight) {
        return bookingClassesManagementSessionBean.getSeatClassCapacity(selectedFlight, "Economy Class");
    }
    
}