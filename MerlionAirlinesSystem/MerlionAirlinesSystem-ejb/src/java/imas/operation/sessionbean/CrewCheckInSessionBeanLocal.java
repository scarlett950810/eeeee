/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.operation.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Howard
 */
@Local
public interface CrewCheckInSessionBeanLocal {

    List<FlightEntity> fetchFlights(String base);

    void doCrewCheckIn(List<PilotEntity> pilots, List<CabinCrewEntity> cabinCrew);

    void doCrewBoarding(List<PilotEntity> pilots, List<CabinCrewEntity> cabinCrew);
    
}
