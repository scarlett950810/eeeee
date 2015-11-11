/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.sessionbean;

import DDS.entity.AgencyEntity;
import GDS.entity.GDSFlightEntity;
import GDS.entity.GDSPassengerEntity;
import GDS.entity.GDSTicketEntity;
import imas.crm.entity.MemberEntity;
import imas.distribution.entity.PassengerEntity;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface GDSMakeBookingSessionBeanLocal {
    String generateGDSItinerary(List<GDSFlightEntity> flights, List<GDSPassengerEntity> passengers, String title, String firstName, String lastName, String address, String city, String country, String email, String contactNumber, String zipCode, String status, double totalPrice);

    List<GDSTicketEntity> retrieveGDSCheckInTicket(String passportNumber, String referenceNumber);

    public String generateItineraryForAgency(List<FlightEntity> flights, List<PassengerEntity> passengers, 
            String title, String firstName, String lastName, String address, String city, String country, String email, String contactNumber, 
            String zipCode, String status, double totalPrice, AgencyEntity agency);
    
}
