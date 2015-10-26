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
import java.util.Date;
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
    public String generateItinerary(List<FlightEntity> flights, List<PassengerEntity> passengers, String title, String firstName, String lastName, String address, String city, String country, String email, String contactNumber, String zipCode, String status, double totalPrice) {

        String referenceNumber = UUID.randomUUID().toString();
        referenceNumber = referenceNumber.replaceAll("-", "").substring(0, 8);

        for (int i = 0; i < passengers.size(); i++) {
            entityManager.persist(passengers.get(i));
            List<TicketEntity> tickets = passengers.get(i).getTickets();
            for (int j = 0; j < tickets.size(); j++) {
                tickets.get(j).setReferenceNumber(referenceNumber);
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
        PNR.setZipCode(zipCode);
        PNR.setTotalPrice(totalPrice);
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
        System.out.print("13");

        return referenceNumber;
    }

    

    @Override
    public List<TicketEntity> retrieveCheckInTicket(String passportNumber, String referenceNumber) {
        Query query = entityManager.createQuery("SELECT t FROM TicketEntity t where t.referenceNumber = :referenceNumber AND t.passenger.passportNumber = :passportNumber");
        query.setParameter("referenceNumber", referenceNumber);
        query.setParameter("passportNumber", passportNumber);
        
        List<TicketEntity> tickets = (List<TicketEntity>)query.getResultList();
        TicketEntity ticket;
        int flag;
        Date currentDate = new Date();
        
        for(int i=0; i<tickets.size(); i++){
            ticket = tickets.get(i);
            
            if(ticket.getFlight().getDepartureDate().before(currentDate)){
                tickets.remove(ticket);
            }else{
                if(currentDate.getTime() - ticket.getFlight().getDepartureDate().getTime() > 259200000){
                    tickets.remove(ticket);
                }
            }
        }
        
        return tickets;
    }

    
}
