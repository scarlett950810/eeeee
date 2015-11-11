/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author Lei
 */
@Remote
public interface InventoryRevenueManagementSessionBeanRemote {

    Integer computeSoldSeats(Long bookingClassID);

    void updateBookingClassQuota(Long bookingClassID, Integer quota);

    void updateBookingClassPricing(Long bookingClassID, Double newPrice);

    int checkSeatsCapacity(FlightEntity selectedFlight);

    public Double getFlightTotalRevenue(FlightEntity flight);

    public Double getFlightTotalCost(FlightEntity flight);

    public Double getRouteTotalRevenueDuringDuration(RouteEntity route, Date from, Date to);

    public Double getRouteTotalCostDuringDuration(RouteEntity route, Date from, Date to);
}
