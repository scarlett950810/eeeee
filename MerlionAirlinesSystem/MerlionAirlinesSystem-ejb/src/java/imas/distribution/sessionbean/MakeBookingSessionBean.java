/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.PNREntity;
import imas.distribution.entity.PassengerEntity;
import imas.distribution.entity.TicketEntity;
import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.entity.SeatEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Howard
 */
@Stateless
public class MakeBookingSessionBean implements MakeBookingSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String generateItinerary(List<FlightEntity> flights, List<PassengerEntity> passengers, String title, String firstName, String lastName, String address, String city, String country, String email, String contactNumber, String status) {

        String referenceNumber = UUID.randomUUID().toString();
        referenceNumber = referenceNumber.replaceAll("-", "").substring(0, 8);

        for (int i = 0; i < passengers.size(); i++) {
            entityManager.persist(passengers.get(i));
            List<TicketEntity> tickets = passengers.get(i).getTickets();
            for (int j = 0; j < tickets.size(); j++) {
                entityManager.persist(tickets.get(j));
            }
        }

        PNREntity PNR = new PNREntity(referenceNumber);
        PNR.setFlights(flights);
        PNR.setPassengers(passengers);
        PNR.setTitle(title);
        PNR.setFirstName(firstName);
        PNR.setLastName(lastName);
        PNR.setEmail(email);
        PNR.setMobilePhone(contactNumber);
        PNR.setAddress(address);
        PNR.setCity(city);
        PNR.setNation(country);
        PNR.setStatus(status);

        System.out.print(referenceNumber);
        System.out.print(passengers);
        System.out.print(title);
        System.out.print(firstName);
        System.out.print(lastName);
        System.out.print(email);
        System.out.print(contactNumber);
        System.out.print(address);
        System.out.print(city);
        System.out.print(country);
        System.out.print(status);

        entityManager.persist(PNR);

        return referenceNumber;
    }

    @Override
    public List<List<SeatHelperClass>> fetchAllSeats(Long aircraftID, Long flightID, String seatClass) {

        Query query1 = entityManager.createQuery("SELECT s FROM SeatEntity s where s.aircraft.id = :aircraftID");
        query1.setParameter("aircraftID", aircraftID);
        
        List<SeatEntity> seats = (List<SeatEntity>)query1.getResultList();
        
        Query query2 = entityManager.createQuery("SELECT t FROM TicketEntity t where t.flight.id = :flightID");
        query2.setParameter("flightID", flightID);
        
        List<SeatEntity> occupiedSeats = (List<SeatEntity>)query2.getResultList();
        
        if(seats.isEmpty() == false){
            List<List<SeatHelperClass>> allSeats = new ArrayList<>();
            List<SeatHelperClass> seathelper = null;
            
            for (Iterator<SeatEntity> iterator = seats.iterator(); iterator.hasNext();) {
                SeatEntity next = iterator.next();
                SeatHelperClass temp = new SeatHelperClass();
                if(next.getSeatNo().indexOf("A") != -1){
                    if(seathelper != null){
                        allSeats.add(seathelper);
                    }
                    seathelper = new ArrayList<>();
                    temp.setSeatNumber(next.getSeatNo());
                    temp.setEligible(next.getSeatClass().equals(seatClass));
                    temp.setOccupied(occupiedSeats.contains(next));
                    seathelper.add(temp);
                }else{
                    temp.setSeatNumber(next.getSeatNo());
                    temp.setEligible(next.getSeatClass().equals(seatClass));
                    temp.setOccupied(occupiedSeats.contains(next));
                    seathelper.add(temp);
                }
                
            }
            
            return allSeats;
            
        }else{
            return null;
        }
    }

}
