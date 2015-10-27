/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.departure.sessionbean;

import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lei
 */
@Local
public interface PassengerCheckInSessionBeanLocal {

    public List<FlightEntity> fetchComingFlights(String base);

    public void intiFFF(FlightEntity flight);

    public void update(TicketEntity ticket, Double actualBaggageWeight, Boolean issued);

    public void update(TicketEntity ticket);

}
