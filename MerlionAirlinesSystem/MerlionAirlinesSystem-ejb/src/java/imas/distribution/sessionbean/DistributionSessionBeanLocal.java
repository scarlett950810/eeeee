/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface DistributionSessionBeanLocal {

    public void makeBooking(BookingClassEntity bookingClass, int number);

    public int getQuotaLeft(BookingClassEntity bookingClassEntity);

    public List<FlightEntity> getAllAvailableFlights();
    
}
