/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface SeatsManagementSessionBeanLocal {

    public List<FlightEntity> getFlightsWithoutBookingClass();
    
    public void generateBookingClass(FlightEntity flight, String seatClass, String bookingClassName, double price, int quota);

    
}
