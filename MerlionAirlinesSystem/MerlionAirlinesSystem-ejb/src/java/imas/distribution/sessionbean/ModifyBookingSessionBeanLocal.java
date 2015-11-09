/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.PNREntity;
import imas.distribution.entity.TicketEntity;
import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface ModifyBookingSessionBeanLocal {

    PNREntity retrievePNRRecord(String referenceNumber);

    List<TicketEntity> getTicketList(String referenceNumber, String passportNumber);

    void flushModification(TicketEntity ticket);

    public TicketEntity modifyTicket(TicketEntity originalTicket, FlightEntity newFlight, BookingClassEntity newBookingClass);
    
}
