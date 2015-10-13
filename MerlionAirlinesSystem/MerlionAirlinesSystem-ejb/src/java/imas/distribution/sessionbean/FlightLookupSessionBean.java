/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.Calendar;
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
public class FlightLookupSessionBean implements FlightLookupSessionBeanLocal {

    @EJB
    private DistributionSessionBeanLocal distributionSessionBean;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> getAllCountryNames() {

        Query queryForAllCountryNames = entityManager.createQuery("SELECT DISTINCT a.nationName FROM AirportEntity a ORDER BY a.nationName ASC");
        List<String> allCountryNames = queryForAllCountryNames.getResultList();

        return allCountryNames;

    }

    @Override
    public List<AirportEntity> getAllAirportsInCountry(String countryName) {

        Query queryForAllAirportsInCountry = entityManager.createQuery("SELECT a FROM AirportEntity a where a.nationName = :nationName");
        queryForAllAirportsInCountry.setParameter("nationName", countryName);
        List<AirportEntity> allAirportsInCountry = queryForAllAirportsInCountry.getResultList();

        return allAirportsInCountry;

    }

    @Override
    public List<AirportEntity> getAllDestinationAirports(AirportEntity airport) {

        Query queryForAllDestinationAirports = entityManager.createQuery("SELECT r.destinationAirport FROM RouteEntity r where r.originAirport = :originAirport GROUP BY r.destinationAirport.nationName ORDER BY r.destinationAirport.nationName ASC");
        queryForAllDestinationAirports.setParameter("originAirport", airport);
        List<AirportEntity> allDestinationAirportsByCountry = queryForAllDestinationAirports.getResultList();

        return allDestinationAirportsByCountry;

    }

    @Override
    public RouteEntity getDirectRoute(AirportEntity origin, AirportEntity destination) {

        // direct routes
        Query queryForRoute = entityManager.createQuery("SELECT r FROM RouteEntity r where r.originAirport = :originAirport and r.destinationAirport = :destinationAirport");
        queryForRoute.setParameter("originAirport", origin);
        queryForRoute.setParameter("destinationAirport", destination);

        if (queryForRoute.getResultList().isEmpty()) {
            RouteEntity directFlightRoute = null;
            return null;
        } else {
            return (RouteEntity) queryForRoute.getResultList().get(0);
        }

    }

    @Override
    public List<FlightEntity> getAvailableFlights(RouteEntity route, Date lowerBound, Date upperBound) {

        Query queryForAvailableFlights = entityManager.createQuery("SELECT f FROM FlightEntity f where f.route = :route and f.departureDate > :lowerBound AND f.departureDate < :upperBound ORDER BY f.departureDate ASC");
        queryForAvailableFlights.setParameter("route", route);
        queryForAvailableFlights.setParameter("lowerBound", lowerBound);
        queryForAvailableFlights.setParameter("upperBound", upperBound);

        List<FlightEntity> availableFlights = queryForAvailableFlights.getResultList();
        return availableFlights;

    }

    // the staying time of transfer would not exceed 24 hours.
    @Override
    public List<TransferFlight> getTransferRoutes(AirportEntity origin, AirportEntity destination, Date departureDate) {
        List<TransferFlight> transferFlights = new ArrayList<>();
        List<AirportEntity> directDestinations = getAllDestinationAirports(origin);
        for (AirportEntity directDestination : directDestinations) {
            List<AirportEntity> transferDestinations = getAllDestinationAirports(directDestination);
            if (transferDestinations.contains(destination)) {
                // found transfer route
                RouteEntity route1 = getDirectRoute(origin, directDestination);
                if (route1 != null) {
                    List<FlightEntity> flights1 = getAvailableFlights(route1, departureDate, getDateAfterDays(departureDate, 1));
                    if (flights1.size() > 0) {
                        RouteEntity route2 = getDirectRoute(directDestination, destination);
                        if (route2 != null) {
                            Date lowerBound = flights1.get(0).getDepartureDate();
                            Date upperBound = getDateAfterDays(lowerBound, 1);

                            List<FlightEntity> flights2 = getAvailableFlights(route2, lowerBound, upperBound);

                            if (flights2.size() > 0) {
                                TransferFlight transferFlight = new TransferFlight(route1, route2, flights1, flights2);
                                transferFlights.add(transferFlight);
                            }
                        }
                    }
                }
            }
        }

        return transferFlights;
    }

    @Override
    public Date getDateAfterDays(Date origin, int daysToadd) {
        Calendar after = Calendar.getInstance();
        after.setTime(origin);
        after.add(Calendar.DATE, daysToadd);
        return after.getTime();
    }

    // for a given flight and seatClass, return all bookingclass under that seat class which has available quota
    @Override
    public List<BookingClassEntity> getAvailableBookingClassUnderFlightUnderSeatClass(FlightEntity flight, String seatClass, int totalPurchaseNo) {
        
        List<BookingClassEntity> allBookingClassesUnderFlight = flight.getBookingClasses();
        List<BookingClassEntity> allBookingClassUnderFlightUnderSeatClass = new ArrayList<>();

        for (BookingClassEntity bookingClassEntity : allBookingClassesUnderFlight) {
            if (bookingClassEntity.getSeatClass().equals(seatClass) && distributionSessionBean.getQuotaLeft(bookingClassEntity) > totalPurchaseNo) {
                allBookingClassUnderFlightUnderSeatClass.add(bookingClassEntity);
            }
        }

        return allBookingClassUnderFlightUnderSeatClass;
    }

}
