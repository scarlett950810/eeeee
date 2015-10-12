/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 *
 * @author wutong
 */
@Local
public interface CrewToFlightSessionBeanLocal {

    List<PilotEntity> retreiveAvailablePilot(AircraftEntity aircraft, RouteEntity route, Date departureDate, Date arrivalDate);

    List<PilotEntity> retrieveAllPilots();

    List<CabinCrewEntity> retrieveAllCabinCrew();

    List<CabinCrewEntity> retrieveAvailableCabinCrew(RouteEntity route, Date departureDate, Date arrivalDate);

    void createFlight(FlightEntity flight, FlightEntity returnFlight);

    List<FlightEntity> retrieveAllFlights();

    void deleteFlight(FlightEntity flight);
    
}
