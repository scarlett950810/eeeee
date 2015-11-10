/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.sessionbean;

import GDS.entity.GDSAirportEntity;
import GDS.entity.GDSBookingClassEntity;
import GDS.entity.GDSFlightEntity;
import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class GDSFlightSessionBean implements GDSFlightSessionBeanLocal {

    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void generateFlightsAndBookingClasses(GDSFlightEntity flight, List<GDSBookingClassEntity> bookingClasses) {
        GDSAirportEntity origin = getManagedGDSAirport(flight.getOrigin());
        GDSAirportEntity destination = getManagedGDSAirport(flight.getDestination());
        flight.setOrigin(origin);
        flight.setDestination(destination);
        em.persist(flight);
        for (GDSBookingClassEntity bc : bookingClasses) {
            bc.setGDSflight(flight);
            em.persist(bc);
        }
        em.flush();
    }

    private GDSAirportEntity getManagedGDSAirport(GDSAirportEntity airport) {
        Query queryForAirport = em.createQuery("SELECT a FROM GDSAirportEntity a WHERE a.IATAorFAA = :IATAorFAA");
        queryForAirport.setParameter("IATAorFAA", airport.getIATAorFAA());
        return (GDSAirportEntity) queryForAirport.getResultList().get(0);
    }

    @Override
    public boolean haveDirectFlight(GDSAirportEntity origin, GDSAirportEntity destination, Date startTime) {
        Query queryForAvailableFlights = em.createQuery("SELECT f FROM GDSFlightEntity f where f.origin = :origin AND f.destination = :destination AND"
                + " f.departureDate > :lowerBound AND f.departureDate < :upperBound ORDER BY f.departureDate ASC");
        queryForAvailableFlights.setParameter("origin", origin);
        queryForAvailableFlights.setParameter("destination", destination);
        queryForAvailableFlights.setParameter("lowerBound", startTime);
        queryForAvailableFlights.setParameter("upperBound", flightLookupSessionBean.getDateAfterDays(startTime, 1));

        List<GDSFlightEntity> availableFlights = queryForAvailableFlights.getResultList();
        return !availableFlights.isEmpty();
    }

    @Override
    public List<GDSFlightEntity> getDirectFlights(GDSAirportEntity origin, GDSAirportEntity destination, Date startTime) {
        Query queryForAvailableFlights = em.createQuery("SELECT f FROM GDSFlightEntity f where f.origin = :origin AND f.destination = :destination AND"
                + " f.departureDate > :lowerBound AND f.departureDate < :upperBound ORDER BY f.departureDate ASC");
        queryForAvailableFlights.setParameter("origin", origin);
        queryForAvailableFlights.setParameter("destination", destination);
        queryForAvailableFlights.setParameter("lowerBound", startTime);
        queryForAvailableFlights.setParameter("upperBound", flightLookupSessionBean.getDateAfterDays(startTime, 1));

        List<GDSFlightEntity> availableFlights = queryForAvailableFlights.getResultList();
        return availableFlights;
    }

    @Override
    public List<GDSTransferFlight> getTransferFlightSet(GDSAirportEntity origin, GDSAirportEntity destination, Date startTime) {
        List<GDSTransferFlight> transferFlightList = new ArrayList<>();
        Query queryForDirectDestination = em.createQuery("SELECT f.destination FROM GDSFlightEntity f WHERE f.origin = :origin "
                + "AND f.departureDate > :lowerBound AND f.departureDate < :upperBound ORDER BY f.departureDate ASC");
        queryForDirectDestination.setParameter("origin", origin);
        queryForDirectDestination.setParameter("lowerBound", startTime);
        queryForDirectDestination.setParameter("upperBound", flightLookupSessionBean.getDateAfterDays(startTime, 1));
        List<GDSAirportEntity> directDestinations = queryForDirectDestination.getResultList();
        for (GDSAirportEntity directDestination : directDestinations) {
            if (haveDirectFlight(directDestination, destination, startTime)) {
                List<GDSFlightEntity> transferFlights1 = getDirectFlights(origin, directDestination, startTime);
                List<GDSFlightEntity> transferFlights2 = getDirectFlights(directDestination, destination, startTime);
                Date transfer2LastDeparture = transferFlights2.get(transferFlights2.size() - 1).getDepartureDate();
                Date transfer1FirstArrival = transferFlights1.get(0).getArrivalDate();
                if (transfer2LastDeparture.after(transfer1FirstArrival)) {
                    // 1 set of transfer flights detected
                    GDSTransferFlight transferSet = new GDSTransferFlight();
                    transferSet.setRoute1("From " + origin + " to " + directDestination);
                    transferSet.setRoute2("From " + directDestination + " to " + destination);
                    transferSet.setFlights1(transferFlights1);
                    transferSet.setFlights2(transferFlights2);
                    transferFlightList.add(transferSet);
                }
            }
        }
        return transferFlightList;
    }

}
