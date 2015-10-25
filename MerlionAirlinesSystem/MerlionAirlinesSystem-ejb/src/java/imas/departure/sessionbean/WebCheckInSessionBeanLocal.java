/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.TicketEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface WebCheckInSessionBeanLocal {

    List<TicketEntity> getCheckInTicket(String passportNumber, String referenceNumber);
    
    List<List<SeatHelperClass>> fetchAllSeats(Long aircraftID, Long flightID, String bookingClass);

    void completeWebCheckIn(List<List<SeatHelperClass>> seatHelper, TicketEntity ticket);
    
}
