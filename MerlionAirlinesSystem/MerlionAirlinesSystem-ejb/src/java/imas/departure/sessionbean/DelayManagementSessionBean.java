/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.departure.sessionbean;

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
public class DelayManagementSessionBean implements DelayManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void fetchDelayFlights(FlightEntity flight) {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.aircraft = :aircraft");
        query.setParameter("aircraft", flight.getAircraft());
        List<FlightEntity> flights = (List<FlightEntity>) query.getResultList();
        Collections.sort(flights);
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).equals(flight)) {
                FlightEntity nextFlight = flights.get(i + 1);
//                System.out.print(nextFlight);
                Date arriveTime = flight.getActualArrivalDate();
                int buffer = nextFlight.getAircraft().getTurnAroundTime().intValue();
                Date readyTime = new Date(arriveTime.getTime() + (1000 * 60 * buffer));
//                System.out.print(readyTime);

//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.YEAR, 3);
//                readyTime = cal.getTime();
//                System.out.print(readyTime);
                Date nextTime = nextFlight.getDepartureDate();
                if (readyTime.after(nextTime)) {
                    nextFlight.setEstimateDepartureDate(readyTime);
                    em.merge(nextFlight);
//                    System.out.print("delay!!!!!!!!");
                }
                break;
            }
        }

    }

    @Override
    public List<FlightEntity> fetchFlights(String base) {
        Query query = em.createQuery("SELECT f FROM FlightEntity f ");
    

        List<FlightEntity> flights = (List<FlightEntity>) query.getResultList();
        List<FlightEntity> delayFlights = new ArrayList<FlightEntity>();

        if (flights.isEmpty()) {
              
            return null;
        } else {
            for (int i = 0; i < flights.size(); i++) {
                System.out.print("no no no no no no no");
                System.out.print(flights.get(i).getEstimateDepartureDate());
                System.out.print(flights.get(i).getActualArrivalDate());
                
                if (flights.get(i).getEstimateDepartureDate() != null && flights.get(i).getActualArrivalDate() == null) {
                    delayFlights.add(flights.get(i));
                    System.out.print("get !!!!!!!!delay!!!!!!!!!!");
                }
            }
        }
        return delayFlights;
    }

}
