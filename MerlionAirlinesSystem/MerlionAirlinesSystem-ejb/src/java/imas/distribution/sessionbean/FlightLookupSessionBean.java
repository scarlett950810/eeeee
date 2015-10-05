/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    public RouteEntity getDirectRoutes(AirportEntity origin, AirportEntity destination) {

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
    public List<RouteEntity> getTransfer1Routes(AirportEntity origin, AirportEntity destination) {

        // direct routes
        Query queryForRoute = entityManager.createQuery("SELECT r FROM RouteEntity r where r.originAirport = :originAirport and r.destinationAirport = :destinationAirport");
        if (queryForRoute.getResultList().isEmpty()) {
            RouteEntity directFlightRoute = null;
            return null;
        } else {
            return queryForRoute.getResultList();
        }

    }

    @Override
    public List<FlightEntity> getAvailableFlights(RouteEntity route, Date departureDate) {

        Calendar upperBound = Calendar.getInstance();
        upperBound.setTime(departureDate);
        upperBound.add(Calendar.DATE, 1);
        Date upperBoundDate = upperBound.getTime();
        Query queryForAvailableFlights = entityManager.createQuery("SELECT f FROM FlightEntity f where f.route = :route and f.departureDate > :lowerBound AND f.departureDate < :upperBound");
        queryForAvailableFlights.setParameter("route", route);
        queryForAvailableFlights.setParameter("lowerBound", departureDate);
        queryForAvailableFlights.setParameter("upperBound", upperBoundDate);

        List<FlightEntity> availableFlights = queryForAvailableFlights.getResultList();
        return availableFlights;

    }

}
