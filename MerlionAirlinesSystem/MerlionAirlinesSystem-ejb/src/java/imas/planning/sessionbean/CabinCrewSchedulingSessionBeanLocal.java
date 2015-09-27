/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.FlightEntity;
import javax.ejb.Local;

/**
 *
 * @author wutong
 */
@Local
public interface CabinCrewSchedulingSessionBeanLocal {

    Integer getFlightCapacity(FlightEntity flight);
    
}
