/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.planning.entity.AirportEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface FlightLookupSessionBeanLocal {

    public List<String> getAllCountryNames();

    public List<AirportEntity> getAllAirportsInCountry(String countryName);

    public List<AirportEntity> getAllDestinationAirports(AirportEntity airport);

    public List<FlightEntity> getAvailableFlights(RouteEntity route, Date departureDate);

    public List<RouteEntity> getTransfer1Routes(AirportEntity origin, AirportEntity destination);

    public RouteEntity getDirectRoutes(AirportEntity origin, AirportEntity destination);
    
}
