/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruicai
 */
@Local
public interface TestFunctionsFAandCSLocal {

    List<FlightEntity> getAllFlights();

    List<PilotEntity> getAllPilots();
    
}
