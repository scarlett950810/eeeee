/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.distribution.sessionbean;

import imas.distribution.entity.TicketEntity;
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
    
    // a flight would be availble for selling until counter closed
    public List<FlightEntity> getAllSellingFlights();

    public int getQuotaLeft(BookingClassEntity bookingClassEntity);

    public List<TicketEntity> createTicketEntitiesWithoutPersisting(BookingClassEntity bookingClass, int number);

    public void runYieldManagementsRulesOnFlight(FlightEntity flight);

    public List<String> getAllCountryNames();

    public List<AirportEntity> getAllAirportsInCountry(String countryName);

    public List<AirportEntity> getAllDestinationAirports(AirportEntity airport);

    public List<FlightEntity> getAvailableFlights(RouteEntity route, Date lowerBound, Date upperBound);

    public List<TransferFlight> getTransferFlights(AirportEntity origin, AirportEntity destination, Date departureDate);

    public RouteEntity getDirectRoute(AirportEntity origin, AirportEntity destination);

    public Date getDateAfterDays(Date origin, int daysToadd);

    /**
     *
     * @param flight
     * @param seatClass
     * @param totalPurchaseNo
     * @return
     */
    public List<BookingClassEntity> getAvailableBookingClassUnderFlightUnderSeatClass(FlightEntity flight, String seatClass, int totalPurchaseNo);

    public boolean reachable(AirportEntity origin, AirportEntity destination);

}
