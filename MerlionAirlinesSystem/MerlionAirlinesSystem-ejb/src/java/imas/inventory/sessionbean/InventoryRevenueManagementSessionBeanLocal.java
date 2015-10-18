/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface InventoryRevenueManagementSessionBeanLocal {

    Integer computeSoldSeats(Long bookingClassID);

    void updateBookingClassQuota(Long bookingClassID, Integer quota);

    void updateBookingClassPricing(Long bookingClassID, Double newPrice);

    int checkSeatsCapacity(FlightEntity selectedFlight);
    
}
