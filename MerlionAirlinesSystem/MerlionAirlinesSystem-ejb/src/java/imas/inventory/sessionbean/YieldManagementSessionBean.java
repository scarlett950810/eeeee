/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.Date;
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
public class YieldManagementSessionBean implements YieldManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Double getRoutePopularity(RouteEntity route) {
        Double totalRevenue = 0.0;
        Integer totalSeatNo = 0;

        Query queryForFlightsUnderRoute = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.route = :route");
        queryForFlightsUnderRoute.setParameter("route", route);
        List<FlightEntity> flightsUnderRoute = (List<FlightEntity>) queryForFlightsUnderRoute.getResultList();
//        System.out.println(flightsUnderRoute);
        for (FlightEntity f : flightsUnderRoute) {
            Query queryForAllTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t WHERE t.flight = :flight");
            queryForAllTicketsUnderFlight.setParameter("flight", f);
            List<TicketEntity> tickets = queryForAllTicketsUnderFlight.getResultList();

            for (TicketEntity t : tickets) {
                totalSeatNo = totalSeatNo + 1;
                totalRevenue = totalRevenue + t.getBookingClass().getPrice();
            }

            return totalRevenue / totalSeatNo;

        }
        return 0.0;
    }

    @Override
    public void runYieldManagementRule1(FlightEntity flight,
            Integer timeToDepartureInDaysParameter, Double totalRevenueToTotalCostParameter,
            Integer changeEconomyClass1Parameter, Integer changeEconomyClass2Parameter,
            Integer changeEconomyClass3Parameter, Integer changeEconomyClass4Parameter) {
//        TODO: change to the commented line.
//        int nowToDeparture = new Date().compareTo(flight.getEstimatedDepartureTime());
        int nowToDeparture = 100;
        
        double totalCost = 0.0;
        double totalRevenueTillNow = 0.0;

        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t WHERE t.flight = :flight");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();

        for (TicketEntity t : tickets) {
            totalRevenueTillNow = totalRevenueTillNow + t.getBookingClass().getPrice();
        }
    }

}
