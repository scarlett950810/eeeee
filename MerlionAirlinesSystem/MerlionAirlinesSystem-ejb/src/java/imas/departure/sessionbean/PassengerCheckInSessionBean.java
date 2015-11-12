/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.departure.sessionbean;

import imas.distribution.entity.PassengerEntity;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.SeatEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Lei
 */
@Stateless
public class PassengerCheckInSessionBean implements PassengerCheckInSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<FlightEntity> fetchComingFlights(String base) {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route.originAirport.airportCode = :base");
        query.setParameter("base", base);

        List<FlightEntity> flights = (List<FlightEntity>) query.getResultList();
        List<FlightEntity> comingFlights = new ArrayList<FlightEntity>();

        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        currentDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24));
        cal.add(Calendar.YEAR, 3);
//        cal.add(Calendar.HOUR, 24);
        Date limitDate = cal.getTime();
        if (flights.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < flights.size(); i++) {
                if (flights.get(i).getDepartureDate().after(currentDate) && flights.get(i).getDepartureDate().before(limitDate)
                        && (flights.get(i).getActualDepartureDate() == null || flights.get(i).getActualArrivalDate() == null)) {
                    comingFlights.add(flights.get(i));
                }
            }
            Collections.sort(comingFlights);
            return comingFlights;
        }
    }

    @Override
    public void intiFFF(FlightEntity flight) {
        flight.setTickets(null);
        PassengerEntity p1 = new PassengerEntity("li" + flight.getFlightNo(), "hao", "12334441");
        PassengerEntity p2 = new PassengerEntity("li2" + flight.getFlightNo(), "hao2", "12334442");
        PassengerEntity p3 = new PassengerEntity("li3" + flight.getFlightNo(), "hao3", "12334443");
        PassengerEntity p4 = new PassengerEntity("li4" + flight.getFlightNo(), "hao4", "12334444");
        PassengerEntity p5 = new PassengerEntity("li5" + flight.getFlightNo(), "hao5", "12334445");
        p1.setTitle("MR");
        p2.setTitle("MR");
        p3.setTitle("MR");
        p4.setTitle("MR");
        p5.setTitle("MR");
        p1.setNationality("Chinese");
        p2.setNationality("Chinese");
        p3.setNationality("Chinese");
        p4.setNationality("Chinese");
        p5.setNationality("Chinese");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString01 = "31-08-1982";
        String dateInString02 = "31-08-2020";
        try {
            Date date = sdf.parse(dateInString01);
            p1.setBirthdate(date);
            p2.setBirthdate(date);
            p3.setBirthdate(date);
            p4.setBirthdate(date);
            p5.setBirthdate(date);
            Date date2 = sdf.parse(dateInString02);
            p1.setPassportExpiry(date2);
            p2.setPassportExpiry(date2);
            p3.setPassportExpiry(date2);
            p4.setPassportExpiry(date2);
            p5.setPassportExpiry(date2);

        } catch (ParseException ex) {
            Logger.getLogger(PassengerCheckInSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(p5);
        TicketEntity t1 = new TicketEntity(flight, "First Class", 1000.0, p1);
        TicketEntity t2 = new TicketEntity(flight, "First Class", 1000.0, p2);
        TicketEntity t3 = new TicketEntity(flight, "First Class", 1000.0, p3);
        TicketEntity t4 = new TicketEntity(flight, "First Class", 1000.0, p4);
        TicketEntity t5 = new TicketEntity(flight, "First Class", 1000.0, p5);
        t1.setFlightWiFi(Boolean.FALSE);
        t2.setFlightWiFi(Boolean.FALSE);
        t3.setFlightWiFi(Boolean.FALSE);
        t4.setFlightWiFi(Boolean.FALSE);
        t5.setFlightWiFi(Boolean.FALSE);
        t1.setPremiumMeal(Boolean.FALSE);
        t2.setPremiumMeal(Boolean.FALSE);
        t3.setPremiumMeal(Boolean.FALSE);
        t4.setPremiumMeal(Boolean.FALSE);
        t5.setPremiumMeal(Boolean.FALSE);
        t1.setExclusiveService(Boolean.FALSE);
        t2.setExclusiveService(Boolean.FALSE);
        t3.setExclusiveService(Boolean.FALSE);
        t4.setExclusiveService(Boolean.FALSE);
        t5.setExclusiveService(Boolean.FALSE);
        t1.setReferenceNumber("AAAAAAAA");
        t2.setReferenceNumber("BBBBBBBB");
        t3.setReferenceNumber("CCCCCCCC");
        t4.setReferenceNumber("DDDDDDDD");
        t5.setReferenceNumber("EEEEEEEE");
        t1.setBaggageWeight(20.0);
        t2.setBaggageWeight(20.0);
        t3.setBaggageWeight(20.0);
        t4.setBaggageWeight(20.0);
        t5.setBaggageWeight(20.0);
        em.persist(t1);
        em.persist(t2);
        em.persist(t3);
        em.persist(t4);
        em.persist(t5);
        List<TicketEntity> newList = new ArrayList<TicketEntity>();
        newList.add(t1);
        newList.add(t2);
        newList.add(t3);
        newList.add(t4);
        newList.add(t5);
        flight.setTickets(newList);
        em.merge(flight);
    }

    public int seatAllocation(TicketEntity ticket) {
        FlightEntity flight = ticket.getFlight();
        Long aircraftID = flight.getAircraft().getId();
        String seatClass;
        if (ticket.getBookingClassName().equals("First Class")) {
            seatClass = "First Class";
        } else if (ticket.getBookingClassName().equals("Business Class")) {
            seatClass = "Business Class";
        } else if (ticket.getBookingClassName().equals("Premium Economy Class")) {
            seatClass = "Premium Economy Class";
        } else {
            seatClass = "Economy Class";
        }

        //all seats
        Query query1 = em.createQuery("SELECT s FROM SeatEntity s where s.aircraft.id = :aircraftID");
        query1.setParameter("aircraftID", aircraftID);
        List<SeatEntity> seats = (List<SeatEntity>) query1.getResultList();
        System.out.print(seats);
        //occupied
        Query query2 = em.createQuery("SELECT t.seat FROM TicketEntity t where t.flight = :flight AND t.seat.seatClass=:seatClass");
        query2.setParameter("flight", flight);
        query2.setParameter("seatClass", seatClass);
        List<SeatEntity> occupiedSeats = (List<SeatEntity>) query2.getResultList();
        System.out.print(occupiedSeats);
        //same class seat
        Query query3 = em.createQuery("SELECT s FROM SeatEntity s where s.aircraft.id = :aircraftID AND s.seatClass=:seatClass");
        query3.setParameter("aircraftID", aircraftID);
        query3.setParameter("seatClass", seatClass);
        List<SeatEntity> sameSeats = (List<SeatEntity>) query3.getResultList();
        System.out.print(sameSeats);

        if (occupiedSeats.size() < sameSeats.size()) {
            for (int i = 0; i < sameSeats.size(); i++) {
                if (!occupiedSeats.contains(sameSeats.get(i))) {
                    ticket.setSeat(sameSeats.get(i));
                    return 1;
                }
            }
        } else {
            for (int i = 0; i < seats.size(); i++) {
                if (!occupiedSeats.contains(seats.get(i))) {
                    if (seatClass.equals("Economy Class")) {
                        ticket.setSeat(seats.get(i));
                        return 2;
                    } else if (seatClass.equals("Premium Economy Class") && (seats.get(i).isBusinessClass() || seats.get(i).isFirstClass())) {
                        ticket.setSeat(seats.get(i));
                        return 2;
                    } else if (seatClass.equals("Business Class") && (seats.get(i).isFirstClass())) {
                        ticket.setSeat(seats.get(i));
                        return 2;
                    } else {
                        return 3;
                    }
                }
            }

        }
        return 3;
    }

    @Override
    public int update(TicketEntity ticket, Double actualBaggageWeight) {
        ticket.setActualBaggageWeight(actualBaggageWeight);
        ticket.setIssued(Boolean.TRUE);
        int result = 1;//has seat or same  1, upgrade 2, no seat 3
        if (ticket.getSeat() == null) {
            System.out.print("WQDWWWQQWQWW");
            result = seatAllocation(ticket);
        }

        em.merge(ticket);
        return result;
//        System.out.print(ticket);
//        System.out.print("shi shi shi");
    }

    @Override
    public void update(TicketEntity ticket) {
        System.out.print(ticket);
        ticket.setBoarded(Boolean.TRUE);
        em.merge(ticket);
    }

}
