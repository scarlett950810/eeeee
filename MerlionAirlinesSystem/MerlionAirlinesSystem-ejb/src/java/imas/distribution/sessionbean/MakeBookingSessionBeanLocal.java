/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.PassengerEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface MakeBookingSessionBeanLocal {

    String generateItinerary(List<FlightEntity> flights, List<PassengerEntity> passengers, String title, String firstName, String lastName, String address, String city, String country, String email, String contactNumber, String status);

    List<List<SeatHelperClass>> fetchAllSeats(Long aircraftID, Long flightID, String seatClass);
    
}
