/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wutong
 */
@Local
public interface CabinCrewSchedulingSessionBeanLocal {

    Integer getFlightCapacity(FlightEntity flight);
    List<FlightEntity> oneCabinCrewScheduling(List<FlightEntity> flights, CabinCrewEntity cabinCrew);
    List<FlightEntity> CabinCrewScheduling(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews);
    List<FlightEntity> CabinScheduling(List<FlightEntity> flights, List<CabinCrewEntity> cabinCrews);
}
