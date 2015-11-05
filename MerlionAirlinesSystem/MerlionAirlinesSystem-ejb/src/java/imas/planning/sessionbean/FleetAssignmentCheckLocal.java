/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.FlightEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface FleetAssignmentCheckLocal {

    List<FlightEntity> fleetAssignmentCapacityCheck(List<FlightEntity> flights, List<AircraftEntity> aircrafts);

    List<FlightEntity> oneAircraftAssignment(AircraftEntity aircraft, List<FlightEntity> flights);
    Double calculateMaintenanceHours(AircraftEntity aircraft, Date mtAcc);
    List<FlightEntity> getParticularPlanningPeriodFlights(Integer planningY, Date startingDate);
}
