/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface CrewSchedulingSessionBeanLocal {

    List<FlightEntity> pilotScheduling(List<FlightEntity> flights, List<PilotEntity> pilots);

    List<FlightEntity> pilotSchedulingForShortDistance(List<FlightEntity> shortFlights, List<PilotEntity> pilots);

    List<FlightEntity> onePilotSchedulingForShort(List<FlightEntity> flights, PilotEntity pilot);
    
    List<FlightEntity> pilotSchedulingForLongDistance(List<FlightEntity> shortFlights, List<PilotEntity> pilots);

    List<FlightEntity> onePilotSchedulingForLong(List<FlightEntity> flights, PilotEntity pilot);
    
//    Date calculateMaintenanceHours(PilotEntity pilot, Date mtAcc);

    List<PilotEntity> retriveAllPilots();

}
