/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.sessionbean;

import GDS.entity.GDSAirportEntity;
import GDS.entity.GDSBookingClassEntity;
import GDS.entity.GDSFlightEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface GDSFlightSessionBeanLocal {
    public void generateFlightsAndBookingClasses(GDSFlightEntity flight, List<GDSBookingClassEntity> bookingClasses);
    public boolean haveDirectFlight(GDSAirportEntity origin, GDSAirportEntity destination, Date startTime);
    public List<GDSFlightEntity> getDirectFlights(GDSAirportEntity origin, GDSAirportEntity destination, Date startTime);
    public List<GDSTransferFlight> getTransferFlightSet(GDSAirportEntity origin, GDSAirportEntity destination, Date startTime);
}
