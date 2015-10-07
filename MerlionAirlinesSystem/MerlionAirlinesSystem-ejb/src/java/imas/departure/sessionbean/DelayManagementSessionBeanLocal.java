/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.departure.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lei
 */
@Local
public interface DelayManagementSessionBeanLocal {

    public void fetchDelayFlights(FlightEntity flight);

    public List<FlightEntity> fetchFlights(String base);

   
    
}
