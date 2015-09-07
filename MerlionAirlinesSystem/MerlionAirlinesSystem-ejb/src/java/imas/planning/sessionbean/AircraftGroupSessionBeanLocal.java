/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Scarlett
 */
@Local
public interface AircraftGroupSessionBeanLocal {

    public void addAircraftGroup(String type);

    public boolean checkAircraftGroupExistense(String type);
    
    public List<AircraftGroupEntity> getAircraftGroups();

    public List<AircraftEntity> getAircraftsFromGroup(AircraftGroupEntity aircraftGroup);

    public void aircraftDetachGroup(AircraftEntity aircraft);

    public void aircraftChangeGroup(AircraftEntity aircraft, AircraftGroupEntity groupToUpdate);
    
}
