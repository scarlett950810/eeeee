/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface BookingClassesManagementSessionBeanLocal {

    public double computeHistoricalShowRate(RouteEntity route);
    
    public int getSeatClassCapacity(FlightEntity flight, String seatClass);

/*
    public int getFirstClassCapacity(FlightEntity flight);

    public int getBusinessClassCapacity(FlightEntity flight);

    public int getPremiumEconomyClassCapacity(FlightEntity flight);

    public int getEconomyClassCapacity(FlightEntity flight);
    
    public void generateFirstClassBookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateBusinessClassBookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generatePremiumEconomyClassBookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateEconomyClass1BookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateEconomyClass2BookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateEconomyClass3BookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateEconomyClass4BookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateEconomyClass5BookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateFirstClassAgencyBookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateBusinessClassAgencyBookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generatePremiumEconomyClassAgencyBookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);

    public void generateEconomyClassAgencyBookingClassEntityAndTAndC(FlightEntity flight, double price, int quota);
*/

    public List<FlightEntity> getFlightCandidateToOpenForBooking();

    public void createBookingClassesAndTAndCs(FlightEntity flight);

    public void autoPriceFlightsNeedToBePriced(int monthToDeparture);

}
