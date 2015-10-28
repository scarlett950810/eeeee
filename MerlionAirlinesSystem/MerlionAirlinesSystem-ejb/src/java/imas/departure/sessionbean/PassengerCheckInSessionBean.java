/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.departure.sessionbean;

import imas.distribution.entity.PassengerEntity;
import imas.distribution.entity.TicketEntity;
import imas.planning.entity.FlightEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
        currentDate = new Date(currentDate.getTime() - (1000 * 60 * 60));
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
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(p5);
        TicketEntity t1 = new TicketEntity(flight, "First", 1000.0, p1);
        TicketEntity t2 = new TicketEntity(flight, "First", 1000.0, p2);
        TicketEntity t3 = new TicketEntity(flight, "First", 1000.0, p3);
        TicketEntity t4 = new TicketEntity(flight, "First", 1000.0, p4);
        TicketEntity t5 = new TicketEntity(flight, "First", 1000.0, p5);
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

    @Override
    public void update(TicketEntity ticket, Double actualBaggageWeight, Boolean issued) {
        ticket.setActualBaggageWeight(actualBaggageWeight);
        ticket.setIssued(issued);
        em.merge(ticket);
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
