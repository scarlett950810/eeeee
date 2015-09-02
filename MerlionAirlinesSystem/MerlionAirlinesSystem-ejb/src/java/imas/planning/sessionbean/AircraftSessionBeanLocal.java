/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AirportEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface AircraftSessionBeanLocal {
    public List<String> getSeatClasses();
    
    public List<AircraftGroupEntity> getAircraftGroups();

    public List<AirportEntity> getAirports();
    
}
