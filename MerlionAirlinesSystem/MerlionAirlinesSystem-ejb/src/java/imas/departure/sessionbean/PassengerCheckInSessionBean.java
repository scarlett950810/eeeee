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
            Collections.sort(flights);
            return comingFlights;
        }
    }
}
