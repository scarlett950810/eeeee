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

        for(int i=0; i<passengers.size(); i++){
            entityManager.persist(passengers.get(i));
            List<TicketEntity> tickets = passengers.get(i).getTickets();
            for(int j=0; j<tickets.size();j++){
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
    public List<PassengerEntity> populateData() {

        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = 'CAN'");
        List<AirportEntity> airports = (List<AirportEntity>) query.getResultList();

        if (airports.isEmpty()) {
            AirportEntity a2 = new AirportEntity(false, "Guangzhou", "Baiyun Airport", "CAN", "China");
            AirportEntity a4 = new AirportEntity(true, "Singapore", "Changi Airport", "SGC", "Singapore");

            entityManager.persist(a2);
            entityManager.persist(a4);

            RouteEntity r1 = new RouteEntity(a2, a4);
            RouteEntity r2 = new RouteEntity(a4, a2);

            entityManager.persist(r1);
            entityManager.persist(r2);

            FlightEntity f1 = new FlightEntity("ML3102", null, r1);
            FlightEntity f2 = new FlightEntity("ML3104", null, r2);

            entityManager.persist(f1);
            entityManager.persist(f2);

            BookingClassEntity b1 = new BookingClassEntity(f1, "Economy Class", "X", 167.5, 20);
            BookingClassEntity b2 = new BookingClassEntity(f2, "Economy Class", "X", 167.5, 20);

            entityManager.persist(b1);
            entityManager.persist(b2);

            SeatEntity s1 = new SeatEntity(null, null, "Economy Class");
            SeatEntity s2 = new SeatEntity(null, null, "Economy Class");

            entityManager.persist(s1);
            entityManager.persist(s2);

            TicketEntity t1 = new TicketEntity(f1,null,0);
            TicketEntity t2 = new TicketEntity(f2,null,0);

//        entityManager.persist(t1);
//        entityManager.persist(t2);
            List<TicketEntity> tickets = new ArrayList<>();
            tickets.add(t1);
            tickets.add(t2);

            PassengerEntity p1 = new PassengerEntity(tickets);
            List<PassengerEntity> passengers = new ArrayList<>();
            passengers.add(p1);

            return passengers;
        } else {
            return null;
        }
    }
}
