/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import DDS.entity.AgencyEntity;
import imas.crm.entity.MemberEntity;
import imas.distribution.entity.PNREntity;
import imas.distribution.entity.PassengerEntity;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import imas.utility.sessionbean.EmailManager;
import java.util.Date;
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
    public String generateItinerary(List<FlightEntity> flights, List<PassengerEntity> passengers, String title, String firstName, String lastName, String address, String city, String country, String email, String contactNumber, String zipCode, String status, double totalPrice, MemberEntity member) {

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

        if (member != null) {
            System.out.print(member);
            for (int i = 0; i < passengers.size(); i++) {
                System.out.print(passengers.get(i).getPassportNumber());
                System.out.print(member.getPassportNumber());
                if (passengers.get(i).getPassportNumber().equals(member.getPassportNumber())) {
                    System.out.println("Enter add ticket");

                    if (member.getTicketList() == null) {
                        List<TicketEntity> tickets = passengers.get(i).getTickets();
                        System.out.println(tickets);
                        member.setTicketList(tickets);
                    } else {
                        List<TicketEntity> tickets = member.getTicketList();
                        tickets.addAll(passengers.get(i).getTickets());
                        System.out.println(tickets);
                        member.setTicketList(tickets);
                    }

                    entityManager.merge(member);
                }

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

        entityManager.persist(PNR);

        String flight = "";
        for (int i = 0; i < flights.size(); i++) {
            FlightEntity flightEntity = flights.get(i);
            flight = flight + "<tr><td>" + flightEntity.getFlightNo() + "</td><td>" + flightEntity.getRoute().getOriginAirport().getCityName() + "," + flightEntity.getRoute().getOriginAirport().getAirportCode()
                    + "</td><td>" + flightEntity.getDepartureDate() + "</td><td>" + flightEntity.getRoute().getDestinationAirport().getCityName() + "," + flightEntity.getRoute().getDestinationAirport().getAirportCode() + "</td><td>"
                    + flightEntity.getArrivalDate() + "</td></tr>";

        }

        String passenger = "";
        for (int j = 0; j < passengers.size(); j++) {
            PassengerEntity passengerEntity = passengers.get(j);
            passenger = passenger + "<tr><td>" + passengerEntity.getTitle() + " " + passengerEntity.getFirstName() + " " + passengerEntity.getLastName() + "</td><td>"
                    + passengerEntity.getPassportNumber() + "</td><td>" + passengerEntity.getNationality() + "</td></tr>";
        }

        EmailManager.runBookingConfirmation(email, "Merlion Airlines|Itineary for your upcoming flight", flight, passenger, referenceNumber);

        return referenceNumber;
    }

    @Override
    public List<TicketEntity> retrieveCheckInTicket(String passportNumber, String referenceNumber) {
        Query query = entityManager.createQuery("SELECT t FROM TicketEntity t where t.referenceNumber = :referenceNumber AND t.passenger.passportNumber = :passportNumber");
        query.setParameter("referenceNumber", referenceNumber);
        query.setParameter("passportNumber", passportNumber);

        List<TicketEntity> tickets = (List<TicketEntity>) query.getResultList();
        TicketEntity ticket;
        int flag;
        Date currentDate = new Date();

        for (int i = 0; i < tickets.size(); i++) {
            ticket = tickets.get(i);

            if (ticket.getFlight().getDepartureDate().before(currentDate)) {
                tickets.remove(ticket);
            } else {
                if (currentDate.getTime() - ticket.getFlight().getDepartureDate().getTime() > 259200000) {
                    tickets.remove(ticket);
                }
            }
        }

        return tickets;
    }

    @Override
    public String generateItineraryForAgency(List<FlightEntity> flights, List<PassengerEntity> passengers,
            String title, String firstName, String lastName, String address, String city, String country, String email, String contactNumber,
            String zipCode, String status, double totalPrice, AgencyEntity agency) {

        String referenceNumber = UUID.randomUUID().toString();
        referenceNumber = referenceNumber.replaceAll("-", "").substring(0, 8);

        AgencyEntity agencyManaged = entityManager.find(AgencyEntity.class, agency.getId());
        List<TicketEntity> originalTickets = agencyManaged.getTickets();
        for (int i = 0; i < passengers.size(); i++) {
            entityManager.persist(passengers.get(i));
            List<TicketEntity> tickets = passengers.get(i).getTickets();
            for (int j = 0; j < tickets.size(); j++) {
                TicketEntity ticket = tickets.get(j);
                ticket.setReferenceNumber(referenceNumber);
                ticket.setAgency(agency);
                entityManager.persist(ticket);
                originalTickets.add(ticket);
            }
        }

        agencyManaged.setTickets(originalTickets);
        entityManager.merge(agencyManaged);

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

        entityManager.persist(PNR);

        String flight = "";
        for (int i = 0; i < flights.size(); i++) {
            FlightEntity flightEntity = flights.get(i);
            flight = flight + "<tr><td>" + flightEntity.getFlightNo() + "</td><td>" + flightEntity.getRoute().getOriginAirport().getCityName() + "," + flightEntity.getRoute().getOriginAirport().getAirportCode()
                    + "</td><td>" + flightEntity.getDepartureDate() + "</td><td>" + flightEntity.getRoute().getDestinationAirport().getCityName() + "," + flightEntity.getRoute().getDestinationAirport().getAirportCode() + "</td><td>"
                    + flightEntity.getArrivalDate() + "</td></tr>";

        }

        String passenger = "";
        for (int j = 0; j < passengers.size(); j++) {
            PassengerEntity passengerEntity = passengers.get(j);
            passenger = passenger + "<tr><td>" + passengerEntity.getTitle() + " " + passengerEntity.getFirstName() + " " + passengerEntity.getLastName() + "</td><td>"
                    + passengerEntity.getPassportNumber() + "</td><td>" + passengerEntity.getNationality() + "</td></tr>";
        }

        EmailManager.runBookingConfirmation(email, "Merlion Airlines|Itineary for your upcoming flight", flight, passenger, referenceNumber);

        return referenceNumber;
    }

}
