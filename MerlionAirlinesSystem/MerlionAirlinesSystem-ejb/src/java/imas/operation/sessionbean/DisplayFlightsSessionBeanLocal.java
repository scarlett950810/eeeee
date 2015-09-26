/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface DisplayFlightsSessionBeanLocal {

    public List<RouteEntity> getAllRoutes();

    public List<FlightEntity> getFlightsUnderRoute(RouteEntity route);

    public List<AircraftEntity> getAllAircrafts();

    public List<FlightEntity> getFlightsUnderAircraft(AircraftEntity aircraft);
    
}
