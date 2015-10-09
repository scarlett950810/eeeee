/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
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
public class inventoryRevenueManagementSessionBean implements inventoryRevenueManagementSessionBeanLocal {
    @EJB
    private SeatsManagementSessionBeanLocal seatsManagementSessionBean;

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<FlightEntity> fetchFlight() {
        
        Query query = em.createQuery("SELECT f FROM FlightEntity f");
        
        List<FlightEntity> flights = (List<FlightEntity>)query.getResultList();
        if(flights.isEmpty()){
            return null;
        }else{
            return flights;
            
        }
    }

    @Override
    public List<BookingClassEntity> fetchBookingClass(Long flightID) {
        Query query = em.createQuery("SELECT b FROM BookingClassEntity b WHERE b.flight.id =:flightID ORDER BY b.price DESC");
        query.setParameter("flightID", flightID);
        
        List<BookingClassEntity> bookingClassList = (List<BookingClassEntity>)query.getResultList();
        if(bookingClassList.isEmpty()){
            System.out.print("The flight is not associated with any booking class.");
            return null;
        }else{
            System.out.print("get");
            return bookingClassList;
        }
    }

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
        Query query = em.createQuery("SELECT b FROM BookingClassEntity b WHERE b.id = :bookingClassID");
        query.setParameter("bookingClassID", bookingClassID);
        
        BookingClassEntity bookingClass = (BookingClassEntity) query.getSingleResult();
        bookingClass.setPrice(newPrice);
        System.out.print("pricing changed");
    }

    @Override
    public int checkSeatsCapacity(FlightEntity selectedFlight) {
        return seatsManagementSessionBean.getEconomyClassCapacity(selectedFlight);
    }

    
    
}
