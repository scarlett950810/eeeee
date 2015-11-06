/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface CrewSchedulingCheckLocal {
    List<FlightEntity> pilotSchedulingCapacityCheck(List<FlightEntity> flights, List<PilotEntity> pilots);

    List<FlightEntity> pilotSchedulingForShortDistance(List<FlightEntity> shortFlights, List<PilotEntity> pilots);

    List<FlightEntity> onePilotSchedulingForShort(List<FlightEntity> flights, PilotEntity pilot);
    
    List<FlightEntity> pilotSchedulingForLongDistance(List<FlightEntity> shortFlights, List<PilotEntity> pilots);

    List<FlightEntity> onePilotSchedulingForLong(List<FlightEntity> flights, PilotEntity pilot);
    
//    Date calculateMaintenanceHours(PilotEntity pilot, Date mtAcc);

    List<PilotEntity> retriveAllPilots();
    Integer getFlightCapacity(FlightEntity flight);

    List<CabinCrewEntity> retrieveAllCabinCrew();
    

    List<FlightEntity> oneCabinCrewScheduling(List<FlightEntity> flights, CabinCrewEntity cabinCrew);
    List<FlightEntity> CabinCrewSchedulingCapacityCheck(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews);
    List<FlightEntity> CabinScheduling(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews);
 List<FlightEntity> fleetAssignmentCheck(List<FlightEntity> flights, List<AircraftEntity> aircrafts);
Double calculateMaintenanceHours(AircraftEntity aircraft, Date mtAcc);
    HashMap oneAircraftAssignment(AircraftEntity aircraft, HashMap<String, List<FlightEntity>> flight);
 
}
