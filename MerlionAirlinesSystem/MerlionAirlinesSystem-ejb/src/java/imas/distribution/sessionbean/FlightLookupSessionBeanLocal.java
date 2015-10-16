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

    public List<FlightEntity> getAvailableFlights(RouteEntity route, Date lowerBound, Date upperBound);

    public List<TransferFlight> getTransferRoutes(AirportEntity origin, AirportEntity destination, Date departureDate);

    public RouteEntity getDirectRoute(AirportEntity origin, AirportEntity destination);

    public Date getDateAfterDays(Date origin, int daysToadd);

    /**
     *
     * @param flight
     * @param seatClass
     * @return
     */
    public List<BookingClassEntity> getAvailableBookingClassUnderFlightUnderSeatClass(FlightEntity flight, String seatClass, int totalPurchaseNo);

    public boolean reachable(AirportEntity origin, AirportEntity destination);

}
