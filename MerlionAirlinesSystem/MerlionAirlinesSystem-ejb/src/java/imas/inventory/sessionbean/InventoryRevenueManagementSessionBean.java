/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.distribution.entity.TicketEntity;
import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Date;
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
public class InventoryRevenueManagementSessionBean implements InventoryRevenueManagementSessionBeanLocal,InventoryRevenueManagementSessionBeanRemote {
    
    @EJB
    private CostManagementSessionBeanLocal costManagementSessionBean;
    
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
//        System.out.print(soldSeats);
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
    
    @Override
    public Double getFlightTotalRevenue(FlightEntity flight) {
        
        FlightEntity flightManaged = em.find(FlightEntity.class, flight.getId());
        em.refresh(flightManaged);
        
        if (flightManaged.getRevenue() != null) {
            return flightManaged.getRevenue();
        } else {
            Double totalRevenue = 0.0;
            List<TicketEntity> tickets = flightManaged.getTickets();
            for (TicketEntity ticket : tickets) {
                totalRevenue = totalRevenue + ticket.getPrice();
            }
            flightManaged.setRevenue(totalRevenue);
            em.flush();
            
            return totalRevenue;
        }
        
    }

    /**
     *
     * @param route
     * @param from
     * @param to
     * @return
     */
    @Override
    public Double getRouteTotalRevenueDuringDuration(RouteEntity route, Date from, Date to) {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route = :route AND f.departureDate > :from AND f.departureDate < :to");
        query.setParameter("route", route);
        query.setParameter("from", from);
        query.setParameter("to", to);
        List<FlightEntity> flights = query.getResultList();
        Double totalRevenue = 0.0;
        for (FlightEntity f : flights) {
            totalRevenue = totalRevenue + getFlightTotalRevenue(f);
        }
        
        return totalRevenue;
    }
    
    @Override
    public Double getFlightTotalCost(FlightEntity flight) {
        double costPerSeatPerMile;
        if (flight.getCostPerSeatPerMile() != null) {
            costPerSeatPerMile = flight.getCostPerSeatPerMile();
        } else {
            costPerSeatPerMile = costManagementSessionBean.getCostPerSeatPerMile(flight.getRoute());
        }
//        System.out.println("flight.getCostPerSeatPerMile() = " + flight.getCostPerSeatPerMile());
//        System.out.println("flight.getAircraft().getSeats().size() = " + flight.getAircraft().getSeats().size());
//        double costPerSeatPerMile = costManagementSessionBean.getCostPerSeatPerMile(flight.getRoute());
        return costPerSeatPerMile * flight.getRoute().getDistance() * flight.getAircraft().getSeats().size();
    }
    
    @Override
    public Double getRouteTotalCostDuringDuration(RouteEntity route, Date from, Date to) {
//        System.out.println("getRouteTotalCostDuringDuration");
//        System.out.println(route + " " + from + " " + to);
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route = :route AND f.departureDate > :from AND f.departureDate < :to");
        query.setParameter("route", route);
        query.setParameter("from", from);
        query.setParameter("to", to);
        List<FlightEntity> flights = query.getResultList();
        Double totalCost = 0.0;
        for (FlightEntity f : flights) {
            totalCost = totalCost + getFlightTotalCost(f);
        }
        
        return totalCost;
    }
    
    @Override
    public FlightEntity getRefreshedFlight(FlightEntity flight) {
        return em.find(FlightEntity.class, flight.getId());
    }
}
