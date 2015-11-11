/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lei
 */
@Remote
public interface BookingClassesManagementSessionBeanRemote {

    public double computeHistoricalShowRate(RouteEntity route);

    public int getSeatClassCapacity(FlightEntity flight, String seatClass);

    public List<FlightEntity> getFlightCandidateToOpenForBooking();

    public void createBookingClassesAndTAndCs(FlightEntity flight);

    public void autoPriceFlightsNeedToBePriced(int monthToDeparture);

}
